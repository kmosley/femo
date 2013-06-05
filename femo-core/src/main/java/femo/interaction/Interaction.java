package femo.interaction;

import femo.feature.FeatureValue;

public abstract interface Interaction <FeatureInputType1, FeatureOutputType1 extends FeatureValue
        , FeatureInputType2, FeatureOutputType2 extends FeatureValue
        , InputType, OutputType extends FeatureValue> {
    //public abstract List<Feature<InputType, OutputType>> getInteractedFeatures(Feature<FeatureInputType1, FeatureOutputType1> feature1, Feature<FeatureInputType2, FeatureOutputType2> feature2);
}
