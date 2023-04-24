package bntu.fitr.gorbachev.ticketsgenerator.main.models.exceptions;

public class FindsChapterWithoutSection extends GenerationConditionException{
    public FindsChapterWithoutSection() {
    }

    public FindsChapterWithoutSection(String message) {
        super(message);
    }

    public FindsChapterWithoutSection(String message, Throwable cause) {
        super(message, cause);
    }

    public FindsChapterWithoutSection(Throwable cause) {
        super(cause);
    }

    public FindsChapterWithoutSection(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
