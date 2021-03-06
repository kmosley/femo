package femo.examples;

import femo.encog.neuralNets.NeuralNetBuilder;
import femo.encog.neuralNets.NeuralNetClassificationModel;
import femo.encog.neuralNets.NeuralNetHelper;
import femo.exception.InvalidInputException;
import femo.feature.*;
import femo.featureset.FeatureSet;
import femo.liblinear.logreg.LogRegBuilder;
import femo.liblinear.logreg.LogRegModel;
import femo.modeling.Model;
import femo.modeling.TrainingSet;
import femo.prediction.Prediction;
import femo.utils.ListUtils;
import femo.weka.linreg.LinRegBuilder;
import femo.weka.linreg.LinRegModel;
import femo.weka.randomForests.ForestBuilder;
import femo.weka.randomForests.ForestModel;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Example application which shows how to:
 * create Features for your data object
 * create a FeatureSet from those Features
 * create a TrainingSet for holding example data and the target value
 * use model builders to create a weka random forest and an encog neural network using the same Features
 * serialize models then deserialize and check the performance is the same
 */
public class IrisExample {

    public static void main(String[] args) throws Exception {

        // Setup data objects and split into a training set and test set
        BufferedReader br = new BufferedReader(new InputStreamReader(IrisExample.class.getResourceAsStream("iris.data")));
        ArrayList<IrisData> irisData = new ArrayList<IrisData>();
        while(br.ready()){
            irisData.add(new IrisData(br.readLine()));
        }

        ArrayList<ArrayList<IrisData>> datasets = ListUtils.splitRandomly(irisData, new double[]{.9, .1}, 1);
        ArrayList<IrisData> train = datasets.get(0);
        ArrayList<IrisData> test = datasets.get(1);

        FeatureSet<IrisData> featureSet = new FeatureSet<IrisData>(IrisFeatures.irisLengths);

        // Create a WEKA random forest model with a String based response feature
        TrainingSet<IrisData,IrisData,String> trainingSetString = new TrainingSet<IrisData,IrisData,String>(featureSet, train.iterator(), IrisFeatures.irisTypeString, train.iterator());
        ForestModel<IrisData, String> forestModelString = new ForestBuilder()
                .setNumSelectionAttributes(2)
                .setNumTrees(100)
                .buildModel(trainingSetString);
        testDiscretePredictions(forestModelString, test, IrisFeatures.irisTypeString);

        // Create a WEKA random forest model with an Enum based response feature
        TrainingSet<IrisData,IrisData,IrisType> trainingSetEnum = new TrainingSet<IrisData,IrisData,IrisType>(featureSet, train.iterator(), IrisFeatures.irisTypeEnum, train.iterator());
        ForestModel<IrisData, IrisType> forestModelEnum = new ForestBuilder()
                .setNumSelectionAttributes(2)
                .setNumTrees(100)
                .buildModel(trainingSetEnum);
        testDiscretePredictions(forestModelEnum, test, IrisFeatures.irisTypeEnum);

        // Create a WEKA linear regression model
        TrainingSet<IrisData,IrisData,Double> linRegTrainingSet = new TrainingSet<>(featureSet, train.iterator(), IrisFeatures.setosaScore,
                train.iterator());
        LinRegModel<IrisData> linRegModel = new LinRegBuilder()
                .buildModel(linRegTrainingSet);
        testRegressionPredictions(linRegModel, test, IrisFeatures.setosaScore);

        // Create a LibLinear logistic regression model
        TrainingSet<IrisData,IrisData,Double> logRegTrainingSet = new TrainingSet<>(featureSet, train.iterator(), IrisFeatures.setosaScore,
                train.iterator());
        LogRegModel<IrisData> logRegModel = new LogRegBuilder()
                .buildModel(logRegTrainingSet);
        testDiscretePredictions(logRegModel, test, IrisFeatures.setosaScore);

        // Create an Encog neural network model
        NeuralNetBuilder builder = new NeuralNetBuilder()
                .setHiddenLayerCounts(new int[]{})
                .setRandomSeed(1)
                .setSecondsToTrain(2);
        // normalizing inputs to neural network helps it train faster
        ArrayList<DoubleFeature<IrisData>> normalizedFeatures
                = NeuralNetHelper.normalizeFeatures(IrisFeatures.irisLengths, builder.getActivationFunction(), train.iterator());
        featureSet = new FeatureSet<IrisData>(normalizedFeatures);
        trainingSetString = new TrainingSet<IrisData,IrisData,String>(featureSet, train.iterator(), IrisFeatures.irisTypeString, train.iterator());

        NeuralNetClassificationModel<IrisData> neuralNetModel = builder.buildModel(trainingSetString);
        testDiscretePredictions(neuralNetModel, test, IrisFeatures.irisTypeString);

    }

    static class IrisFeatures{
        public static final ArrayList<DoubleFeature<IrisData>> irisLengths;
        public static final StringFeature<IrisData> irisTypeString;
        public static final EnumFeature<IrisData, IrisType> irisTypeEnum;
        public static final DoubleFeature<IrisData> setosaScore;

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

            irisTypeString = new StringFeature<IrisData>("IrisTypeString", new String[]{"Iris-setosa", "Iris-versicolor", "Iris-virginica"}, false) {
                @Override
                protected String getStringValue(IrisData inputData) throws Exception {
                    return inputData.type;
                }
            };

            irisTypeEnum = new EnumFeature<IrisData, IrisType>("IrisTypeEnum", IrisType.class) {
                @Override
                public IrisType getEnumValue(IrisData inputData) throws Exception {
                    switch (inputData.type){
                        case "Iris-setosa":
                            return IrisType.SETOSA;
                        case "Iris-versicolor":
                            return IrisType.VERSICOLOR;
                        case "Iris-virginica":
                            return IrisType.VIRGINICA;
                    }
                    throw new InvalidInputException("cannot convert string "+inputData.type+" to IrisType");
                }
            };

            setosaScore = new DoubleFeature<IrisData>("SetosaScore") {
                @Override
                public Double getDoubleValue(IrisData inputData) throws Exception {
                    if(inputData.type.equals("Iris-setosa"))
                        return 1d;
                    return 0d;
                }
            };
        }
    }

    enum IrisType {
        SETOSA,
        VERSICOLOR,
        VIRGINICA
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

    static <ResponseValueType> void testDiscretePredictions(Model<IrisData, ResponseValueType, ? extends Prediction<ResponseValueType>> model, List<IrisData> test,
                                                            Feature<IrisData, ResponseValueType> responseFeature) throws Exception {
        System.out.println("Testing "+model.getClass());
        System.out.println("To Serialize: ");
        testDiscretePredictionsInner(model, test, responseFeature);
        System.out.println("Deserialized: ");
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("irisModel.model"));
        oos.writeObject(model);
        oos.close();
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("irisModel.model"));
        Model<IrisData, ResponseValueType, ? extends Prediction<ResponseValueType>> deserializedModel
                = (Model<IrisData, ResponseValueType, ? extends Prediction<ResponseValueType>>)ois.readObject();
        ois.close();
        testDiscretePredictionsInner(deserializedModel, test, responseFeature);
    }

    static <ResponseValueType> void testDiscretePredictionsInner(Model<IrisData, ResponseValueType, ? extends Prediction<ResponseValueType>> model,
                                                                 List<IrisData> test, Feature<IrisData, ResponseValueType> responseFeature)
            throws Exception {
        int wrong=0;
        int correct=0;
        for(IrisData testData : test){
            if(model.getPrediction(testData).getValue().equals(responseFeature.getFeatureValue(testData).getValue()))
                correct++;
            else
                wrong++;
        }
        System.out.println("correct = "+correct+" ("+(100*correct/((double)correct+wrong))+"%)");
    }

    static void testRegressionPredictions(Model<IrisData, Double, ? extends Prediction<Double>> model, List<IrisData> test,
                                                            Feature<IrisData, Double> responseFeature) throws Exception {
        System.out.println("Testing "+model.getClass());
        System.out.println("To Serialize: ");
        testRegressionPredictionsInner(model, test, responseFeature);
        System.out.println("Deserialized: ");
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("irisModel.model"));
        oos.writeObject(model);
        oos.close();
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("irisModel.model"));
        Model<IrisData, Double, ? extends Prediction<Double>> deserializedModel
                = (Model<IrisData, Double, ? extends Prediction<Double>>)ois.readObject();
        ois.close();
        testRegressionPredictionsInner(deserializedModel, test, responseFeature);
    }

    static void testRegressionPredictionsInner(Model<IrisData, Double, ? extends Prediction<Double>> model, List<IrisData> test,
                                               Feature<IrisData, Double> responseFeature) throws Exception {
        SimpleRegression regression = new SimpleRegression();
        SummaryStatistics summaryStatistics = new SummaryStatistics();

        for(IrisData testData : test){
            double actual = responseFeature.getFeatureValue(testData).getValue();
            double predicted = model.getPrediction(testData).getValue();
            regression.addData(actual, predicted);
            summaryStatistics.addValue(Math.abs(actual - predicted));
        }
        System.out.println("RMS: "+Math.sqrt(regression.getMeanSquareError()));
        System.out.println("Avg Abs Error: "+summaryStatistics.getMean());
        System.out.println("Abs Error Std Dev: "+summaryStatistics.getStandardDeviation());
    }
}