package bntu.fitr.gorbachev.ticketsgenerator.main.basis.threads.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class was created for convenient working by patterns initialization
 *
 * @version 06.11.2022
 */
public class PreparerPatterns {
    private final Pattern pattern;

    /**
     * @param regex regex expression
     */
    private PreparerPatterns(String regex) {
        pattern = Pattern.compile(regex);
    }

    /**
     * Factory method for the creation new instance this a class
     *
     * @param regex regex expression
     * @return new instance
     */
    public static PreparerPatterns newInstance(String regex) {
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
