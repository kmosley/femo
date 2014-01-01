package femo.feature;

public class FeatureValue <ValueType> {

    final protected Feature feature;
    final protected String name;
    final protected ValueType value;

    public FeatureValue(Feature feature, ValueType value){
        this(feature, feature.getName(), value);
    }

    public FeatureValue(FeatureValue featureValue, ValueType value){
        this(featureValue.feature, featureValue.getName(), value);
    }

    public FeatureValue(Feature feature, String name, ValueType value){
        this.feature = feature;
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return value.equals(value);
    }

    public String getName() {
        return name;
    }

    public ValueType getValue() {
        return value;
    }

    public Feature getFeature() { return feature; }
}
