package femo.modeling;

import femo.prediction.Prediction;

public interface IModel<DataType, ResponseValueType, PredictionType extends Prediction<ResponseValueType>> {
    public PredictionType getPrediction(DataType dataObj) throws Exception;
}
