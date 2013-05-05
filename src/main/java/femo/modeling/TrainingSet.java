package femo.modeling;

import femo.feature.Feature;
import femo.feature.FeatureSet;

import java.util.Iterator;

public abstract class TrainingSet <FeatureSetInput, ResponseFeatureInput> {
    protected final FeatureSet<FeatureSetInput> predictorFeatures;
    protected final Feature<ResponseFeatureInput> responseFeature;

    protected TrainingSet(FeatureSet predictorFeatures, Feature responseFeature) {
        this.predictorFeatures = predictorFeatures;
        this.responseFeature = responseFeature;
    }

    public abstract Iterator<TrainingExample> exampleIterator() throws Exception;

    public FeatureSet<FeatureSetInput> getPredictorFeatures() {
        return predictorFeatures;
    }

    public Feature<ResponseFeatureInput> getResponseFeature() {
        return responseFeature;
    }
}
