package femo.feature;

import femo.utils.EnumUtils;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class EnumFeature<DataType, EnumType extends Enum> extends Feature<DataType, EnumType> {

    public EnumFeature(String name, Class<EnumType> enumTypeClass){
        super(name, enumTypeClass);
    }

    @Override
    public FeatureValue<EnumType> getFeatureValue(DataType dataObject) throws Exception {
        EnumType value = getEnumValue(dataObject);
        return new FeatureValue<>(this, value);
    }

    public abstract EnumType getEnumValue(DataType example) throws Exception;

    public ArrayList<EnumType> getValidValues() { return new ArrayList<>(Arrays.asList(valueTypeClass.getEnumConstants())); }
}
