package femo.encog.neuralNets;

import femo.exception.ModelConditionsNotMetException;
import femo.feature.FeatureValue;
import femo.modeling.InMemTrainingSet;
import femo.modeling.TrainingExample;

import java.util.ArrayList;

public class NeuralNetHelper {
    public static void validateTrainingSet(InMemTrainingSet trainingSet) throws Exception {
        ArrayList<TrainingExample> examples = trainingSet.getTrainingExamples();
        int predictorFeatureSize = examples.get(0).getPredictorFeatureValues().size();

        // could implement support for string features by pulling them into different indicator features
        // this is not a perfect check but good enough for now
        for(FeatureValue featureValue : examples.get(0).getPredictorFeatureValues()){
            if(featureValue.getValue() != null && !(featureValue.getValue() instanceof Double)){
                throw new ModelConditionsNotMetException("neural nets only work with double feature values");
            }
        }

        for(TrainingExample example : examples){
            if(example.getPredictorFeatureValues().size() != predictorFeatureSize)
                throw new ModelConditionsNotMetException("all training examples must have the same number of predictor features");
        }
    }
}
