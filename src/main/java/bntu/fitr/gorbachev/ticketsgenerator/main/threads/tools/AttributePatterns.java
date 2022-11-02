package bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum AttributePatterns {
    N("(n)[\\s&&[^\\n]]*=[\\s&&[^\\n]]*([\\wА-Яа-яё]+)[\\s&&[^\\n]]*;"),
    R("(r)[\\s&&[^\\n]]*=[\\s&&[^\\n]]*(\\d{1,2})[\\s&&[^\\n]]*;"),
    L("(l)[\\s&&[^\\n]]*=[\\s&&[^\\n]]*(\\d{1,2})[\\s&&[^\\n]]*;");

    private final Pattern pattern;

    /**
     * This private constructor with parameters
     *
     * @param regex regex expression
     */
    AttributePatterns(String regex) {
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
