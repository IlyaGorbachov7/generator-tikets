package bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.tags;

public class TagPatterns {
    public static final PreparerPatterns LIST_START_TAG = PreparerPatterns.newInstance("^[\\s&&[^\\n]]*<S>((.*)>)?[\\s&&[^\\n]]*$");

    public static final PreparerPatterns LIST_END_TAG = PreparerPatterns.newInstance("^[\\s&&[^\\n]]*<\\\\S>[\\s&&[^\\n]]*$");

    public static final PreparerPatterns QUESTION_TAG = PreparerPatterns.newInstance("^[\\s&&[^\\n]]*\\{(.*?)}");
}
