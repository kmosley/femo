package femo.feature;

import femo.modeling.Example;

public interface FeatureSetInterface <DataType> {
    public Example getFeatureValues(DataType dataObject) throws Exception;
}
