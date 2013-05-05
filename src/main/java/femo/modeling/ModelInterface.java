package femo.modeling;

import femo.prediction.Prediction;

public interface ModelInterface<DataType> {
    public Prediction getPrediction(DataType dataObj) throws Exception;
}
