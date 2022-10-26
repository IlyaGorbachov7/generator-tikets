package bntu.fitr.gorbachev.ticketsgenerator.main.threads;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.QuestionExt;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.ContentExtractException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import static bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.ExtractorDefinerParaProperties.*;

public abstract class AbstractContentExtractThread<T extends QuestionExt>
        implements Callable<List<T>> {

    protected XWPFDocument docxFile;
    protected String urlDocxFile;
    protected Supplier<T> supplierQuestion;

    public AbstractContentExtractThread() {
    }

    /**
     * Constructor without parameters
     *
     * @param docxFile    class object {@link XWPFDocument} constants list questions
     * @param urlDocxFile url file
     */
    public AbstractContentExtractThread(XWPFDocument docxFile, String urlDocxFile) {
        this.docxFile = docxFile;
        this.urlDocxFile = urlDocxFile;
        this.supplierQuestion = factoryQuestion();
    }

    /**
     * @return class object {@link XWPFDocument}
     */
    public XWPFDocument docxFile() {
        return docxFile;
    }

    /**
     * @return string file url
     */
    public String urlDocxFile() {
        return urlDocxFile;
    }

    public Supplier<T> getSupplierQuestion() {
        return supplierQuestion;
    }

    public void setDocxFile(XWPFDocument docxFile) {
        this.docxFile = docxFile;
    }

    public void setUrlDocxFile(String urlDocxFile) {
        this.urlDocxFile = urlDocxFile;
    }

    public void setSupplierQuestion(Supplier<T> supplierQuestion) {
        this.supplierQuestion = supplierQuestion;
    }

    /**
     * Execution method
     *
     * @return map questions
     * @throws Exception in case general troubles
     */
    @Override
    public List<T> call() throws ContentExtractException {
        List<T> listQuestions = new ArrayList<>();

        var paragraphs = docxFile.getParagraphs();
        String topic;
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
                topic = extractValFromStartTag(curP);
                if (topic == null) {   // don't must null
                    throw new NullPointerException(urlDocxFile + "\nProgrammist you made a mistake");
                }
                topic = (topic.equals("")) ? "topic_" + i : topic;

                i++;
                while (i < paragraphs.size() && isNumbering(curP = paragraphs.get(i))) { // running by one topic
                    T ques = supplierQuestion.get();// 1 question - can contain picture or math-expressions
                    ques.setSection(topic);
                    ques.add(curP);

                    int j = i + 1;
                    while (j < paragraphs.size() && (!isNumbering(curP = paragraphs.get(j)) && !isEndTag(curP))) { // running by one questions
                        if (isStartTag(curP)) { // required condition
                            throw new ContentExtractException(urlDocxFile +
                                                              "\nBy reading content of the question no found end tag : <S/>");
                        }
                        ques.add(curP);
                        ++j;
                    }

                    i = j; // then update index, point on the
                    listQuestions.add(ques);
                }
//
//                if (isEndTag(curP)) {
//                    // so far nothing. I will delete soon
//                } else {
//                    throw new ContentExtractException(urlDocxFile + "\nBy reading numbering" +
//                                                      " list no find  end tag : <S/>");
//                }
            }
        }
        return listQuestions;
    }

    protected abstract Supplier<T> factoryQuestion();

    /**
     * This method validate order determine start and end tags.
     * <p>
     * If case success, this method return {@code true}
     * <p>
     * else in case failure return {@code false} - if start and end tags no exist (that is indexStart and indexEnd < 0)
     * <b>or</b> in case failure throw exception {@link ContentExtractException}
     * <p>
     * <b>Success this method: </b> <i>If indexStartTag > indexEndTag</i>
     *
     * @param i       index start tag
     * @param iEndTag index end tag
     * @return true, in case success, otherwise false
     * @throws ContentExtractException
     */
    protected boolean isEndTagAfterStartTag(int i, int iEndTag) throws ContentExtractException {
        if ((i < 0 && iEndTag < 0) || (iEndTag > 0 && i > iEndTag) || (i > 0 && iEndTag < 0) || (i < 0 && iEndTag > 0)) {

            if (i < 0 && iEndTag < 0) { // i < 0 && iEndTag < 0
                return false; // if no searched start and end tags

                // in other case throw exceptions
            } else if (iEndTag > 0 && i > iEndTag) {  // iEndTag > 0 && i > iEndTag
                throw new ContentExtractException(urlDocxFile +
                                                  "\n No specified start tag, although exist end tag");
            } else if (i > 0) { // i > 0 && iEndTag < 0
                throw new ContentExtractException(urlDocxFile +
                                                  "\n No find  end tag : <S/>");
            } else { // i < 0 && iEndTag > 0
                throw new ContentExtractException(urlDocxFile +
                                                  "\n Not exist start tag, although exist end tag");
            }
        }
        return true;
    }
}
