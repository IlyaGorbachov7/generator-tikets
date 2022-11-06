package bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.tags;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PreparerPatterns {
    private final Pattern pattern;

    private PreparerPatterns(String regex) {
        pattern = Pattern.compile(regex);
    }

    protected static PreparerPatterns newInstance(String regex) {
        return new PreparerPatterns(regex);
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
