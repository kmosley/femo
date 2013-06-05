package femo.normalizer;

import femo.feature.FeatureValue;

public abstract class Normalizer<InputType, OutputType> {
    public abstract OutputType normalize(InputType input);

    public FeatureValue<OutputType> normalize(FeatureValue<InputType> featureValue) {
        return new FeatureValue<OutputType>(featureValue, normalize(featureValue.getValue()));
    }
}
