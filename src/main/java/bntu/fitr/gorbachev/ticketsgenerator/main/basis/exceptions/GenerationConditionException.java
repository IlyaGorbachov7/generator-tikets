package bntu.fitr.gorbachev.ticketsgenerator.main.basis.exceptions;

public class GenerationConditionException extends Exception{
    public GenerationConditionException() {
    }

    public GenerationConditionException(String message) {
        super(message);
    }

    public GenerationConditionException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenerationConditionException(Throwable cause) {
        super(cause);
    }

    public GenerationConditionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
