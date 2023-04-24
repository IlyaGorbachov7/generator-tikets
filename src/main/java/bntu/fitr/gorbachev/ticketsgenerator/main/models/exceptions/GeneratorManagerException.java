package bntu.fitr.gorbachev.ticketsgenerator.main.models.exceptions;

public class GeneratorManagerException extends RuntimeException{
    public GeneratorManagerException() {
    }

    public GeneratorManagerException(String message) {
        super(message);
    }

    public GeneratorManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeneratorManagerException(Throwable cause) {
        super(cause);
    }

    public GeneratorManagerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
