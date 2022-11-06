package bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.tags;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum TagPatterns {

    LIST_START_TAG("^[\\s&&[^\\n]]*<S>((.*)>)?[\\s&&[^\\n]]*$"),
    LIST_END_TAG("^[\\s&&[^\\n]]*<\\\\S>[\\s&&[^\\n]]*$"),
    QUESTION_TAG("^[\\s&&[^\\n]]*\\{(.*?)}");
    private final Pattern pattern;

    /**
     * This private constructor with parameters
     *
     * @param regex regex expression
     */
    TagPatterns(String regex) {
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
