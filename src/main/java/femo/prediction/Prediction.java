package femo.prediction;

public abstract class Prediction<PredictionValue> {
    PredictionValue value;
    protected Prediction (PredictionValue value){
        this.value = value;
    }
    public PredictionValue getValue(){
        return value;
    }
}
