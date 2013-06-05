package femo.exception;

public class FemoException extends Exception {
    public FemoException(String message){
        super(message);
    }
    public FemoException(String message, Exception cause){
        super(message, cause);
    }
}
