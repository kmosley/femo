package femo.feature;

public abstract class DoubleFeature <InputType> extends Feature<InputType> {
    protected DoubleFeature(String name) {
        super(name);
    }

    @Override
    public FeatureValue<Double> getFeatureValue(InputType dataObject) throws Exception {
        Double val = getDoubleValue(dataObject);
        return new FeatureValue(this, val);
    }

    public abstract Double getDoubleValue(InputType inputData) throws Exception;
}
