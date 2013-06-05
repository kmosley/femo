package femo.exception;

public class InvalidDiscreteValueException extends FemoException {
    public InvalidDiscreteValueException(String message){
        super(message);
    }
    public InvalidDiscreteValueException(String message, Exception cause){
        super(message, cause);
    }
}
