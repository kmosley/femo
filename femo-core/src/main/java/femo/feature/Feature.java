package femo.feature;

import java.io.Serializable;

/**
 * Defines how to extract a feature value from a data object. *
 *
 * @param <DataType> the class of data object which the features can extract values from
 */
public abstract class Feature <DataType, ValueType> implements Serializable {
    final protected String name;
    final protected Class<ValueType> valueTypeClass;

    protected Feature(String name, Class<ValueType> valueTypeClass) {
        this.name = name;
        this.valueTypeClass = valueTypeClass;
    }

    public abstract FeatureValue<ValueType> getFeatureValue(DataType dataObject) throws Exception;

    public String getName(){
        return name;
    }

    public Class<ValueType> getValueTypeClass() { return valueTypeClass; }
}