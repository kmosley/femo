package femo.exception;

public class InvalidFeatureException extends FemoValidationException {
    public InvalidFeatureException(String message) {
        super(message);
    }

    public InvalidFeatureException(String message, Exception cause) {
        super(message, cause);
    }
}