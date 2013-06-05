package femo.exception;

public class TrainingSetConditionsNotMet extends FemoException {
    public TrainingSetConditionsNotMet(String message){
        super(message);
    }
    public TrainingSetConditionsNotMet(String message, Exception cause){
        super(message, cause);
    }
}