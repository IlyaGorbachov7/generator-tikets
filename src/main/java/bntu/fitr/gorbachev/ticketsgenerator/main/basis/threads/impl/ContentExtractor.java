package bntu.fitr.gorbachev.ticketsgenerator.main.basis.threads.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.basis.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.QuestionExt;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.exceptions.ContentExtractException;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.exceptions.InvalidLexicalException;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.threads.AbstractContentExtractThread;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.threads.tools.attributes.AttributeService;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.threads.tools.attributes.impl.ListTagAttributeService;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.threads.tools.attributes.impl.QuestTagAttributeService;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.threads.tools.constants.TagPatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
                ListTagAttributeService listStartTagService;
                try { /* extract attributes from ListStartTag and put values inside a class ListTagAttributesService*/
                    listStartTagService = (ListTagAttributeService) extractAttributesFromListStartTag(curP);
                } catch (InvalidLexicalException e) {
                    throw new ContentExtractException(e.getMessage());
                }

                i++;
                while (i < paragraphs.size() && isNumbering(curP = paragraphs.get(i))) { // running by one topic
                    Question2 ques = supplierQuestion.get();// 1 question - can contain picture or math-expressions
                    QuestTagAttributeService questTagService = null;
                    /* checking paragraph to contains QuestionTag */
                    if (isExistQuestTag(curP)) {
                        try { /*extract attributes from QuestionTag and put value inside a class QuestTagAttributeService*/
                            questTagService = extractAttributesFromQuestTag(curP);
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

                    if (!Objects.isNull(questTagService)) { // else override attributes question of values contains in questTagService
                        if (questTagService.getR() >= QuestionExt.MIN_VALUE_REPEAT) {
                            final int valueAtrR = listStartTagService.getR(); // value can be < MIN_VALUE_REPEAT, so
                            if (valueAtrR < QuestionExt.MIN_VALUE_REPEAT) { // this is needed that setter setRepeat from QuestionExt don't throw exception
                                listStartTagService.setR(questTagService.getR());
                            }
                            // then will be filling question fields via object implementing listStartTagService
                            fillerQuestionFields(ques, listStartTagService);
                            // then return old value
                            listStartTagService.setR(valueAtrR);
                            // filling question fields via object implementing QuestTagAttributeService
                            fillerQuestionFields(ques, questTagService);
                            if(listStartTagService.getR() >= QuestionExt.MIN_VALUE_REPEAT){
                                listQuestions.add(ques);
                            }else if(questTagService.getR() != Integer.MAX_VALUE){
                                listQuestions.add(ques);
                            }
                        }
                        // else if getR <= 0 then question don't add in list
                    } else {
                        if (listStartTagService.getR() >= QuestionExt.MIN_VALUE_REPEAT) {
                            /* filling question fields via object implementing AttributeService*/
                            fillerQuestionFields(ques, listStartTagService);
                            /*and adding question in list, else if getR() < MIN_VALUE_REPEAT then this question don't add in list*/
                            listQuestions.add(ques);
                        }
                        // else if getR <= 0 then question don't add in list
                    }
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
            // value getL() can be  < or >= MIN_VALUE_LEVEL. Throw exception if getL < MIN_VALUE_LEVEL
            quest.setLevel((questTagAttributeService.getL()) == Integer.MAX_VALUE ? quest.getLevel() :
                    questTagAttributeService.getL());
            // value getR() always >= MIN_VALUE_LEVEL, else throw exception
            quest.setRepeat((questTagAttributeService.getR() == Integer.MAX_VALUE ? quest.getRepeat() :
                    questTagAttributeService.getR()));
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
