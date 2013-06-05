package femo.normalizer;

import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;

import java.io.Serializable;

/*
Linear interpolation from the clamp domain to the normal domain

Example:
clampLow = -2
clampHigh = 2
normalLow = -1
normalHigh = 1
value <= -2 = -1
value >= 2 = 1
value of -1 = -.5
value of 0 = 0
 */
public class ClampedNormalizer extends Normalizer<Double, Double> implements Serializable {
    double clampLow;
    double clampHigh;
    double normalLow;
    double normalHigh;

    NormalizedField normalizer;

    public ClampedNormalizer (double clampLow, double clampHigh, double normalLow, double normalHigh){
        this.clampLow = clampLow;
        this.clampHigh = clampHigh;
        this.normalLow = normalLow;
        this.normalHigh = normalHigh;
        normalizer = new NormalizedField(NormalizationAction.Normalize,null,clampHigh,clampLow,normalHigh,normalLow);
    }

    @Override
    public Double normalize(Double d){
        if(d > clampHigh)
            d = clampHigh;
        else if (d < clampLow)
            d = clampLow;
        return normalizer.normalize(d);
    }
}
