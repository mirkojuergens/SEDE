import exe
logging = exe.logging

from exe.executor import Executor
from exe.config import ExecutorConfig
from http import server

from util.com import MultiContextHandler, StringServerResponse,\
    BasicClientRequest, HttpClientRequest, ByteServerResponse

from procedure.data_procedures import TransmitDataProcedure, FinishProcedure
from exe.data import SEDEObject, SEMANTIC, PrimitiveType
from exe import data

from exe.requests import DataPutRequest, ExecRequest


def create_put_request(host, fieldname, executionId, unavailable:bool=True, semtype=SEMANTIC) -> BasicClientRequest:
    dataPutUrl = host + "/put/" + executionId + "/" + fieldname + "/"
    if unavailable:
        dataPutUrl += "unavailable"
    else:
        dataPutUrl += semtype
    return HttpClientRequest(dataPutUrl)


class PutDataHandler(ByteServerResponse):

    def __init__(self, executor):
        self.executor : Executor = executor

    def receive_bytes(self, input_bytes: bytes, executionId, fieldname, semtype) -> str:
        logging.trace("%s received data at %s/%s/%s", self.executor.config.executor_id, executionId, fieldname, semtype)
        try:
            if semtype != "unavailable":
                input_data: SEDEObject = data.decode(input_bytes, semtype)
                unavailable = False
            else:
                input_data = None
                unavailable = True
            put_request = DataPutRequest(requestId=executionId, data=input_data, fieldname=fieldname, unavailable=unavailable)
            self.executor.put(put_request)
            return ""
        except Exception as e:
            logging.exception("Exception in executor %s while receiving data at url: %s/%s/%s",
                              self.executor.config.executor_id, executionId, fieldname, semtype)
            return str(e)


class ExecuteGraphHandler(StringServerResponse):
    def __init__(self, executor):
        self.executor: Executor = executor

    def receive_str(self, input_str: str) -> str:
        try:
            logging.debug("%s received execute request.", self.executor.config.executor_id)
            execRequest: ExecRequest = ExecRequest.from_json_string(input_str)
            self.executor.execute(execRequest)
            return ""
        except Exception as e:
            logging.exception("Exception in executor %s while receiving exec request.",
                              self.executor.config.executor_id)
            return str(e)

class InterruptHandler(StringServerResponse):

    def __init__(self, executor):
        self.executor: Executor = executor

    def receive_str(self, input_str: str, executionId:str) -> str:
        try:
            self.executor.interrupt(executionId)
            return "";
        except Exception as e:
            logging.exception("Exception in executor %s while receiving interrupt request for execution %s",
                              self.executor.config.executor_id, executionId)
            return str(e)



class TransmitDataOverHttp(TransmitDataProcedure):

    def get_put_request(self, task, unavailable:bool)->BasicClientRequest:
        host = task["contact-info"]["host-address"]
        fieldname = task["fieldname"]
        if "semantic-type" in task:
            semtype = task["semantic-type"]
        else:
            semtype = None
        executionId = task.execution.exec_id
        if semtype is None:
            sedeObj:SEDEObject = task.execution.env[fieldname]
            semtype = sedeObj.type
        logging.trace("Execution '%s': Transmitting %s of type %s to %s.", executionId, fieldname, semtype, host)
        return create_put_request(host,fieldname,executionId,unavailable,semtype)


class FinishOverHttp(FinishProcedure):

    def get_finish_flag_request(self, task)-> BasicClientRequest:
        host = task["contact-info"]["host-address"]
        fieldname = task["fieldname"]
        semtype = PrimitiveType.Bool.name
        executionId = task.execution.exec_id
        logging.trace("Execution '%s': Notifying %s with a finish flag.", executionId, host)
        return create_put_request(host, fieldname, executionId, unavailable=False, semtype=semtype)


class HTTPExecutor(Executor):

    def __init__(self, config, host_address:str, port:int):

        super().__init__(config)
        self.host_address = host_address
        self.port = port
        self.bind_http_procedure_names()
        self.request_handler = MultiContextHandler()
        self.request_handler.add_context("/put/(?P<executionId>\w+)/(?P<fieldname>(?:[&_a-zA-Z][&\w]*))/(?P<semtype>\w+)",
                                         self.handler_put_data)
        self.request_handler.add_context("/execute",
                                         self.handler_execute)
        self.request_handler.add_context("/interrupt/(?P<executionId>\w+)",
                                         self.handler_interrupt)

        self.httpserver = server.HTTPServer(("", port), self.request_handler)
        logging.info("Starting Python executor http server: '%s'. host: %s port: %i", config.executor_id, host_address, port)


    def bind_http_procedure_names(self):
        self.worker_pool.bind_procedure("TransmitData", TransmitDataOverHttp)
        self.worker_pool.bind_procedure("Finish", FinishOverHttp)

    def handler_put_data(self):
        return PutDataHandler(self)

    def handler_execute(self):
        return ExecuteGraphHandler(self)

    def handler_interrupt(self):
        return InterruptHandler(self)

    def contact_info(self):
        d = super().contact_info();
        d["host-address"] = self.host_address + ":" + str(self.port)
        return d

    def start_listening(self):
        try:
            self.httpserver.serve_forever()
        except KeyboardInterrupt:
            self.httpserver.shutdown()


if __name__ == "__main__":
    executor = HTTPExecutor(ExecutorConfig.empty_config(), "localhost", 5000)
    executor.start_listening()


