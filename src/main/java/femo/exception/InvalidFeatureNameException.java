package femo.exception;

public class InvalidFeatureNameException extends FemoException {
    public InvalidFeatureNameException(String message){
        super(message);
    }
    public InvalidFeatureNameException(String message, Exception cause){
        super(message, cause);
    }
}
