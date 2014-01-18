package femo.weka.randomForests;

import femo.prediction.Prediction;

public class ForestPrediction<PredictionType> extends Prediction<PredictionType> {

    protected ForestPrediction(PredictionType predictionType) {
        super(predictionType);
    }
}
