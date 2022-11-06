package bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.tags;


public class LexicalPatterns {
    public static final PreparerPatterns STRING_REGEX = PreparerPatterns.newInstance("^[\\s&&[^\\n]]*=[\\s&&[^\\n]]*([\\wА-Яа-яё.\\-]+)[\\s&&[^\\n]]*;");

    public static final PreparerPatterns NUMBER_REGEX = PreparerPatterns.newInstance("^[\\s&&[^\\n]]*=[\\s&&[^\\n]]*(-?\\d{1,2}([.]\\d{1,2})?)[\\s&&[^\\n]]*;");

}
