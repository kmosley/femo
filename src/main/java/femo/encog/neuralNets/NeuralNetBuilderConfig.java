package femo.encog.neuralNets;

import femo.encog.neuralNets.activationFunction.KActivationFunction;
import femo.normalizer.Normalizer;

import java.util.Random;

public class NeuralNetBuilderConfig {
    public int randomSeed = new Random().nextInt();
    public int[] hiddenLayerCounts = new int[]{15, 5};
    public KActivationFunction activationFunction = KActivationFunction.TANH;
    public int secondsToTrain = 60;
    public Normalizer normalizer;
}
