package femo.weka.randomForests;

import femo.exception.InvalidFeatureValueException;
import femo.feature.FeatureValue;
import femo.feature.StringFeature;
import femo.modeling.*;
import weka.classifiers.trees.RandomForest;
import weka.core.*;
import weka.core.Instance;

import java.util.*;

public class ForestBuilder {

    public int randomSeed = new Random().nextInt();
    public int numTrees = 50;
    public int numSelectionAttributes = 10;
    public int numThreadsToUse = 4;

    // the ResponseType generic doesn't need to be there but I'm not sure how to get the DataType from the training set without it
    // include response feature so we can map the forest outputs to the correct string values
    public <DataType, ResponseType> ForestModel<DataType> buildModel(InMemTrainingSet<DataType, ResponseType> trainingSet) throws Exception {
        ForestHelper.validateTrainingSet(trainingSet);

        ArrayList<Attribute> attributes = getPredictorAttributes(trainingSet.getTrainingExamples());
        Attribute classAttribute = new Attribute(
                trainingSet.getResponseFeature().getName(),
                ((StringFeature)trainingSet.getResponseFeature()).getValidValues());
        attributes.add(classAttribute);

        Instances trainingInstances = createInstancesObject("TrainingInstances", trainingSet, attributes, classAttribute);

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

        ForestModel<DataType> model = new ForestModel<DataType>(trainingSet.getPredictorFeatures(), forest, attributes, classAttribute, new ArrayList<String>(((StringFeature)trainingSet.getResponseFeature()).getValidValues()));

        return model;
    }

    protected static Instances createInstancesObject(String name, InMemTrainingSet trainingSet, ArrayList<Attribute> attributes, Attribute classAttribute) throws Exception {
        ArrayList<TrainingExample> examples = trainingSet.getTrainingExamples();

        Instances instances = new Instances(name, attributes, examples.size());
        instances.setClass(classAttribute);

        for(TrainingExample example : examples){
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
            else if (featureValue.getValue() instanceof String)
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

    protected static ArrayList<Attribute> getPredictorAttributes(ArrayList<TrainingExample> examples){
        // initialize with first example's size even though that's not guaranteed to be max
        // it is guaranteed to be max if we are returning null feature values for all missing features
        ArrayList<Attribute> attributes = new ArrayList<Attribute>(examples.get(0).getPredictorFeatureValues().size());
        HashSet<String> seenFeatureNames = new HashSet<String>();

        for(TrainingExample example : examples){
            for(FeatureValue featureValue : example.getPredictorFeatureValues()){
                if(!seenFeatureNames.contains(featureValue.getName())){
                    Attribute attribute = new Attribute(featureValue.getName());
                    attributes.add(attribute);
                    seenFeatureNames.add(featureValue.getName());
                }
            }
        }

        return attributes;
    }
}
