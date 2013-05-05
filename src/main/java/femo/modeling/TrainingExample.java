package femo.modeling;

import femo.exception.InvalidFeatureValueException;
import femo.feature.FeatureValue;

import java.util.HashMap;

public class TrainingExample extends Example {
    public final HashMap<String, Object> metaData;
    public FeatureValue responseFeatureValue;

    public TrainingExample(Example example, FeatureValue responseFeatureValue) throws InvalidFeatureValueException {
        metaData = new HashMap<String, Object>();
        this.type = example.type;
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
