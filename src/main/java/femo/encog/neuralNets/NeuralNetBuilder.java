package femo.encog.neuralNets;

import femo.feature.FeatureValue;
import femo.modeling.InMemTrainingSet;
import femo.modeling.TrainingExample;
import org.encog.mathutil.randomize.ConsistentRandomizer;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.Propagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.pattern.FeedForwardPattern;

import java.util.ArrayList;

public class NeuralNetBuilder {

    public static BasicNetwork createNeuralNet(NeuralNetBuilderConfig config, InMemTrainingSet trainingSet, InMemTrainingSet cvSet) throws Exception {
        NeuralNetHelper.validateTrainingSet(trainingSet);
        BasicNetwork network = createNetwork(config, trainingSet);
        MLDataSet trainingDataSet = createMLDataSet(trainingSet, config.activationFunction.mean);
        MLDataSet cvDataSet = createMLDataSet(cvSet, config.activationFunction.mean);

        Propagation trainer = new ResilientPropagation(network, trainingDataSet);
        trainer.setThreadCount(0);

        System.out.println("Beginning training...");
        long remaining;
        final long start = System.currentTimeMillis();
        do {
            trainer.iteration();

            final long current = System.currentTimeMillis();
            final long elapsed = (current - start) / 1000;// seconds
            remaining = config.secondsToTrain - elapsed;

            int iteration = trainer.getIteration();

//            System.out.println("Iteration #" + Format.formatInteger(iteration)
//                    + " Error:" + Format.formatPercent(trainer.getError())
//                    + " elapsed time = " + Format.formatTimeSpan((int) elapsed)
//                    + " time left = "
//                    + Format.formatTimeSpan((int) remaining));
            if(iteration % 300 == 0)
                System.out.println("CV Error: "+network.calculateError(cvDataSet)+" iteration "+iteration);

        } while (remaining > 0);

        return network;
    }

    protected static BasicNetwork createNetwork(NeuralNetBuilderConfig config, InMemTrainingSet trainingSet) throws Exception {
        final FeedForwardPattern pattern = new FeedForwardPattern();
        pattern.setInputNeurons(((TrainingExample) trainingSet.getTrainingExamples().get(0)).getPredictorFeatureValues().size());
        pattern.setOutputNeurons(1);
        pattern.setActivationFunction(config.activationFunction.activationFunction);

        for (int hiddenLayerCount : config.hiddenLayerCounts) {
            if (hiddenLayerCount != 0)
                pattern.addHiddenLayer(hiddenLayerCount);
        }

        BasicNetwork network = (BasicNetwork)pattern.generate();
        network.reset();
        new ConsistentRandomizer(-1,1,config.randomSeed).randomize(network);

        return network;
    }

    protected static MLDataSet createMLDataSet(InMemTrainingSet trainingSet, double activationFunctionMean) throws Exception {
        int inputSize = ((TrainingExample)trainingSet.getTrainingExamples().get(0)).getPredictorFeatureValues().size();
        MLDataSet dataSet = new BasicMLDataSet();
        for(TrainingExample example : (ArrayList<TrainingExample>)trainingSet.getTrainingExamples()){
            MLData inputData = new BasicMLData(inputSize);
            // keyset should be ordered since we're using a LinkedHashMap
            int dataInd=0;
            for(FeatureValue featureValue : example.getPredictorFeatureValues()){
                if(featureValue.getValue() == null)
                    inputData.setData(dataInd++, activationFunctionMean);
                else
                    inputData.setData(dataInd++, (Double)featureValue.getValue());
            }
            dataSet.add(inputData, new BasicMLData(new double[]{(Double)example.responseFeatureValue.getValue()}));
        }
        return dataSet;
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
