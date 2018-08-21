import os
import os.path
import exe
import numpy as np
from numpy import ndarray
from ml import larff
from collections import UserList

logging = exe.getlogger("SL.ML")

# Read the path of data set folder from environment variable: 'DATASET_PATH'

if "DATASET_PATH" in os.environ:
    DATASET_PATH = os.environ["DATASET_PATH"]
    if DATASET_PATH[-1] is not '/':
        DATASET_PATH += '/'
else:
    DATASET_PATH = "testrsc/ml/datasets/"
    logging.warn("Environment variable 'DATASET_PATH' isn't defined.")
    logging.info("Using %s as default path to data sets. Change default in: MLDataSets.java",
                 DATASET_PATH)

if not os.path.isdir(DATASET_PATH):
    logging.fatal("Dataset folder %s doesn't exist.", os.path.abspath(DATASET_PATH))

def dataset_abspath(relative_path:str)->str:
    if DATASET_PATH.endswith("/") and relative_path.startswith("/"):
        relative_path = relative_path[1:] # chop the duplicate '/' letter off.
    return os.path.abspath(DATASET_PATH + relative_path)

def load_dataset(relative_path:str, classindex = None)->dict:
    dataset_path = dataset_abspath(relative_path)
    with open(dataset_path, "r") as fp:
        dataset = larff.load(fp)

    prepare_dataset(dataset, classindex)
    return dataset

def create_dataset(arff:str, classindex = None)->dict:
    dataset = larff.loads(arff)
    prepare_dataset(dataset, classindex)
    return dataset

def prepare_dataset(dataset:dict, classindex = None):
    set_classindex(dataset, classindex)


def get_attr_dtype(dataset:dict) -> str:
    dtype = ""
    for attr in dataset["attributes"]:
        type_ = attr[1]
        if isinstance(type_, list):
            dtype += "U"
            length = 0
            for classname in type_:
                if len(classname) > length:
                    length = len(classname)
            dtype += str(length)
        elif isinstance(type_, str) and type_.upper() == "REAL":
            dtype += "f8"
        else:
            dtype += "O"
        # TODO other classes

        dtype +=", "
    return dtype[:-2]



def store_dataset(dataset:dict, relative_path:str):
    dataset_path = dataset_abspath(relative_path)
    parent_dir = os.path.dirname(dataset_path)
    os.makedirs(parent_dir, exist_ok=True)
    if not os.path.isdir(parent_dir):
        raise ValueError("Cannot sore dataset to path: %s. Not a directory." % dataset_path)
    with open(dataset_path, "w") as fp:
        larff.dump(dataset, fp)


def attr_count(dataset:dict)->int:
    return len(dataset["attributes"])

def classindex(dataset) :
    return dataset["classindex"]

def set_classindex(dataset:dict, classindex = None):
    if classindex is not None:
        attrCount = attr_count(dataset)
        dataset["classindex"] = (classindex % attrCount)
    else:
        dataset["classindex"] = None

def has_classindex(dataset:dict)-> bool:
    return "classindex" in dataset and dataset["classindex"] is not None

def onehot_attr(attribute):
    attribute_type = attribute[1]
    if attribute_type == 'REAL':
        # already numeric attribute. Add attribute as it is:
        return [attribute]
    elif isinstance(attribute_type, list):
        # attribute is categorical:
        if len(attribute_type) == 0:
            raise ValueError("Categorical attribute has no categories defined: " + str(attribute))
        attribute_name = attribute[0]
        onehotted = list()
        for category_index in range(0, len(attribute_type)):
            hot_attr = (attribute_name + "_" + attribute_type[category_index] + "_" +str(category_index), 'REAL')
            onehotted.append(hot_attr)
        return onehotted
    else:
        raise ValueError("Cannot handle attribute type: " + str(attribute))


def onehot_and_splitXY(dataset:dict) -> dict:
    """
    onehottes the data section of the given data set and splits inputs and output data into two separate arrays.
    The returned dict contains 'data_x' and 'data_y' instead of 'data'.
    :param dataset: dict in the format returned by larff parser.
    :return: dict in a similar format to the one returned by larff parser. instead of 'data' it contains 'data_x' and 'data_y'.
    """
    n_dataset = dict()
    n_dataset["description"] = dataset["description"]
    n_dataset["relation"] = dataset["relation"]

    n_dataset["attributes"] = list()
    n_dataset["data_x"] = list()
    n_dataset["data_y"] = list()

    # local variables for quick access
    n_attr: list = n_dataset["attributes"]
    n_data_x: list = n_dataset["data_x"]
    n_data_y: list = n_dataset["data_y"]
    attr: list = dataset["attributes"]
    data: list = dataset["data"]

    attr_range = range(len(attr))

    classindex = dataset["classindex"]

    if classindex is None: # no class defined
        classindex = -1 # set a to a number which cant be reached by any attribute index.

    for attribute_index in attr_range:
        if attribute_index == classindex:
            continue # skipp the class index
        attribute: tuple = attr[attribute_index]
        onehotted = onehot_attr(attribute)
        n_attr.extend(onehotted)

    # add the class index at the end.
    if classindex >= 0:
        n_attr.append(attr[classindex])
        n_dataset["classindex"] = len(n_attr) - 1
    else:
        n_dataset["classindex"] = None



    for datapoint in data:
        n_datapoint = list()

        for attr_index in attr_range:
            if attr_index == classindex:
                n_data_y.append(datapoint[attr_index])

            attribute_type = attr[attr_index][1]

            if attribute_type == 'REAL':
                n_datapoint.append(datapoint[attr_index])

            elif isinstance(attribute_type, list):
                for category in attribute_type:
                    if datapoint[attr_index] == category:
                        n_datapoint.append(1.)
                    else:
                        n_datapoint.append(0.)

        n_data_x.append(n_datapoint)

    return n_dataset

def mergeXY(dataset:dict) -> dict:
    """
    Merges the input array and the output array of the given dataset inplace.
    The returned dataset can be fed to larff parser to create an arff representation.
    """
    if "data_x" in dataset and "data_y" in dataset:
        data_x:list = dataset["data_x"]
        data_y = dataset["data_y"]
        rowcount = min(len(data_x), len(data_y))
        for data_index in range(rowcount):
            data_x[data_index].append(data_y[data_index])
        dataset["data"] = data_x
        return dataset
    else:
        raise ValueError("Dataset format not recognized. No dataset_x and dataset_y entry.")

class NumericDatasetCaster:
    @staticmethod
    def cts_NumericDataset(dataset:'dict')->bytes:
        dataset = mergeXY(dataset)
        return larff.dumps(dataset).encode()

    @staticmethod
    def cfs_NumericDataset(byte_arr: bytes)->dict:
        arffstring = byte_arr.decode()
        dataset = larff.loads(arffstring)
        return onehot_and_splitXY(dataset)

class NdArrayCaster:
    @staticmethod
    def cts_ndarray(arr: ndarray) -> bytes:
        if len(arr.shape) > 1:
            raise ValueError("Cannot cast a ndarray with more than 1 dimension to a list. array shape: " + str(ndarray.shape))
        list_ = arr.tolist()
        from de.upb.sede import BuiltinCaster
        return BuiltinCaster.cts_List(list_)

    @staticmethod
    def cfs_ndarray(byte_arr: bytes) -> ndarray:
        from de.upb.sede import BuiltinCaster
        list_ = BuiltinCaster.cfs_List(bytes)
        return np.asanyarray(list_)