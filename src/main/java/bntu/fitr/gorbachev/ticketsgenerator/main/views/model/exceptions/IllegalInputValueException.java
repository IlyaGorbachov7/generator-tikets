package bntu.fitr.gorbachev.ticketsgenerator.main.views.model.exceptions;

public class IllegalInputValueException extends RuntimeException{
    public IllegalInputValueException() {
    }

    public IllegalInputValueException(String message) {
        super(message);
    }

    public IllegalInputValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalInputValueException(Throwable cause) {
        super(cause);
    }

    public IllegalInputValueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
