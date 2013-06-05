package femo.exception;

public class ModelConditionsNotMetException extends FemoException {
    public ModelConditionsNotMetException(String message){
        super(message);
    }
    public ModelConditionsNotMetException(String message, Exception cause){
        super(message, cause);
    }
}
