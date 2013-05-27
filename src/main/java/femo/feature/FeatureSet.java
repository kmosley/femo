package femo.feature;

import femo.modeling.Example;
import femo.modeling.ExampleDensity;

import java.io.Serializable;
import java.util.ArrayList;

public final class FeatureSet <DataType> implements FeatureSetInterface<DataType>, Serializable {

    protected ArrayList<Feature<DataType>> predictorFeatures = new ArrayList<Feature<DataType>>();
    protected ExampleDensity density;

    protected FeatureSet(ArrayList<Feature<DataType>> predictorFeatures, ExampleDensity density){
        this.predictorFeatures = predictorFeatures;
        this.density = density;
    }

    @Override
    public Example getExample(DataType dataObject) throws Exception {
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
}
