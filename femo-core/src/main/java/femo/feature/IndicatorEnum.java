package femo.feature;

public enum IndicatorEnum {
    No,
    Yes;

    public static IndicatorEnum nullableVal(Boolean b){
        return b == null
                ? null
                : b ? Yes : No;
    }
}
