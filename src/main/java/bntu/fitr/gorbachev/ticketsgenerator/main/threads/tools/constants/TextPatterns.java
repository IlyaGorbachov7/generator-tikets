package bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.constants;

import bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.PreparerPatterns;

/**
 * This class contains constants define text patterns
 * for correctly input data
 *
 * @author Gorbachev I. D.
 * @version 06.05.2022
 */
public class TextPatterns {

    public static final PreparerPatterns COMMON_PATTERN = PreparerPatterns.newInstance("[\\wА-Яа-я\\-\"«»{}:;)(\\sЁё&&[^_]]*");

    /**
     * Text: Серова Арина Юрьевна, or: Серова А Ю, or: Серова А. Ю.
     */
    public static final PreparerPatterns PERSON_NAME_PATTERN_V1 = PreparerPatterns
            .newInstance("(^([А-ЯЁ][а-яё]+)\\s+(([А-ЯЁ][а-яё]+)|([А-ЯЁ]\\.?))\\s+(([А-ЯЁ][а-яё]+)|([А-ЯЁ]\\.?))\\s*)?");
    /**
     * Same, however this pattern does not oblige you to insert the text. An empty string is allowed.
     */
    public static final PreparerPatterns PERSON_NAME_PATTERN_V2 = PreparerPatterns
            .newInstance("(^([А-ЯЁ][а-яё]+)\\s+(([А-ЯЁ][а-яё]+)|([А-ЯЁ]\\.?))\\s+(([А-ЯЁ][а-яё]+)|([А-ЯЁ]\\.?))\\s*)");
    /**
     * Text: 21213 or: 2.423.0
     */
    public static final PreparerPatterns PROTOCOL_PATTERN = PreparerPatterns
            .newInstance("((^[0-9]+)((\\.)([0-9]+))*)?\\s*");
    /**
     * Text: 234234234
     */
    public static final PreparerPatterns NUMBER_PATTERN = PreparerPatterns.newInstance("[1-9]{1}([0-9]{1,2})?");
    /**
     * Text: 21.02.3232 or: 31/12/2022, or: 03-01-2002
     */
    public static final PreparerPatterns DATE_PATTERN = PreparerPatterns
            .newInstance("^(0[1-9]|3[01]|[1-2][\\d])[\\.\\/\\-](0[1-9]|1[0-2])[\\.\\/\\-](\\d{4})$");
}