package bntu.fitr.gorbachev.ticketsgenerator.main.models.exceptions;

public class ContentExtractException extends Exception{
    public ContentExtractException() {
    }

    public ContentExtractException(String message) {
        super(message);
    }

    public ContentExtractException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContentExtractException(Throwable cause) {
        super(cause);
    }

    public ContentExtractException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
