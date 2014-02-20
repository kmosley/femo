package femo.featureset;

import femo.modeling.Example;
import femo.modeling.ExampleDensity;

public interface IFeatureSet<DataType> {
    public Example getExample(DataType dataObject, ExampleDensity density) throws Exception;
}
