package femo.modeling;

import femo.feature.Feature;
import femo.feature.FeatureSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TrainingSet <DataType, ResponseDataType> {
    protected final FeatureSet<DataType> featureSet;
    protected Iterator<DataType> dataObjectsIterator;
    protected final Feature<ResponseDataType> responseFeature;
    protected Iterator<ResponseDataType> responseIterator;

    public TrainingSet(FeatureSet<DataType> featureSet,
                        Iterator<DataType> dataObjectsIterator,
                        Feature<ResponseDataType> responseFeature,
                        Iterator<ResponseDataType> responseIterator){
        this.featureSet = featureSet;
        this.dataObjectsIterator = dataObjectsIterator;
        this.responseFeature = responseFeature;
        this.responseIterator = responseIterator;
    }

    public TrainingExample generateNextTrainingExample(ExampleDensity density) throws Exception {
        if(dataObjectsIterator.hasNext()){
            return new TrainingExample(featureSet.getExample(dataObjectsIterator.next(), density), responseFeature.getFeatureValue(responseIterator.next()));
        }
        return null;
    }

    public List<TrainingExample> generateAllExamples(ExampleDensity density) throws Exception {
        List<TrainingExample> examples = new ArrayList<TrainingExample>();
        TrainingExample example = generateNextTrainingExample(density);
        while(example != null){
            examples.add(example);
            example = generateNextTrainingExample(density);
        }
        return examples;
    }

    public void resetIterators(Iterator<DataType> dataObjectsIterator, Iterator<ResponseDataType> responseIterator){
        this.dataObjectsIterator = dataObjectsIterator;
        this.responseIterator = responseIterator;
    }

    public FeatureSet<DataType> getFeatureSet() {
        return featureSet;
    }

    public Iterator<DataType> getDataObjectsIterator() {
        return dataObjectsIterator;
    }

    public Feature<ResponseDataType> getResponseFeature() {
        return responseFeature;
    }

    public Iterator<ResponseDataType> getResponseIterator() {
        return responseIterator;
    }
}
