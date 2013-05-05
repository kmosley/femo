package femo.feature;

import femo.exception.InvalidDiscreteValueException;

import java.util.*;

public abstract class StringFeature <InputType> extends Feature<InputType> {

    Map<String, Integer> validValuesLookup;
    ArrayList<String> validValues;
    boolean addValueIfNotPresent;

    public StringFeature(String name){
        this(name, new ArrayList<String>(), true);
    }
    public StringFeature(String name, ArrayList<String> validValues, boolean addValueIfNotPresent){
        super(name);
        this.validValues = validValues;
        this.validValuesLookup = new HashMap<String, Integer>(validValues.size());
        for(int i=0; i<validValues.size(); i++)
            validValuesLookup.put(validValues.get(i), i);
        this.addValueIfNotPresent = addValueIfNotPresent;
    }
    public StringFeature(String name, String[] validValuesArr, boolean addValueIfNotPresent){
        this(name, new ArrayList<String>(Arrays.asList(validValuesArr)), addValueIfNotPresent);
    }

    @Override
    public FeatureValue<String> getFeatureValue(InputType inputData) throws Exception {
        String value = getStringValue(inputData);
        if (value == null)
            return new FeatureValue(this, null);
        if(!validValues.contains(value)){
            if(addValueIfNotPresent)
                validValues.add(value);
            else
                throw new InvalidDiscreteValueException("Value "+value+" does not appear in validValues set.");
        }
        return new FeatureValue(this, value);
    }

    public ArrayList<String> getValidValues(){
        return validValues;
    }

    public int getValueIndex(String value){
        return validValuesLookup.get(value);
    }

    protected abstract String getStringValue(InputType inputData) throws Exception;
}