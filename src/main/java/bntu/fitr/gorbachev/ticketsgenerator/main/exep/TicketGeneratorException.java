package bntu.fitr.gorbachev.ticketsgenerator.main.exep;

public class TicketGeneratorException extends Exception {
    public TicketGeneratorException() {
    }

    public TicketGeneratorException(String message) {
        super(message);
    }

    public TicketGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public TicketGeneratorException(Throwable cause) {
        super(cause);
    }

    public TicketGeneratorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
