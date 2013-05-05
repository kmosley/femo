package femo.weka.randomForests;

import femo.exception.InvalidFeatureValueException;
import femo.feature.StringFeature;
import femo.modeling.InMemTrainingSet;

public abstract class ForestHelper {
    static void validateTrainingSet(InMemTrainingSet trainingSet) throws InvalidFeatureValueException {
        if(!(trainingSet.getResponseFeature() instanceof StringFeature)){
            throw new InvalidFeatureValueException("response feature for trees must be of type string: "+trainingSet.getResponseFeature().getName());
        }
    }
}
