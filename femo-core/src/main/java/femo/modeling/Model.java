package femo.modeling;

import femo.featureset.FeatureSet;
import femo.prediction.Prediction;

import java.io.Serializable;

/**
 * Abstract class to hold trained models. Should be serialized with all necessary objects to have consistent
 * results at training time and prediction time. The stored FeatureSet does however require that the model is
 * run with access to the lib containing the specific Features.
 *
 * @param <DataType> the class of data object which the features can extract values from
 * @param <PredictionType> type of prediction this model should produce
 */
public abstract class Model <DataType, ResponseValueType, PredictionType extends Prediction<ResponseValueType>>
        implements IModel<DataType, ResponseValueType, PredictionType>, Serializable {
    public FeatureSet<DataType> featureSet;

    protected Model(FeatureSet<DataType> featureSet){
        this.featureSet = featureSet;
    }
}
