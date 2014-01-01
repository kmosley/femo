package femo.feature;

import femo.utils.EnumUtils;

public abstract class EnumFeature<DataType, EnumType extends Enum> extends StringFeature<DataType> {

    Class<EnumType> enumTypeClass;

    public EnumFeature(String name, Class<EnumType> enumTypeClass){
        super(name, EnumUtils.getEnumValues(enumTypeClass), false);
        this.enumTypeClass = enumTypeClass;
    }

    @Override
    protected String getStringValue(DataType example) throws Exception {
        EnumType enumVal = getEnumValue(example);
        if (enumVal == null)
            return null;
        return enumVal.toString();
    }

    public abstract EnumType getEnumValue(DataType example) throws Exception;

    public Class<EnumType> getEnumTypeClass() {
        return enumTypeClass;
    }
}
