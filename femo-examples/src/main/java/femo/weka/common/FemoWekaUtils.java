package femo.weka.common;

import femo.exception.InvalidFeatureValueException;
import femo.feature.DoubleFeature;
import femo.feature.EnumFeature;
import femo.feature.FeatureValue;
import femo.feature.StringFeature;
import femo.modeling.Example;
import femo.modeling.TrainingExample;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;

public class FemoWekaUtils {

    public static <ResponseValueType> Instances createInstancesObject(String name, List<TrainingExample<ResponseValueType>> trainingExamples,
                                                  ArrayList<Attribute> attributes, Attribute classAttribute) throws Exception {
        Instances instances = new Instances(name, attributes, trainingExamples.size());
        instances.setClass(classAttribute);

        for(TrainingExample example : trainingExamples){
            Instance instance = createInstance(instances, example);
            if(instance != null)
                instances.add(instance);
        }

        return instances;
    }

    public static Instance createInstance(Instances instances, TrainingExample example) throws InvalidFeatureValueException {
        Instance instance = createInstance(instances, (Example)example);
        if(example.responseFeatureValue.getFeature() instanceof DoubleFeature)
            instance.setClassValue((Double)example.responseFeatureValue.getValue());
        else
            instance.setClassValue(example.responseFeatureValue.toString());
        return instance;
    }

    public static Instance createInstance(Instances instances, Example example) throws InvalidFeatureValueException {
        Instance instance = new DenseInstance(instances.numAttributes());
        instance.setDataset(instances);
        for(FeatureValue featureValue : example.getPredictorFeatureValues()){
            if(featureValue.getValue() == null)
                continue;
            Attribute attribute = instances.attribute(featureValue.getName());
            if (featureValue.getFeature() instanceof DoubleFeature)
                instance.setValue(attribute, (Double)featureValue.getValue());
            else if (featureValue.getFeature() instanceof StringFeature)
                instance.setValue(attribute, (String)featureValue.getValue());
            else if (featureValue.getFeature() instanceof EnumFeature)
                instance.setValue(attribute, featureValue.getValue().toString());
            else if(featureValue.getValue() != null)
                System.out.println("Warning: could not assign attribute value for "+featureValue.getName());
        }
        return instance;
    }
}
