package bntu.fitr.gorbachev.ticketsgenerator.main.threads.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.ContentExtractException;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.InvalidLexicalException;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.AbstractContentExtractThread;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.tags.TagPatterns;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.tags.attributes.impl.AttributesListStartTag;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.tags.attributes.SomeAttributes;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;

public class ContentExtractor extends AbstractContentExtractThread<Question2> {

    public ContentExtractor() {
    }

    public ContentExtractor(XWPFDocument docxFile, String urlDocxFile) {
        super(docxFile, urlDocxFile);
    }

    @Override
    public List<Question2> call() throws ContentExtractException {
        return super.call();
    }

    @Override
    protected Supplier<Question2> factoryQuestion() {
        return Question2::new;
    }

    /**
     * This method check the paragraph, which is question, on the contains tag-question.
     * <p>
     * Tag: {@code {...}}
     * <p>
     * <b>Condition tag:</b>
     * <p>
     * 1) This tag should be in beginning question.
     * <p>
     * 2) This paragraph, should be numbering
     *
     * @param p paragraph contains questions
     * @return true if success, else false
     */
    protected boolean isExistQuestTag(XWPFParagraph p) {
        if (!isNumbering(p)) return false;
        String s = p.getText().trim();
        return TagPatterns.QUESTION_TAG.matches(s);
    }

    /**
     * @param p
     * @return
     * @throws InvalidLexicalException
     */
    protected SomeAttributes extractValFromQuestTag(XWPFParagraph p) throws InvalidLexicalException {
        if (!isExistQuestTag(p)) throw new IllegalArgumentException("paragraph is not Numbering as question");

        String s = p.getText().trim();
        String attribute = "";
        Matcher matcher = TagPatterns.QUESTION_TAG.getMatcher(s);
        while (matcher.find()) {
            attribute = matcher.group(1);
            attribute = (attribute == null) ? "" : attribute;
        }

        return extractAndFill(attribute, AttributesListStartTag.class);
    }
}
