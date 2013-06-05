package femo.exception;

public class FeatureSetConditionsNotMetException extends FemoException {
    public FeatureSetConditionsNotMetException(String message){
        super(message);
    }
    public FeatureSetConditionsNotMetException(String message, Exception cause){
        super(message, cause);
    }
}
