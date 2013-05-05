package femo.weka.randomForests;

import femo.feature.StringFeature;
import femo.modeling.InMemTrainingSet;
import femo.modeling.TrainingExample;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.HashMap;

public class ForestEvaluator {

    public static void printResults(RandomForest forest, InMemTrainingSet trainingSet, float resultConfidence) throws Exception {
        ForestHelper.validateTrainingSet(trainingSet);
        System.out.println("oob error: "+forest.measureOutOfBagError());
        ArrayList<TrainingExample> examples = trainingSet.getTrainingExamples();

        ArrayList<Attribute> attributes = ForestBuilder.getPredictorAttributes(trainingSet.getTrainingExamples());
        Attribute classAttribute = new Attribute(
                trainingSet.getResponseFeature().getName(),
                new ArrayList<String>(((StringFeature)trainingSet.getResponseFeature()).getValidValues()));
        attributes.add(classAttribute);

        Instances instances = ForestBuilder.createInstancesObject("TestingInstances", trainingSet, attributes, classAttribute);

        ArrayList<String> classNameLookup = new ArrayList<String>(((StringFeature)trainingSet.getResponseFeature()).getValidValues());

        int numTotal = examples.size();
        int numTotalPredicted = 0;
        int numTotalPredictedCorrectly = 0;
        HashMap<String, Integer> numInClass = new HashMap<String, Integer>();
        HashMap<String, Integer> numPredicted = new HashMap<String, Integer>();
        HashMap<String, Integer> numPredictedCorrectly = new HashMap<String, Integer>();
        for(String s : classNameLookup){
            numInClass.put(s, 0);
            numPredicted.put(s, 0);
            numPredictedCorrectly.put(s, 0);
        }

        // class values in weka are based on the array index of the string values
        for(TrainingExample example : examples){
            Instance instance = ForestBuilder.createInstance(instances, example);

            int classValue = (int)Math.round(instance.classValue());
            String className = classNameLookup.get(classValue);
            numInClass.put(className, numInClass.get(className)+1);

            int predictedClassValue = (int)Math.round(forest.classifyInstance(instance));
            String predictedClassName = classNameLookup.get(predictedClassValue);
            if(forest.distributionForInstance(instance)[predictedClassValue] >= resultConfidence){
                numPredicted.put(predictedClassName, numPredicted.get(predictedClassName)+1);
                numTotalPredicted++;
                if(predictedClassName.equals(className)){
                    numPredictedCorrectly.put(predictedClassName, numPredictedCorrectly.get(predictedClassName)+1);
                    numTotalPredictedCorrectly++;
                }
            }
        }

        System.out.println("numTotal "+numTotal);
        System.out.println("totalPredicted "+numTotalPredicted+" "+numPredicted+" ("+((float)(numTotalPredicted)/numTotal*100)+"% of total)");
        System.out.println("numPredictedCorrectly "+numTotalPredictedCorrectly+" "+numPredictedCorrectly+" ("+((float)numTotalPredictedCorrectly/(numTotalPredicted)*100)+"% of predicted)");

        for(String name : classNameLookup){
            System.out.println("num "+name+" "+numInClass.get(name)+" ("+((float)numInClass.get(name)/numTotal*100)+"% of total)");
            System.out.println("num predicted "+name+" "+numPredicted.get(name)+" ("+((float)numPredicted.get(name)/numTotalPredicted*100)+"% of predicted)");
            System.out.println("num predicted "+name+" correctly "+numPredictedCorrectly.get(name)+" ("+((float)numPredictedCorrectly.get(name)/numPredicted.get(name)*100)+"%)");
        }
    }
}
