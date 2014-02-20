package femo.featureset;

import femo.feature.DoubleFeature;
import femo.feature.EnumFeature;

public class EnumBasedDoubleFeature <InputType, EnumType extends Enum> extends DoubleFeature<InputType> {

    final EnumFeature<InputType, EnumType> baseFeature;
    final EnumType indicatorValue;

    public EnumBasedDoubleFeature(EnumFeature<InputType, EnumType> baseFeature, EnumType indicatorValue){
        super(baseFeature.getName()+"_"+indicatorValue);
        this.baseFeature = baseFeature;
        this.indicatorValue = indicatorValue;
    }

    @Override
    public Double getDoubleValue(InputType inputData) throws Exception {
        EnumType value = baseFeature.getFeatureValue(inputData).getValue();
        if(value.equals(indicatorValue))
            return 1d;
        return 0d;
    }
}
