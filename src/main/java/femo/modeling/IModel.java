package femo.modeling;

import femo.prediction.Prediction;

public interface IModel<DataType> {
    public Prediction getPrediction(DataType dataObj) throws Exception;
}
