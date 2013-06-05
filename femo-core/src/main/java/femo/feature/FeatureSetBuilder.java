package femo.feature;

import femo.modeling.ExampleDensity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FeatureSetBuilder <InputDataType> {

    protected ArrayList<Feature<InputDataType>> setFeatures;
    protected ExampleDensity exampleDensity;

    public FeatureSetBuilder(){
        setFeatures = new ArrayList<Feature<InputDataType>>();
        exampleDensity = ExampleDensity.Dense;
    }

    public FeatureSetBuilder<InputDataType> setExampleDensity(ExampleDensity exampleDensity){
        this.exampleDensity = exampleDensity;
        return this;
    }

    public FeatureSetBuilder<InputDataType> addFeature(Feature<InputDataType> feature){
        setFeatures.add(feature);
        return this;
    }

    public FeatureSetBuilder<InputDataType> addFeatures(Collection<? extends Feature<InputDataType>> features){
        setFeatures.addAll(features);
        return this;
    }

    public FeatureSet<InputDataType> build(){
        return new FeatureSet<InputDataType>(setFeatures, exampleDensity);
    }
}
