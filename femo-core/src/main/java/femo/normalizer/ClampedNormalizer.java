package femo.normalizer;

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

    public ClampedNormalizer (double clampLow, double clampHigh, double normalLow, double normalHigh){
        this.clampLow = clampLow;
        this.clampHigh = clampHigh;
        this.normalLow = normalLow;
        this.normalHigh = normalHigh;
    }

    @Override
    public Double normalize(Double d){
        if(d > clampHigh)
            d = clampHigh;
        else if (d < clampLow)
            d = clampLow;
        return ((d - clampLow) / (clampHigh - clampLow))
                * (normalHigh - normalLow)
                + normalLow;
    }
}
