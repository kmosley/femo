package femo.feature;

public abstract class DoubleFeature <InputType> extends Feature<InputType> {
    protected DoubleFeature(String name) {
        super(name);
    }

    @Override
    public FeatureValue<Double> getFeatureValue(InputType inputData) throws Exception {
        Double val = getDoubleValue(inputData);
        return new FeatureValue(this, val);
    }

    public abstract Double getDoubleValue(InputType inputData) throws Exception;
}
