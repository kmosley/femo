package femo.encog.neuralNets.activationFunction;

import org.encog.engine.network.activation.ActivationFunction;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.engine.network.activation.ActivationTANH;

public class KActivationFunction {
    public final double mean;
    public final double min;
    public final double max;
    public final ActivationFunction activationFunction;

    public KActivationFunction(double mean, double min, double max, ActivationFunction activationFunction) {
        this.mean = mean;
        this.min = min;
        this.max = max;
        this.activationFunction = activationFunction;
    }

    public static final KActivationFunction TANH = new KActivationFunction(0, -1, 1, new ActivationTANH());
    public static final KActivationFunction SIGMOID = new KActivationFunction(.5, 0, 1, new ActivationSigmoid());
}
