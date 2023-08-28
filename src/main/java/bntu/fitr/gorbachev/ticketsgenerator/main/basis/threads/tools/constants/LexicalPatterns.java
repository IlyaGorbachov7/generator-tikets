package bntu.fitr.gorbachev.ticketsgenerator.main.basis.threads.tools.constants;


import bntu.fitr.gorbachev.ticketsgenerator.main.basis.threads.tools.PreparerPatterns;

/**
 * This class contains <b>pattern-constants</b>, which describe supported rules for writing
 * strings and numbers. This using together attributes
 *
 * @version 06.11.2022
 */
public class LexicalPatterns {
    /**
     * Text: = Bkmsdf ываоыва_233-ываоdss, or:  = TextAndy-top999.
     * @apiNote <b>Should necessarily start with symbol '='</b>
     */
    public static final PreparerPatterns STRING_REGEX = PreparerPatterns
            .newInstance("^[\\s&&[^\\n]]*=[\\s&&[^\\n]]*([\\wА-Яа-яё.\\-]+([\\s&&[^\\n]]*[\\wА-Яа-яё.\\-])*)[\\s&&[^\\n]]*;");

    /**
     * Text: = 23 or: =99.99,
     * <p>
     * <b>No supported:</b>
     * <p>
     * Text: 12 - just number,<b> Should necessarily start with symbol '='</b>
     * <p>
     * <b>No supported:</b>
     * <p>
     * Text: 121 - number more, than 99.99
     */
    public static final PreparerPatterns NUMBER_REGEX = PreparerPatterns
            .newInstance("^[\\s&&[^\\n]]*=[\\s&&[^\\n]]*(-?\\d{1,2}([.]\\d{1,2})?)[\\s&&[^\\n]]*;");

}
