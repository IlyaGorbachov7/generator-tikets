package bntu.fitr.gorbachev.ticketsgenerator.main.basis.exceptions;

public class FindsNonMatchingLevel extends GenerationConditionException{
    public FindsNonMatchingLevel() {
    }

    public FindsNonMatchingLevel(String message) {
        super(message);
    }

    public FindsNonMatchingLevel(String message, Throwable cause) {
        super(message, cause);
    }

    public FindsNonMatchingLevel(Throwable cause) {
        super(cause);
    }

    public FindsNonMatchingLevel(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
