package femo.modeling;

import femo.feature.FeatureSet;
import femo.prediction.Prediction;

import java.io.Serializable;

public abstract class Model <DataType, PredictionType extends Prediction> implements IModel<DataType>, Serializable {
    public FeatureSet<DataType> featureSet;

    protected Model(FeatureSet<DataType> featureSet){
        this.featureSet = featureSet;
    }

    @Override
    public PredictionType getPrediction(DataType dataObj) throws Exception {
        return getPrediction(featureSet.getExample(dataObj));
    }

    protected abstract PredictionType getPrediction(Example example) throws Exception;
}
