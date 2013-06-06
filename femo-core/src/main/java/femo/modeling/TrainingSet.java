package femo.modeling;

import femo.feature.Feature;
import femo.feature.FeatureSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A convenience class for the model builders which holds the predictor and response features as well as iterators
 * for the data objects. Iterators can be used to stream examples from large files or just load all examples into
 * memory.
 *
 * @param <DataType> the class of data object which the features can extract values from
 * @param <ResponseDataType> the class of data object for the response feature to extract it's value from
 */
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
