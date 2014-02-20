package femo.featureset;

import femo.feature.DoubleFeature;
import femo.feature.StringFeature;

public class StringBasedDoubleFeature <InputType> extends DoubleFeature<InputType> {

    final StringFeature<InputType> baseFeature;
    final String indicatorValue;

    public StringBasedDoubleFeature(StringFeature<InputType> baseFeature, String indicatorValue){
        super(baseFeature.getName()+"_"+indicatorValue);
        this.baseFeature = baseFeature;
        this.indicatorValue = indicatorValue;
    }

    @Override
    public Double getDoubleValue(InputType inputData) throws Exception {
        String value = baseFeature.getFeatureValue(inputData).getValue();
        if(value.equals(indicatorValue))
            return 1d;
        return 0d;
    }
}
