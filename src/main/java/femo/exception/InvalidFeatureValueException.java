package femo.exception;

public class InvalidFeatureValueException extends FemoException {
    public InvalidFeatureValueException(String message) {
        super(message);
    }
    public InvalidFeatureValueException(String message, Exception cause){
        super(message, cause);
    }
}
