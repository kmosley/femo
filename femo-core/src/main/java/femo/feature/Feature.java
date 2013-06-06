package femo.feature;

import java.io.Serializable;

/**
 * Defines how to extract a feature value from a data object. *
 *
 * @param <DataType> the class of data object which the features can extract values from
 */
public abstract class Feature <DataType> implements Serializable {
    final protected String name;

    protected Feature(String name) {
        this.name = name;
    }

    public abstract FeatureValue getFeatureValue(DataType dataObject) throws Exception;

    public String getName(){
        return name;
    }

}