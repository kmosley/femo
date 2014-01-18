package femo.exception;

public class InvalidFeatureValueException extends FemoValidationException {
    public InvalidFeatureValueException(String message) {
        super(message);
    }
    public InvalidFeatureValueException(String message, Exception cause){
        super(message, cause);
    }
}
