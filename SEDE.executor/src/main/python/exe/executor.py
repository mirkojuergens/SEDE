import exe
logging = exe.logging

from exe import req as requests
from exe.execution import Execution
from exe.workers import WorkerPool
from util.observing import Observer
from exe.config import ExecutorConfig
from typing import Dict
from threading import RLock

class ExecutionPool:
    execMap: Dict[str, Execution]
    config: ExecutorConfig
    lock: RLock

    def __init__(self, config:ExecutorConfig):
        self.config = config
        self.execMap = dict()
        self.lock = RLock()
        pass

    def remove_execution(self, execution: Execution) -> Execution:
        with self.lock:
            if execution.exec_id in self.execMap:
                del self.execMap[execution.exec_id]

    def get_orcreate_execution(self, execution_id: str) -> Execution:
        with self.lock:
            if execution_id in self.execMap:
                return self.execMap[execution_id]
            else:
                logging.debug("%s created a new execution: %s", self.config.executor_id, execution_id);
                execution = Execution(execution_id, self.config)
                self.execMap[execution_id] = execution
                return execution

    def execIdTaken(self, execId) -> bool:
        with self.lock:
            if execId not in self.execMap:
                return False
            elif self.execMap[execId].has_graph:
                return True
            else:
                return False


class Executor:
    execPool: ExecutionPool
    config: ExecutorConfig
    worker_pool: WorkerPool
    task_enqueuer: Observer
    executor_garbage_collector: Observer

    def __init__(self, config: ExecutorConfig):
        self.execPool = ExecutionPool(config)
        self.config = config
        self.worker_pool = WorkerPool(config.thread_count)

        self.task_enqueuer = Observer(lambda task: True,
                                      self.worker_pool.submit_task,
                                      lambda task: False)
        self.executor_garbage_collector = Observer(
            Execution.has_execution_finished, self.remove_execution)
        self.bind_procedure_names()
        self.lock = RLock()

    def bind_procedure_names(self):
        from procedure.data_procedures import \
            ParseConstatProcedure,\
            AcceptDataProcedure, \
            CastTypeProcedure, \
            ServiceInstanceStorageProcedure
        from procedure.inst_procedure import InstructionProcedure

        self.worker_pool.bind_procedure("Instruction", InstructionProcedure)
        self.worker_pool.bind_procedure("ParseConstant", ParseConstatProcedure)
        self.worker_pool.bind_procedure("AcceptData", AcceptDataProcedure)
        self.worker_pool.bind_procedure("CastType", CastTypeProcedure)
        self.worker_pool.bind_procedure("ServiceInstanceStorage", ServiceInstanceStorageProcedure)
        # send graph and transmit data needs to be bounded from outside because based
        # on the type of this executor they require different of implementations.

    def remove_execution(self, execution: Execution):
        with self.lock:
            self.execPool.remove_execution(execution)
            self.worker_pool.remove_execution(execution)

    def put(self, dataputrequest: 'requests.DataPutRequest'):
        with self.lock:
            execution = self.execPool.get_orcreate_execution(
                dataputrequest.requestId)
            environment = execution.env
            if dataputrequest.unavailable:
                # The request indicates that the data is unavailable. (wont be delivered)
                environment.mark_unavailable(dataputrequest.fieldname)
            else:
                # The request contains the data:
                environment[dataputrequest.fieldname] = dataputrequest.data

    def execute(self, execrequest: 'requests.ExecRequest'):
        with self.lock:
            execId = execrequest.requestId
            # First check if execution id is taken:
            if self.execPool.execIdTaken(execId):
                raise Exception("Execution Id is already taken: {}".format(execId))
            execution = self.execPool.get_orcreate_execution(execId)
            execution.runnable_tasks.observe(self.task_enqueuer)
            execution.deserialize_graph(execrequest.graph)
            execution.state.observe(self.executor_garbage_collector)
            logging.info("Execution request {} started.".format(execId))
            return execution

    def interrupt(self, executionId: str):
        with self.lock:
            if self.execPool.execIdTaken(executionId):
                logging.info("")
                execution = self.execPool.get_orcreate_execution(executionId)
                self.worker_pool.interrupt(execution)
                execution.interrupt()

    def contact_info(self):
        return {"id": self.config.executor_id}

    def registration(self):
        return requests.ExecutorRegistration.from_config(self.contact_info(), self.config)
