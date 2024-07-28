package bntu.fitr.gorbachev.ticketsgenerator.main.util.logger;


public class LoggerException extends Exception {
    public LoggerException() {
    }

    public LoggerException(String message) {
        super(message);
    }

    public LoggerException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoggerException(Throwable cause) {
        super(cause);
    }
}
