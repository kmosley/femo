package femo.modeling;

import femo.exception.InvalidFeatureValueException;
import femo.feature.Feature;
import femo.feature.FeatureSet;
import femo.feature.FeatureValue;
import femo.prediction.Prediction;

import java.util.Iterator;

public interface ModelBuilder <ModelType extends IModel> {

    public <DataType, ResponseDataType, ResponseValueType> IModel<DataType> buildModel(TrainingSet<DataType, ResponseDataType, ResponseValueType> trainingSet) throws Exception;

}
