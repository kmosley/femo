package femo.featureset;

import femo.exception.FemoValidationException;
import femo.feature.DoubleFeature;
import femo.feature.EnumFeature;
import femo.feature.Feature;
import femo.feature.StringFeature;
import femo.utils.EnumUtils;

import java.util.ArrayList;

public abstract class FeatureSetUtils {
    public static <DataType> FeatureSet<DataType> expandToDoubleOnly(FeatureSet<DataType> featureSet) throws FemoValidationException {
        ArrayList<DoubleFeature<DataType>> expandedFeatures = new ArrayList<>();
        for(Feature feature : featureSet.getPredictorFeatures()){
            if(feature instanceof DoubleFeature){
                expandedFeatures.add((DoubleFeature)feature);
            } else if(feature instanceof StringFeature){
                ArrayList<String> possibleValues = ((StringFeature)feature).getValidValues();
                for(int i=0; i<possibleValues.size(); i++){
                    expandedFeatures.add(new StringBasedDoubleFeature((StringFeature)feature, possibleValues.get(i)));
                }
            } else if(feature instanceof EnumFeature){
                ArrayList<? extends Enum> possibleValues = ((EnumFeature)feature).getValidValues();
                for(int i=0; i<possibleValues.size(); i++){
                    expandedFeatures.add(new EnumBasedDoubleFeature((EnumFeature)feature, possibleValues.get(i)));
                }
            }
            else {
                throw new FemoValidationException("Not supported feature class: "+feature.getClass());
            }
        }
        return new FeatureSet<>(expandedFeatures);
    }
}
