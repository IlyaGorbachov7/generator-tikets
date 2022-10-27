package bntu.fitr.gorbachev.ticketsgenerator.main.threads.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Question;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.ContentExtractException;
import org.apache.poi.xwpf.usermodel.*;

import java.util.*;
import java.util.concurrent.Callable;


/**
 * This class extract content from file with docx extension
 * <p>
 * Purpose thread is extract content from file .docx representing class object {@link XWPFDocument}
 * to the questions map
 *
 * @author Gorbachev I. D.
 * @version 13.03.2022
 */
@Deprecated(forRemoval = true)
public class ContentExtractThread
        implements Callable<Map<String, List<Question>>> {

    private final XWPFDocument docxFile;
    private final String urlDocxFile;

    /**
     * Constructor without parameters
     *
     * @param docxFile    class object {@link XWPFDocument} constants list questions
     * @param urlDocxFile url file
     */
    public ContentExtractThread(XWPFDocument docxFile, String urlDocxFile) {
        this.docxFile = docxFile;
        this.urlDocxFile = urlDocxFile;
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

    /**
     * Execution method
     *
     * @return map questions
     * @throws Exception in case general troubles
     */
    @Override
    public Map<String, List<Question>> call() throws ContentExtractException {
        Map<String, List<Question>> mapQ = new LinkedHashMap<>();
        var paragraphs = docxFile.getParagraphs();
        String topic;
        List<Question> listQ;
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

                listQ = new ArrayList<>(); // list questions into topic
                i++;
                while (i < paragraphs.size() && isNumbering(curP = paragraphs.get(i))) { // running by one topic
                    Question ques = new Question(){};// 1 question - can contain picture or math-expressions
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
                    listQ.add(ques);
                }

                if (isEndTag(curP)) {
                    List<Question> prevListQ = mapQ.putIfAbsent(topic, listQ);
                    if (prevListQ != null) { // If by specific key (title) already exist value
                        prevListQ.addAll(listQ); // then add
                    }
                } else {
                    throw new ContentExtractException(urlDocxFile + "\nBy reading numbering" +
                                                      " list no find  end tag : <S/>");
                }
            }
        }
        return mapQ;
    }

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


    /**
     * The method checks whether the paragraph is a numbered
     *
     * @param p class object {@link XWPFParagraph}
     * @return true is numbering else false
     */
    public static boolean isNumbering(XWPFParagraph p) {
        return !Objects.isNull(p.getNumID());
    }

    /**
     * The method checks whether the paragraph is a start tag
     * <p>
     * Start tag : <b>{@code <S>...>}</b> or just <b>{@code <S>}</b>
     * <p>
     * ... - value string.
     * <p>
     * <b>{@code <S>}</b>, then value = "" - empty string
     * <p>
     * <b>{@code <S>>}</b> - value = "" - empty string
     *
     * @param p class object {@link XWPFParagraph}
     * @return true is startTag else false
     */
    public static boolean isStartTag(XWPFParagraph p) {
        if (!checkTagCondition(p)) return false;
        String s = p.getText().trim();
        return s.equals("<S>") ||
               ((s.startsWith("<S>")) && s.endsWith(">"));
    }

    /**
     * The method checks whether the paragraph is a end tag
     * <p>
     * End tag : <b>{@code <S/>}</b>
     *
     * @param p class object {@link XWPFParagraph}
     * @return true is end tag else false
     */
    public static boolean isEndTag(XWPFParagraph p) {
        if (!checkTagCondition(p)) return false;
        String s = p.getText().trim();
        return s.equals("<S/>");
    }

    /**
     * Exist 3 case used tag :
     * <b>If paragraph is not a tag, then return value {@code null}</b>
     *
     * @return string value
     */
    public static String extractValFromStartTag(XWPFParagraph p) {
        if (!isStartTag(p)) return null;
        String s = p.getText();
        int indexStart = s.indexOf('>');
        int indexEnd = s.lastIndexOf('>');
        if (indexStart == indexEnd) return "";
        return s.substring(++indexStart, indexEnd);
    }

    /**
     * Search for a paragraph in file containing the end tag
     *
     * @param curPara current paragraph is beginning search
     * @return index paragraph containing the end tag or -1 if search is failed
     */
    public static int searchParaEndTag(XWPFParagraph curPara) {
        XWPFDocument docx = curPara.getDocument();
        int curPosPara = docx.getPosOfParagraph(curPara);
        curPosPara = docx.getParagraphPos(curPosPara);
        List<XWPFParagraph> listP = docx.getParagraphs();
        while (curPosPara < listP.size()) {
            if (isEndTag(listP.get(curPosPara))) return curPosPara;
            ++curPosPara;
        }
        return -1;
    }


    /**
     * Search for a paragraph in file containing the start tag
     *
     * @param curPara current paragraph is beginning search
     * @return index paragraph containing the start tag or -1 if search is failed
     */
    public static int searchParaStartTag(XWPFParagraph curPara) {
        XWPFDocument docx = curPara.getDocument();
        int curPosPara = docx.getPosOfParagraph(curPara);
        /* if in file is table, then current position paragraph may be
         * violated with the actual paragraph position in the paragraph list.
         *
         * That's why this line of code is so necessary.
         * If you want to understand told, then add table first, then two
         * list questions wrapping in tag <S><S/>.
         * Between two list questions not should be paragraphs.
         * Remove below line.Then you will see that the position
         *  of the current paragraph is not the same
         * as in the list of paragraphs
         *
         * */
        curPosPara = docx.getParagraphPos(curPosPara); // it very needed. It is very helped in find correct pos
        List<XWPFParagraph> listP = docx.getParagraphs();
        while (curPosPara < listP.size()) {
            if (isStartTag(listP.get(curPosPara))) return curPosPara;
            ++curPosPara;
        }
        return -1;
    }

    /**
     * Checking paragraph for tag condition.
     *
     * @param p paragraph
     * @return true if paragraph meet the requirements
     */
    private static boolean checkTagCondition(XWPFParagraph p) {
        // paragraph necessary must be center alignment
        if (p.getAlignment() != ParagraphAlignment.CENTER) return false;

        // paragraph don't must be numeration list
        if (!Objects.isNull(p.getNumID())) return false;

        // paragraph don't must contains math function
        if (!p.getCTP().getOMathList().isEmpty() ||
            !p.getCTP().getOMathParaList().isEmpty()) return false;

        // if absent runs is means that absent string - is false
        for (var run : p.getRuns()) {
            // run don't must contain picture
            if (!run.getEmbeddedPictures().isEmpty()) return false;
        }
        return true;
    }
}