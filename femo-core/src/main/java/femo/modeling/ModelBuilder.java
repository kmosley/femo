package femo.modeling;

import femo.exception.InvalidFeatureValueException;
import femo.feature.Feature;
import femo.feature.FeatureSet;
import femo.feature.FeatureValue;
import femo.prediction.Prediction;

import java.util.Iterator;

public interface ModelBuilder <DataType, ResponseValueType, PredictionType extends Prediction<ResponseValueType>,
        ModelType extends IModel<DataType, ResponseValueType, PredictionType>> {

    public <ResponseDataType> ModelType buildModel(TrainingSet<DataType, ResponseDataType, ResponseValueType> trainingSet) throws Exception;

}
