package femo.exception;

public class FemoValidationException extends FemoException {
    public FemoValidationException(String message) {
        super(message);
    }

    public FemoValidationException(String message, Exception cause) {
        super(message, cause);
    }
}
