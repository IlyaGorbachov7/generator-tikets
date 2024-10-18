package bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.sender;

public class SenderStopSleepException extends RuntimeException {
    public SenderStopSleepException() {
    }

    public SenderStopSleepException(String message) {
        super(message);
    }
}
