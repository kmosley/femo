package femo.feature;

import femo.modeling.Example;
import femo.modeling.ExampleDensity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class FeatureSet <DataType> implements IFeatureSet<DataType>, Serializable {

    protected final List<? extends Feature<DataType, ? extends Object>> predictorFeatures;

    public FeatureSet(List<? extends Feature<DataType, ? extends Object>> predictorFeatures){
        this.predictorFeatures = predictorFeatures;
    }

    public FeatureSet(Feature<DataType, ? extends Object> ... predictorFeatures){
        this.predictorFeatures = Arrays.asList(predictorFeatures);
    }

    @Override
    public Example getExample(DataType dataObject, ExampleDensity density) throws Exception {
        if(density == ExampleDensity.Dense)
            return getDenseExample(dataObject);
        return getSparseExample(dataObject);
    }

    public Example getDenseExample(DataType dataObject) throws Exception{
        Example example = new Example();
        for(Feature feature : predictorFeatures){
            example.add(feature.getFeatureValue(dataObject));
        }
        return example;
    }

    public Example getSparseExample(DataType dataObject) throws Exception{
        Example example = new Example();
        for(Feature feature : predictorFeatures){
            FeatureValue featureValue = feature.getFeatureValue(dataObject);
            if(featureValue.getValue() != null)
                example.add(featureValue);
        }
        return example;
    }

    public List<? extends Feature<DataType, ? extends Object>> getPredictorFeatures() {
        return predictorFeatures;
    }
}
