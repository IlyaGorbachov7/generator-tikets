package bntu.fitr.gorbachev.ticketsgenerator.main.threads.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.ContentExtractException;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.InvalidLexicalException;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.AbstractContentExtractThread;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.attributes.AttributeService;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.attributes.impl.QuestTagAttributeService;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.constants.TagPatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;

public class ContentExtractor extends AbstractContentExtractThread<Question2> {

    public ContentExtractor(XWPFDocument docxFile, String urlDocxFile) {
        super(docxFile, urlDocxFile);
    }

    @Override
    public List<Question2> call() throws ContentExtractException {
        List<Question2> listQuestions = new ArrayList<>();

        var paragraphs = docxFile.getParagraphs();
        for (int i = 0; i < paragraphs.size(); i++) {
            XWPFParagraph curP = paragraphs.get(i);
            XWPFParagraph nextP;

            i = searchParaStartTag(curP);
            int iEndTag = searchParaEndTag(curP);
            if (!isEndTagAfterStartTag(i, iEndTag)) {
                break;
            } else {// search start tag is success
                curP = paragraphs.get(i); // start tag
                nextP = (i < paragraphs.size() - 1) ? paragraphs.get(i + 1) : curP;

                if (!isNumbering(nextP)) { // если за тегом <S> нет нумерованного списка
                    throw new ContentExtractException(urlDocxFile +
                                                      "\nNext paragraph is not numeration list");
                }

                // if all required check is fulfilled
                // Then...
                AttributeService listStartTagService;
                try { /* extract attributes from ListStartTag and put values inside a class ListTagAttributesService*/
                    listStartTagService = extractAttributesFromListStartTag(curP);
                } catch (InvalidLexicalException e) {
                    throw new ContentExtractException(e.getMessage());
                }

                i++;
                while (i < paragraphs.size() && isNumbering(curP = paragraphs.get(i))) { // running by one topic
                    Question2 ques = supplierQuestion.get();// 1 question - can contain picture or math-expressions
                    /* filling question fields via object implementing AttributeService*/
                    fillerQuestionFields(ques, listStartTagService);
                    /* checking paragraph to contains QuestionTag */
                    if (isExistQuestTag(curP)) {
                        try { /*extract attributes from QuestionTag and put value inside a class QuestTagAttributeService*/
                            AttributeService questTagService = extractAttributesFromQuestTag(curP);
                            fillerQuestionFields(ques, questTagService);
                        } catch (InvalidLexicalException e) {
                            throw new ContentExtractException(e.getMessage());
                        }
                    }
                    ques.add(curP);

                    int j = i + 1;
                    while (j < paragraphs.size() && (!isNumbering(curP = paragraphs.get(j)) && !isEndTag(curP))) { // running by one questions
                        if (isListStartTag(curP)) { // required condition
                            throw new ContentExtractException(urlDocxFile +
                                                              "\nBy reading content of the question no found end tag : <\\S>");
                        }
                        ques.add(curP);
                        ++j;
                    }

                    i = j; // then update index, point on the
                    listQuestions.add(ques);
                }
            }
        }
        return listQuestions;
    }

    @Override
    protected Supplier<Question2> factoryQuestion() {
        return Question2::new;
    }

    @Override
    protected void fillerQuestionFields(Question2 quest, AttributeService service) {
        if (service instanceof QuestTagAttributeService questTagAttributeService) {
            quest.setRepeat((questTagAttributeService.getR() <= 0 ? quest.getRepeat() :
                    questTagAttributeService.getR()));
            quest.setLevel((questTagAttributeService.getL()) <= 0 ? quest.getLevel() :
                    questTagAttributeService.getL());
        } else super.fillerQuestionFields(quest, service);
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
    protected QuestTagAttributeService extractAttributesFromQuestTag(XWPFParagraph p) throws InvalidLexicalException {
        if (!isExistQuestTag(p)) throw new IllegalArgumentException("paragraph no exist question tag");

        String s = p.getText().trim();

        String attribute = "";
        for (Matcher matcher = TagPatterns.QUESTION_TAG.getMatcher(s); matcher.find();
             attribute = (attribute == null) ? "" : attribute) {
            attribute = matcher.group(1);
        }
        QuestTagAttributeService service = (QuestTagAttributeService) extractAndFill(attribute,
                QuestTagAttributeService.class);

        removeRunsQuestTagFromParagraph(p);
        return service;
    }

    /**
     * From WPFRParagraph removing XWPFRuns, which contains data about <i>questTag</i>
     */
    protected void removeRunsQuestTagFromParagraph(XWPFParagraph p) {
        if (!isExistQuestTag(p)) throw new IllegalArgumentException("paragraph no exist question tag");

        int indexRun = 0;
        var runs = p.getRuns();
        String s;
        int posSymbol = 0;
        // search XWPFRun to the symbol '}'
        while (indexRun < runs.size() && (posSymbol = (s = runs.get(indexRun).text()).indexOf('}')) < 0) {
            indexRun++;
        }
        // erase a piece of text to a symbol '}', and reinstall the remaining text in the same XPWFRun
        XWPFRun run = runs.get(indexRun);
        String text = run.getText(0);
        text = text.substring(++posSymbol);
        run.setText(text, 0);

        // removing runs contains tag {...}
        --indexRun;
        while (indexRun >= 0) {
            run = runs.get(indexRun);
            CTText[] arrTextsRun = run.getCTR().getTArray();
            if (arrTextsRun.length == 0) { // if XWPFRun content picture or other, then skipping this XPWFRun
                --indexRun;
                continue;
            }
            p.removeRun(indexRun);
            --indexRun;
        }
    }

}
