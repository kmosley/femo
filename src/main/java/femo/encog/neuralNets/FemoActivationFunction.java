package femo.encog.neuralNets;

import org.encog.engine.network.activation.ActivationFunction;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.engine.network.activation.ActivationTANH;

public class FemoActivationFunction implements ActivationFunction {

    public static final FemoActivationFunction TANH = new FemoActivationFunction(0, -1, 1, new ActivationTANH());
    public static final FemoActivationFunction SIGMOID = new FemoActivationFunction(.5, 0, 1, new ActivationSigmoid());

    public final double mean;
    public final double min;
    public final double max;
    public final ActivationFunction activationFunction;

    public FemoActivationFunction(double mean, double min, double max, ActivationFunction activationFunction) {
        this.mean = mean;
        this.min = min;
        this.max = max;
        this.activationFunction = activationFunction;
    }

    @Override
    public void activationFunction(double[] d, int start, int size) {
        activationFunction.activationFunction(d, start, size);
    }

    @Override
    public double derivativeFunction(double b, double a) {
        return activationFunction.derivativeFunction(b, a);
    }

    @Override
    public boolean hasDerivative() {
        return activationFunction.hasDerivative();
    }

    @Override
    public double[] getParams() {
        return activationFunction.getParams();
    }

    @Override
    public void setParam(int index, double value) {
        activationFunction.setParam(index, value);
    }

    @Override
    public String[] getParamNames() {
        return activationFunction.getParamNames();
    }

    @Override
    public ActivationFunction clone() {
        return new FemoActivationFunction(mean, min, max, activationFunction);
    }
}
