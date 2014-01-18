package femo.exception;

public class InvalidInputException extends FemoValidationException {
    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Exception cause) {
        super(message, cause);
    }
}