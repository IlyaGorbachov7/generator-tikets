package bntu.fitr.gorbachev.ticketsgenerator.main.util.loc;

public class LocalizerException extends Exception{
    public LocalizerException() {
    }

    public LocalizerException(String message) {
        super(message);
    }

    public LocalizerException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocalizerException(Throwable cause) {
        super(cause);
    }

    public LocalizerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
