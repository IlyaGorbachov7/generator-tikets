package bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.constants;

import bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.PreparerPatterns;

/**
 * This class contains <b>pattern-constants</b>, which describe rule writing different tags
 *
 * @version 06.11.2022
 */
public class TagPatterns {
    /**
     * Start tag : <b>{@code <S>...>}</b>(\n)? or just <b>{@code <S>}</b>
     * <p>
     * ... - value string.
     * <p>
     * <b>{@code <S>}</b>, then value = "" - empty string
     * <p>
     * <b>{@code <S>>}</b> - value = "" - empty string
     */
    public static final PreparerPatterns LIST_START_TAG = PreparerPatterns.newInstance("^[\\s&&[^\\n]]*<S>((.*)>)?\\s*$");

    /**
     * End tag : <b>{@code <\S>}</b>(\n)?
     */
    public static final PreparerPatterns LIST_END_TAG = PreparerPatterns.newInstance("^[\\s&&[^\\n]]*<\\\\S>\\s*$");

    /**
     * Tag: {@code {...}}
     */
    public static final PreparerPatterns QUESTION_TAG = PreparerPatterns.newInstance("^[\\s&&[^\\n]]*\\{(.*?)}");
}
