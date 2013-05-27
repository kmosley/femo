package femo.modeling;

import femo.exception.TrainingSetConditionsNotMet;
import femo.feature.Feature;
import femo.feature.FeatureSet;
import femo.utils.ListUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InMemTrainingSet<FeatureSetInput, ResponseFeatureInput> extends TrainingSet<FeatureSetInput, ResponseFeatureInput> {

    protected ArrayList<TrainingExample> trainingExamples;

    public InMemTrainingSet(FeatureSet<FeatureSetInput> predictorFeatures, Feature<ResponseFeatureInput> responseFeature, List<FeatureSetInput> instanceData, List<ResponseFeatureInput> responseData) throws Exception {
        super(predictorFeatures, responseFeature);
        if(instanceData.size() != responseData.size())
            throw new TrainingSetConditionsNotMet("instanceData and response data lists are not of equal size");

        trainingExamples = new ArrayList<TrainingExample>(instanceData.size());
        for(int i=0; i<instanceData.size(); i++){
            trainingExamples.add(new TrainingExample(predictorFeatures.getExample(instanceData.get(i)), responseFeature.getFeatureValue(responseData.get(i))));
        }
    }
    public InMemTrainingSet(FeatureSet<FeatureSetInput> predictorFeatures, Feature<ResponseFeatureInput> responseFeature, ArrayList<TrainingExample> trainingExamples) throws Exception {
        super(predictorFeatures, responseFeature);
        this.trainingExamples = trainingExamples;
    }

    @Override
    public Iterator<TrainingExample> exampleIterator() throws Exception {
        return trainingExamples.iterator();
    }

    public ArrayList<TrainingExample> getTrainingExamples() {
        return trainingExamples;
    }
}
