package femo.normalizer;

import femo.feature.FeatureValue;

import java.io.Serializable;

public abstract class Normalizer<InputType, OutputType> implements Serializable {
    public abstract OutputType normalize(InputType input);

    public FeatureValue<OutputType> normalize(FeatureValue<InputType> featureValue) {
        return new FeatureValue<OutputType>(featureValue, normalize(featureValue.getValue()));
    }
}
