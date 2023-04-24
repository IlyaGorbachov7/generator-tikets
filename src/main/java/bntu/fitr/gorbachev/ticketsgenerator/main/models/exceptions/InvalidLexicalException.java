package bntu.fitr.gorbachev.ticketsgenerator.main.models.exceptions;

public class InvalidLexicalException extends Exception{
    public InvalidLexicalException() {
    }

    public InvalidLexicalException(String message) {
        super(message);
    }

    public InvalidLexicalException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidLexicalException(Throwable cause) {
        super(cause);
    }

    public InvalidLexicalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
