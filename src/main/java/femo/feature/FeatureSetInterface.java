package femo.feature;

import femo.modeling.Example;
import femo.modeling.ExampleDensity;

public interface FeatureSetInterface <DataType> {
    public Example getExample(DataType dataObject) throws Exception;
}
