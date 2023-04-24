package bntu.fitr.gorbachev.ticketsgenerator.main.models.exceptions;

public class OutputContentException extends Exception{
    public OutputContentException() {
    }

    public OutputContentException(String message) {
        super(message);
    }

    public OutputContentException(String message, Throwable cause) {
        super(message, cause);
    }
}
