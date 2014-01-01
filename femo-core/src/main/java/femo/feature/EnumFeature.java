package femo.feature;

import femo.utils.EnumUtils;

public abstract class EnumFeature<DataType, EnumType extends Enum> extends StringFeature<DataType> {

    Class<EnumType> clazz;

    public EnumFeature(String name, Class<EnumType> clazz){
        super(name, EnumUtils.getEnumValues(clazz), false);
        this.clazz = clazz;
    }

    @Override
    protected String getStringValue(DataType example) throws Exception {
        EnumType enumVal = getEnumValue(example);
        if (enumVal == null)
            return null;
        return enumVal.toString();
    }

    public abstract EnumType getEnumValue(DataType example) throws Exception;
}
