package femo.modeling;

import femo.exception.InvalidFeatureNameException;
import femo.feature.FeatureValue;

import java.util.ArrayList;
import java.util.HashMap;

public class Example {
    protected ArrayList<FeatureValue> predictorFeatureValues;
    protected HashMap<String, FeatureValue> valueLookup;

    public Example(){
        predictorFeatureValues = new ArrayList<FeatureValue>();
        valueLookup = new HashMap<String, FeatureValue>();
    }

    public void add (FeatureValue featureValue) throws InvalidFeatureNameException {
        if(valueLookup.containsKey(featureValue.getName()))
            throw new InvalidFeatureNameException("Cannot duplicate feature names in Example");
        predictorFeatureValues.add(featureValue);
        valueLookup.put(featureValue.getName(), featureValue);
    }

    public ArrayList<FeatureValue> getPredictorFeatureValues(){
        return predictorFeatureValues;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(FeatureValue featureValue : predictorFeatureValues){
            if(sb.length() > 0)
                sb.append(" ");
            sb.append(featureValue.getName());
            sb.append("=");
            sb.append(featureValue.toString());
        }
        return sb.toString();
    }
}
