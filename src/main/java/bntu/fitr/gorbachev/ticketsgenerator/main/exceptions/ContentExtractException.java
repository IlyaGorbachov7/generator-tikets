package bntu.fitr.gorbachev.ticketsgenerator.main.exceptions;

public class ContentExtractException extends Exception{
    public ContentExtractException() {
    }

    public ContentExtractException(String message) {
        super(message);
    }

    public ContentExtractException(String message, Throwable cause) {
        super(message, cause);
    }
}
