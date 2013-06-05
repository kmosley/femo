package femo.examples;

import femo.encog.neuralNets.NeuralNetBuilder;
import femo.encog.neuralNets.NeuralNetClassificationModel;
import femo.feature.*;
import femo.modeling.Example;
import femo.modeling.ExampleDensity;
import femo.modeling.InMemTrainingSet;
import femo.modeling.Model;
import femo.prediction.Prediction;
import femo.utils.ListUtils;
import femo.weka.randomForests.ForestBuilder;
import femo.weka.randomForests.ForestModel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class IrisExample {

    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(IrisExample.class.getResourceAsStream("iris.data")));
        ArrayList<IrisData> irisData = new ArrayList<IrisData>();
        while(br.ready()){
            irisData.add(new IrisData(br.readLine()));
        }

        ArrayList<ArrayList<IrisData>> datasets = ListUtils.splitRandomly(irisData, new double[]{.9, .1}, 1);
        ArrayList<IrisData> train = datasets.get(0);
        ArrayList<IrisData> test = datasets.get(1);

        FeatureSetBuilder<IrisData> featureSetBuilder = new FeatureSetBuilder<IrisData>()
                .addFeatures(IrisFeatures.irisLengths);
        FeatureSet<IrisData> denseFeatureSet = featureSetBuilder.setExampleDensity(ExampleDensity.Dense).build();
        FeatureSet<IrisData> sparseFeatureSet = featureSetBuilder.setExampleDensity(ExampleDensity.Sparse).build();

        InMemTrainingSet<IrisData,IrisData> trainingSet = new InMemTrainingSet<IrisData,IrisData>(sparseFeatureSet, IrisFeatures.irisType, train, train);

        //TODO: get rid of training set idea, builder takes a featureset, DataType example iterator, and response iterator
        // builder is then in charge of dictating example density

        ForestModel<IrisData> forestModel = new ForestBuilder().buildModel(trainingSet);
        testPredictions(forestModel, test, IrisFeatures.irisType);
//        NeuralNetClassificationModel<IrisData> neuralNetModel = new NeuralNetBuilder().buildModel(trainingSet);
//        testPredictions(neuralNetModel, test, responseFeature);

    }

    static void testPredictions(Model<IrisData, ? extends Prediction<String>> model, List<IrisData> test, StringFeature<IrisData> responseFeature) throws Exception {
        System.out.println("Testing: "+model.getClass());
        int wrong=0;
        int correct=0;
        for(IrisData testData : test){
            if(model.getPrediction(testData).getValue().equals(responseFeature.getFeatureValue(testData).getValue()))
                correct++;
            else
                wrong++;
        }
        System.out.println("correct = "+correct+" ("+(100*correct/((double)correct+wrong))+"%)");

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("irisModel.model"));
        oos.writeObject(model);
        oos.close();
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("irisModel.model"));
        Model<IrisData, ? extends Prediction<String>> serializedModel = (Model<IrisData, ? extends Prediction<String>>)ois.readObject();
        ois.close();
        wrong=0;
        correct=0;
        for(IrisData testData : test){
            if(serializedModel.getPrediction(testData).getValue().equals(responseFeature.getFeatureValue(testData).getValue()))
                correct++;
            else
                wrong++;
        }
        System.out.println("correct = "+correct+" ("+(100*correct/((double)correct+wrong))+"%)");
    }

    static class IrisFeatures{
        public static final ArrayList<DoubleFeature<IrisData>> irisLengths;
        public static final StringFeature<IrisData> irisType;

        static{
            irisLengths = new ArrayList<DoubleFeature<IrisData>>();
            for(int i=0; i<4; i++){
                final int index = i;
                irisLengths.add(new DoubleFeature<IrisData>(String.valueOf(index)) {
                    @Override
                    public Double getDoubleValue(IrisData inputData) throws Exception {
                        return inputData.measurements[index];
                    }
                });
            }

            irisType = new StringFeature<IrisData>("IrisType", new String[]{"Iris-setosa", "Iris-versicolor", "Iris-virginica"}, false) {
                @Override
                protected String getStringValue(IrisData inputData) throws Exception {
                    return inputData.type;
                }
            };
        }
    }

    static class IrisData{
        double[] measurements;
        String type;
        IrisData(String inputLine){
            String[] data = inputLine.split(",");
            measurements = new double[4];
            for (int i = 0; i < measurements.length; i++) {
                measurements[i] = Double.parseDouble(data[i]);
            }
            type = data[4];
        }
    }
}