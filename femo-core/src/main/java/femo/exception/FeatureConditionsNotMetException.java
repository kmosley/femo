package femo.exception;

public class FeatureConditionsNotMetException extends FemoException {
    public FeatureConditionsNotMetException(String message){
        super(message);
    }
    public FeatureConditionsNotMetException(String message, Exception cause){
        super(message, cause);
    }
}
