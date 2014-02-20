package femo.modeling;

import femo.exception.InvalidFeatureValueException;
import femo.feature.FeatureValue;

//TODO: maybe use composition instead of inheritance here
public class TrainingExample<ResponseValueType> extends Example {
    public FeatureValue<ResponseValueType> responseFeatureValue;

    public TrainingExample(Example example, FeatureValue<ResponseValueType> responseFeatureValue) throws InvalidFeatureValueException {
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
