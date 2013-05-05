package femo.feature;

import femo.modeling.Example;

import java.io.Serializable;

public abstract class FeatureSet <DataType> implements FeatureSetInterface<DataType>, Serializable {
    public abstract Example getFeatureValues(DataType dataObject) throws Exception;
}
