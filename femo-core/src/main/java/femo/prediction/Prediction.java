package femo.prediction;

/**
 * Container for a model prediction. This allows metadata from the learning method to be stored along with predictions.
 *
 * @param <PredictionValue>
 */
public abstract class Prediction<PredictionValue> {
    PredictionValue value;
    protected Prediction (PredictionValue value){
        this.value = value;
    }
    public PredictionValue getValue(){
        return value;
    }
}
