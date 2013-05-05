package femo.feature;

import femo.exception.InvalidDiscreteValueException;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class StringFeature <InputType> extends Feature<InputType> {

    LinkedHashSet<String> validValues;
    boolean addValueIfNotPresent;

    public StringFeature(String name){
        this(name, new LinkedHashSet<String>(), true);
    }
    public StringFeature(String name, LinkedHashSet<String> validValues, boolean addValueIfNotPresent){
        super(name);
        this.validValues = validValues;
        this.addValueIfNotPresent = addValueIfNotPresent;
    }
    public StringFeature(String name, String[] validValuesArr, boolean addValueIfNotPresent){
        super(name);
        this.validValues = new LinkedHashSet<String>(Arrays.asList(validValuesArr));
        this.addValueIfNotPresent = addValueIfNotPresent;
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

    public Set<String> getValidValues(){
        return validValues;
    }

    protected abstract String getStringValue(InputType inputData) throws Exception;
}