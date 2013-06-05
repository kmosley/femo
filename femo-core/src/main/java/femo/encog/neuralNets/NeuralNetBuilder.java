package femo.encog.neuralNets;

import femo.exception.InvalidFeatureValueException;
import femo.feature.DoubleFeature;
import femo.feature.Feature;
import femo.feature.FeatureValue;
import femo.feature.StringFeature;
import femo.modeling.*;
import org.encog.mathutil.randomize.ConsistentRandomizer;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.Propagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.pattern.FeedForwardPattern;

import java.util.List;
import java.util.Random;

public class NeuralNetBuilder implements ModelBuilder<NeuralNetClassificationModel> {

    protected int randomSeed = new Random().nextInt();
    protected int[] hiddenLayerCounts = new int[]{15, 5};
    protected FemoActivationFunction activationFunction = FemoActivationFunction.TANH;
    protected int secondsToTrain = 30;

    @Override
    public <DataType, ResponseDataType> NeuralNetClassificationModel<DataType> buildModel(TrainingSet<DataType, ResponseDataType> trainingSet) throws Exception {

        List<TrainingExample> trainingExamples = trainingSet.generateAllExamples(ExampleDensity.Dense);

        BasicNetwork network = createNetwork(trainingExamples);
        MLDataSet trainingDataSet = createMLDataSet(trainingExamples, trainingSet.getResponseFeature(), activationFunction.mean);
        //MLDataSet cvDataSet = createMLDataSet(cvSet, activationFunction.mean);

        Propagation trainer = new ResilientPropagation(network, trainingDataSet);
        trainer.setThreadCount(0);

        System.out.println("Beginning training...");
        long remaining;
        final long start = System.currentTimeMillis();
        do {
            trainer.iteration();

            final long current = System.currentTimeMillis();
            final long elapsed = (current - start) / 1000;// seconds
            remaining = secondsToTrain - elapsed;

            int iteration = trainer.getIteration();

//            System.out.println("Iteration #" + Format.formatInteger(iteration)
//                    + " Error:" + Format.formatPercent(trainer.getError())
//                    + " elapsed time = " + Format.formatTimeSpan((int) elapsed)
//                    + " time left = "
//                    + Format.formatTimeSpan((int) remaining));
            //if(iteration % 300 == 0)
            //    System.out.println("CV Error: "+network.calculateError(cvDataSet)+" iteration "+iteration);

        } while (remaining > 0);

        NeuralNetClassificationModel<DataType> model = new NeuralNetClassificationModel<DataType>(trainingSet.getFeatureSet(), network, ((StringFeature)trainingSet.getResponseFeature()).getValidValues());

        return model;
    }

    protected <DataType, ResponseInput> BasicNetwork createNetwork(List<TrainingExample> trainingExamples) throws Exception {
        final FeedForwardPattern pattern = new FeedForwardPattern();
        pattern.setInputNeurons((trainingExamples.get(0)).getPredictorFeatureValues().size());
        pattern.setOutputNeurons(1);
        pattern.setActivationFunction(activationFunction);

        for (int hiddenLayerCount : hiddenLayerCounts) {
            if (hiddenLayerCount != 0)
                pattern.addHiddenLayer(hiddenLayerCount);
        }

        BasicNetwork network = (BasicNetwork)pattern.generate();
        network.reset();
        new ConsistentRandomizer(-1,1,randomSeed).randomize(network);

        return network;
    }

    protected static <DataType, ResponseInput> MLDataSet createMLDataSet(List<TrainingExample> trainingExamples, Feature responseFeature, double activationFunctionMean) throws Exception {
        MLDataSet dataSet = new BasicMLDataSet();
        for(TrainingExample example : trainingExamples){
            MLData inputData = createMLData(example, activationFunctionMean);
            if(example.responseFeatureValue.getValue() instanceof Double)
                dataSet.add(inputData, new BasicMLData(new double[]{(Double) example.responseFeatureValue.getValue()}));
            if(example.responseFeatureValue.getValue() instanceof String)
                dataSet.add(inputData, new BasicMLData(new double[]{((StringFeature) responseFeature).getValueIndex((String)example.responseFeatureValue.getValue())}));
        }
        return dataSet;
    }

    protected static MLData createMLData(Example example, double activationFunctionMean) throws Exception {
        MLData inputData = new BasicMLData(example.getPredictorFeatureValues().size());
        // keyset should be ordered
        int dataInd=0;
        for(FeatureValue featureValue : example.getPredictorFeatureValues()){
            if(featureValue.getValue() == null)
                inputData.setData(dataInd++, activationFunctionMean);
            else
                inputData.setData(dataInd++, (Double)featureValue.getValue());
        }
        return inputData;
    }

    public NeuralNetBuilder setRandomSeed(int randomSeed) {
        this.randomSeed = randomSeed;
        return this;
    }

    public NeuralNetBuilder setHiddenLayerCounts(int[] hiddenLayerCounts) {
        this.hiddenLayerCounts = hiddenLayerCounts;
        return this;
    }

    public NeuralNetBuilder setActivationFunction(FemoActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
        return this;
    }

    public NeuralNetBuilder setSecondsToTrain(int secondsToTrain) {
        this.secondsToTrain = secondsToTrain;
        return this;
    }

    //    public void trainNetworkAdditionalEpochs(Propagation trainer, int epochsToTrain) throws SQLException, IOException, ClassNotFoundException {
//        int startEpoch = trainer.getIteration();
//
//        System.out.println("Beginning training...");
//        final long start = System.currentTimeMillis();
//        for(int i=0; i<epochsToTrain; i++){
//            trainer.iteration();
//            int iteration = trainer.getIteration();
//
//            final long elapsed = (System.currentTimeMillis() - start) / 1000;// seconds
//            int remaining = (int)(elapsed / ((float)(iteration - startEpoch) / epochsToTrain) - elapsed);
//
//            System.out.println("Iteration #" + Format.formatInteger(iteration)
//                    + " Error:" + Format.formatPercent(trainer.getError())
//                    + " elapsed time = " + Format.formatTimeSpan((int) elapsed)
//                    + " time left = "
//                    + Format.formatTimeSpan(remaining));
//
//        }
//    }
}
