package femo.utils;

import java.util.ArrayList;

public abstract class EnumUtils {
    public static <T extends Enum<T>> ArrayList<String> getEnumValues(Class<T> enumType) {
        ArrayList names = new ArrayList<String>();
        for (T c : enumType.getEnumConstants()) {
            names.add(c.name());
        }
        return names;
    }
}
