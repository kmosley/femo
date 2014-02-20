package femo.weka.randomForests;

import femo.exception.FemoValidationException;
import femo.exception.InvalidFeatureException;
import femo.exception.InvalidFeatureValueException;
import femo.feature.*;
import femo.modeling.*;
import femo.prediction.Prediction;
import femo.utils.EnumUtils;
import femo.weka.common.FemoWekaUtils;
import weka.classifiers.trees.RandomForest;
import weka.core.*;
import weka.core.Instance;

import java.util.*;

public class ForestBuilder<DataType, ResponseValueType>
        implements ModelBuilder<DataType, ResponseValueType, ForestPrediction<ResponseValueType>, ForestModel<DataType, ResponseValueType>>{

    protected int randomSeed = new Random().nextInt();
    protected int numTrees = 50;
    protected int numSelectionAttributes = 10;
    protected int numThreadsToUse = 4;

    @Override
    public <ResponseDataType> ForestModel<DataType, ResponseValueType> buildModel(
            TrainingSet<DataType, ResponseDataType, ResponseValueType> trainingSet) throws Exception {

        validateTrainingSet(trainingSet);

        List<TrainingExample<ResponseValueType>> trainingExamples = trainingSet.generateAllExamples(ExampleDensity.Sparse);

        ArrayList<Attribute> attributes = getPredictorAttributes(trainingExamples);
        Attribute classAttribute = new Attribute(
                trainingSet.getResponseFeature().getName(),
                trainingSet.getResponseFeature() instanceof StringFeature
                        ? ((StringFeature)trainingSet.getResponseFeature()).getValidValues()
                        : EnumUtils.getEnumValues(((EnumFeature)trainingSet.getResponseFeature()).getValueTypeClass()));
        attributes.add(classAttribute);

        Instances trainingInstances = FemoWekaUtils.createInstancesObject("TrainingInstances", trainingExamples, attributes, classAttribute);

        // configure forest
        RandomForest forest = new RandomForest();
        forest.setOptions(new String[]{
                "-D",
                "-I", String.valueOf(numTrees),
                "-K", String.valueOf(numSelectionAttributes),
                "-num-slots", String.valueOf(numThreadsToUse),
                "-S", String.valueOf(randomSeed)
        });

        forest.buildClassifier(trainingInstances);

        ForestModel<DataType, ResponseValueType> model;
        if(trainingSet.getResponseFeature() instanceof StringFeature)
            model = new ForestModel<>(trainingSet.getFeatureSet(), forest, attributes, classAttribute, new ArrayList<ResponseValueType>(((StringFeature)trainingSet.getResponseFeature()).getValidValues()));
        else
            model = new ForestModel<DataType, ResponseValueType>(trainingSet.getFeatureSet(), forest, attributes, classAttribute, ((EnumFeature)trainingSet.getResponseFeature()).getValidValues());

        return model;
    }

    protected <DataType, ResponseDataType, ResponseValueType> void validateTrainingSet(
            TrainingSet<DataType, ResponseDataType, ResponseValueType> trainingSet) throws FemoValidationException{

        if(!(trainingSet.getResponseFeature() instanceof StringFeature)
                && !(trainingSet.getResponseFeature() instanceof EnumFeature)){
            throw new InvalidFeatureValueException("response feature for trees must return string or enum values: "+trainingSet.getResponseFeature().getName());
        }

        for(Feature feature : trainingSet.getFeatureSet().getPredictorFeatures()){
            if(!(feature instanceof DoubleFeature)
                    && !(feature instanceof StringFeature)
                    && !(feature instanceof EnumFeature))
                throw new InvalidFeatureException("feature must be a Double, String, or Enum feature");
        }
    }

    protected static <ResponseValueType> ArrayList<Attribute> getPredictorAttributes(List<TrainingExample<ResponseValueType>> examples){
        // initialize with first example's size even though that's not guaranteed to be max
        // it is guaranteed to be max if we are returning null feature values for all missing features
        // this is just an allocation optimization
        ArrayList<Attribute> attributes = new ArrayList<Attribute>(examples.get(0).getPredictorFeatureValues().size());
        HashSet<String> seenFeatureNames = new HashSet<String>();

        for(TrainingExample example : examples){
            for(FeatureValue featureValue : example.getPredictorFeatureValues()){
                if(!seenFeatureNames.contains(featureValue.getName())){
                    Attribute attribute;
                    if(featureValue.getFeature() instanceof StringFeature)
                        attribute = new Attribute(featureValue.getName(), ((StringFeature)featureValue.getFeature()).getValidValues());
                    else if(featureValue.getFeature() instanceof EnumFeature)
                        attribute = new Attribute(featureValue.getName(),
                                EnumUtils.getEnumValues(featureValue.getFeature().getValueTypeClass()));
                    else
                        attribute = new Attribute(featureValue.getName());
                    attributes.add(attribute);
                    seenFeatureNames.add(featureValue.getName());
                }
            }
        }

        return attributes;
    }

    public ForestBuilder setRandomSeed(int randomSeed) {
        this.randomSeed = randomSeed;
        return this;
    }

    public ForestBuilder setNumTrees(int numTrees) {
        this.numTrees = numTrees;
        return this;
    }

    public ForestBuilder setNumSelectionAttributes(int numSelectionAttributes) {
        this.numSelectionAttributes = numSelectionAttributes;
        return this;
    }

    public ForestBuilder setNumThreadsToUse(int numThreadsToUse) {
        this.numThreadsToUse = numThreadsToUse;
        return this;
    }
}
