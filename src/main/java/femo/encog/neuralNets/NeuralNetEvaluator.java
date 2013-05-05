package femo.encog.neuralNets;

import femo.modeling.InMemTrainingSet;
import femo.visualization.PlotGraph;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.jfree.ui.RefineryUtilities;

import java.util.ArrayList;

public class NeuralNetEvaluator {

    public static void printResults(BasicNetwork network, InMemTrainingSet trainingSet, double activationFunctionMean) throws Exception {
        NeuralNetHelper.validateTrainingSet(trainingSet);
        MLDataSet dataSet = NeuralNetBuilder.createMLDataSet(trainingSet, activationFunctionMean);
        ArrayList<Double> xValues = new ArrayList<Double>();
        ArrayList<Double> yValues = new ArrayList<Double>();
        for (MLDataPair pair : dataSet) {
            double networkOutput = network.compute(pair.getInput()).getData(0);
            if(Math.abs(networkOutput) < .75d)
                continue;
            xValues.add(networkOutput);
            yValues.add(pair.getIdeal().getData(0));
            System.out.println("ideal output: " + pair.getIdeal().getData(0) + " network output: " + networkOutput);
        }
        System.out.println("CV Error: "+network.calculateError(dataSet));

        PlotGraph graph = new PlotGraph("Plot Graph", xValues, yValues);
        graph.pack();
        RefineryUtilities.centerFrameOnScreen(graph);
        graph.setVisible(true);
    }
}
