package bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This enum contains constants define text patterns
 * for correctly input data
 *
 * @author Gorbachev I. D.
 * @version 06.05.2022
 */
public enum TextPatterns {
    COMMON_PATTERN("[\\wА-Яа-я\\-\"«»\\sЁё&&[^_]]*"),
    PERSON_NAME_PATTERN_V1("(^([А-ЯЁ][а-яё]+)\\s+(([А-ЯЁ][а-яё]+)|([А-ЯЁ]\\.?))" +
                           "\\s+(([А-ЯЁ][а-яё]+)|([А-ЯЁ]\\.?))\\s*)?"),
    PERSON_NAME_PATTERN_V2("(^([А-ЯЁ][а-яё]+)\\s+(([А-ЯЁ][а-яё]+)|([А-ЯЁ]\\.?))" +
                           "\\s+(([А-ЯЁ][а-яё]+)|([А-ЯЁ]\\.?))\\s*)"),
    PROTOCOL_PATTERN("((^[0-9]+)((\\.)([0-9]+))*)?\\s*"),
    NUMBER_PATTERN("[0-9]+"),
    DATE_PATTERN("^(0[1-9]|3[01]|[1-2][\\d])[\\.\\/\\-](0[1-9]|1[0-2])[\\.\\/\\-](\\d{4})$");

    private final Pattern pattern;

    /**
     * This private constructor with parameters
     *
     * @param regex regex expression
     */
    TextPatterns(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    /**
     * This method return template
     *
     * @return is class object {@link Pattern} represent template string
     */
    public Pattern getPattern() {
        return pattern;
    }

    /**
     * This method return class object {@link Matcher}
     *
     * @param input matchers string
     * @return is class object {@link Matcher} represent result matches
     */
    public Matcher getMatcher(String input) {
        return pattern.matcher(input);
    }

    /**
     * This method matches input string with pattern
     *
     * @param input match string
     * @return true if input string match pattern else false
     */
    public boolean matches(String input) {
        return getMatcher(input).matches();
    }

    /**
     * Object represent string
     *
     * @return string
     */
    @Override
    public String toString() {
        return pattern.pattern();
    }
}