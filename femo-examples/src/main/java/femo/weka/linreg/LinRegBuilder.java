package femo.weka.linreg;

import femo.exception.FemoValidationException;
import femo.exception.InvalidFeatureException;
import femo.exception.InvalidFeatureValueException;
import femo.feature.*;
import femo.modeling.*;
import femo.utils.EnumUtils;
import femo.weka.common.FemoWekaUtils;
import weka.classifiers.functions.LinearRegression;
import weka.core.*;

import java.util.*;

public class LinRegBuilder<DataType>
        implements ModelBuilder<DataType, Double, LinRegPrediction, LinRegModel<DataType>>{

    protected boolean showDebugOutput = false;

    @Override
    public <ResponseDataType> LinRegModel<DataType> buildModel(
            TrainingSet<DataType, ResponseDataType, Double> trainingSet) throws Exception {

        validateTrainingSet(trainingSet);

        List<TrainingExample<Double>> trainingExamples = trainingSet.generateAllExamples(ExampleDensity.Sparse);

        ArrayList<Attribute> attributes = getPredictorAttributes(trainingExamples);
        Attribute classAttribute = new Attribute(trainingSet.getResponseFeature().getName());
        attributes.add(classAttribute);

        Instances trainingInstances = FemoWekaUtils.createInstancesObject("TrainingInstances", trainingExamples, attributes, classAttribute);

        // configure model
        LinearRegression classifier = new LinearRegression();
        classifier.setOptions(new String[]{
                showDebugOutput ? "-D" : "", // debugging output
        });

        classifier.buildClassifier(trainingInstances);

        LinRegModel<DataType> model = new LinRegModel<>(trainingSet.getFeatureSet(), classifier, attributes, classAttribute);

        return model;
    }

    protected <DataType, ResponseDataType, Double> void validateTrainingSet(
            TrainingSet<DataType, ResponseDataType, Double> trainingSet) throws FemoValidationException{

        if(!(trainingSet.getResponseFeature() instanceof DoubleFeature)){
            throw new InvalidFeatureValueException("response feature for linear regression must return a double: "
                    +trainingSet.getResponseFeature().getName());
        }

        for(Feature feature : trainingSet.getFeatureSet().getPredictorFeatures()){
            if(!(feature instanceof DoubleFeature)
                    && !(feature instanceof StringFeature)
                    && !(feature instanceof EnumFeature))
                throw new InvalidFeatureException("feature must be a Double, String, or Enum feature");
        }
    }

    protected static ArrayList<Attribute> getPredictorAttributes(List<TrainingExample<Double>> examples){
        // initialize with first example's size even though that's not guaranteed to be max
        // it is guaranteed to be max if we are returning null feature values for all missing features
        // this is just an allocation optimization
        ArrayList<Attribute> attributes = new ArrayList<Attribute>(examples.get(0).getPredictorFeatureValues().size());
        HashSet<String> seenFeatureNames = new HashSet<String>();

        for(TrainingExample example : examples){
            for(FeatureValue featureValue : example.getPredictorFeatureValues()){
                // IMPLEMENTATION 1: MANUALLY BREAK INTO INDICATOR ATTRIBUTES
                // NEED TO MAKE SPECIFIC UTILS FUNCTIONS FOR ALTERING HOW FEATUREVALUE MAPS TO ATTRIBUTE
//                String attributeName;
//                if(featureValue.getFeature() instanceof StringFeature
//                        || featureValue.getFeature() instanceof EnumFeature)
//                    attributeName = featureValue.getName()+"."+featureValue.getValue().toString();
//                else
//                    attributeName = featureValue.getName();
//
//                if(!seenFeatureNames.contains(attributeName)){
//                    Attribute attribute = new Attribute(featureValue.getName());
//                    attributes.add(attribute);
//                    seenFeatureNames.add(featureValue.getName());
//                }
                // IMPLEMENTATION 2: ALLOW STRING ATTRIBUTES TO SEE IF WEKA MAKES THEM INDICATORS FOR YOU
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

    public LinRegBuilder setShowDebugOutput(boolean showDebugOutput) {
        this.showDebugOutput = showDebugOutput;
        return this;
    }
}
