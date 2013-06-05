package femo.feature;

import femo.utils.EnumUtils;

import java.util.LinkedHashSet;

public abstract class EnumFeature<K, T extends Enum> extends StringFeature<K> {

    Class<T> clazz;

    public EnumFeature(String name, Class<T> clazz){
        super(name, EnumUtils.getEnumValues(clazz), false);
        this.clazz = clazz;
    }

    @Override
    protected String getStringValue(K example) throws Exception {
        T enumVal = getEnumValue(example);
        if (enumVal == null)
            return null;
        return enumVal.toString();
    }

    public abstract T getEnumValue(K example) throws Exception;
}
