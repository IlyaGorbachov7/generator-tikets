package bntu.fitr.gorbachev.ticketsgenerator.main.exceptions;

/**
 * This class is checked exception.
 * <p>
 * It is thrown when the number of required questions is not enough
 *
 * @author Gorbachev I. D.
 * @version 12, 03.2022
 */
public final class NumberQuestionsRequireException extends Exception {
    /**
     * Constructor without parameters
     */
    public NumberQuestionsRequireException() {
        super();
    }

    /**
     * Constructs with the given detail message.
     *
     * @param message The detail message; can be null
     */
    public NumberQuestionsRequireException(String message) {
        super(message);
    }
}
