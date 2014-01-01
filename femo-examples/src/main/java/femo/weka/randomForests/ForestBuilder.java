package femo.weka.randomForests;

import femo.exception.InvalidFeatureValueException;
import femo.feature.FeatureValue;
import femo.feature.StringFeature;
import femo.modeling.*;
import weka.classifiers.trees.RandomForest;
import weka.core.*;
import weka.core.Instance;

import java.util.*;

public class ForestBuilder implements ModelBuilder<ForestModel>{

    protected int randomSeed = new Random().nextInt();
    protected int numTrees = 50;
    protected int numSelectionAttributes = 10;
    protected int numThreadsToUse = 4;

    @Override
    public <DataType, ResponseDataType> ForestModel<DataType> buildModel(TrainingSet<DataType, ResponseDataType> trainingSet) throws Exception {

        if(!(trainingSet.getResponseFeature() instanceof StringFeature)){
            throw new InvalidFeatureValueException("response feature for trees must return string values: "+trainingSet.getResponseFeature().getName());
        }

        List<TrainingExample> trainingExamples = trainingSet.generateAllExamples(ExampleDensity.Sparse);

        ArrayList<Attribute> attributes = getPredictorAttributes(trainingExamples);
        Attribute classAttribute = new Attribute(
                trainingSet.getResponseFeature().getName(),
                ((StringFeature)trainingSet.getResponseFeature()).getValidValues());
        attributes.add(classAttribute);

        Instances trainingInstances = createInstancesObject("TrainingInstances", trainingExamples, attributes, classAttribute);

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

        ForestModel<DataType> model = new ForestModel<DataType>(trainingSet.getFeatureSet(), forest, attributes, classAttribute, new ArrayList<String>(((StringFeature)trainingSet.getResponseFeature()).getValidValues()));

        return model;
    }

    protected static Instances createInstancesObject(String name, List<TrainingExample> trainingExamples, ArrayList<Attribute> attributes, Attribute classAttribute) throws Exception {
        Instances instances = new Instances(name, attributes, trainingExamples.size());
        instances.setClass(classAttribute);

        for(TrainingExample example : trainingExamples){
            Instance instance = createInstance(instances, example);
            if(instance != null)
                instances.add(instance);
        }

        return instances;
    }

    public static Instance createInstance(Instances instances, Example example) throws InvalidFeatureValueException {
        Instance instance = new DenseInstance(instances.numAttributes());
        instance.setDataset(instances);
        for(FeatureValue featureValue : example.getPredictorFeatureValues()){
            if(featureValue.getValue() == null)
                continue;
            Attribute attribute = instances.attribute(featureValue.getName());
            if (featureValue.getValue() instanceof Double)
                instance.setValue(attribute, (Double)featureValue.getValue());
            else if (featureValue.getFeature() instanceof StringFeature)
                instance.setValue(attribute, (String)featureValue.getValue());
            else if(featureValue.getValue() != null)
                System.out.println("Warning: could not assign attribute value for "+featureValue.getName());
        }
        return instance;
    }

    protected static Instance createInstance(Instances instances, TrainingExample example) throws InvalidFeatureValueException {
        Instance instance = createInstance(instances, (Example)example);
        // string type already validated
        instance.setClassValue((String) example.responseFeatureValue.getValue());
        return instance;
    }

    protected static ArrayList<Attribute> getPredictorAttributes(List<TrainingExample> examples){
        // initialize with first example's size even though that's not guaranteed to be max
        // it is guaranteed to be max if we are returning null feature values for all missing features
        ArrayList<Attribute> attributes = new ArrayList<Attribute>(examples.get(0).getPredictorFeatureValues().size());
        HashSet<String> seenFeatureNames = new HashSet<String>();

        for(TrainingExample example : examples){
            for(FeatureValue featureValue : example.getPredictorFeatureValues()){
                if(!seenFeatureNames.contains(featureValue.getName())){
                    Attribute attribute;
                    if(featureValue.getFeature() instanceof StringFeature)
                        attribute = new Attribute(featureValue.getName(), ((StringFeature)featureValue.getFeature()).getValidValues());
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
