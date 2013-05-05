package femo.feature;

import java.io.Serializable;

//TODO: look into making abstract get as string and get as double functions
public abstract class Feature <InputType> implements Serializable {
    final protected String name;

    protected Feature(String name) {
        this.name = name;
    }

    public abstract FeatureValue getFeatureValue(InputType inputData) throws Exception;

    public String getName(){
        return name;
    }

}