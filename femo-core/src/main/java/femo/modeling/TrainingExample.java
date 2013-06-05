package femo.modeling;

import femo.exception.InvalidFeatureValueException;
import femo.feature.FeatureValue;

import java.util.HashMap;

//TODO: maybe use composition instead of inheritance here
public class TrainingExample extends Example {
    public FeatureValue responseFeatureValue;

    public TrainingExample(Example example, FeatureValue responseFeatureValue) throws InvalidFeatureValueException {
        this.predictorFeatureValues = example.predictorFeatureValues;
        this.valueLookup = example.valueLookup;
        this.responseFeatureValue = responseFeatureValue;
        if(responseFeatureValue.getValue() == null)
            throw new InvalidFeatureValueException("response feature value cannot be null: "+responseFeatureValue.getName());
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(responseFeatureValue.getName()).append("=").append(responseFeatureValue.getValue()).append(" | ");
        sb.append(super.toString());
        return sb.toString();
    }
}
