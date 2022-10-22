package bntu.fitr.gorbachev.ticketsgenerator.main.threads;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Question;
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
    public Map<String, List<Question>> call() throws Exception {
        Map<String, List<Question>> mapQ = new LinkedHashMap<>();
        var paragraphs = docxFile.getParagraphs();
        String topic;
        List<Question> listQ;
        for (int i = 0; i < paragraphs.size(); i++) {
            XWPFParagraph curP = paragraphs.get(i);
            XWPFParagraph nextP;

            i = searchParaStartTag(curP);
            int iEndTag = searchParaEndTag(curP);
            if ((i < 0 && iEndTag < 0) || (iEndTag > 0 && i > iEndTag)
                || (i > 0 && iEndTag < 0)
                || (i < 0 && iEndTag > 0)) {
                if (i < 0 && iEndTag < 0) {
                    break;
                } else if (iEndTag > 0 && i > iEndTag) {
                    throw new IllegalArgumentException(urlDocxFile +
                                                       "\n No specified start tag, although exist end tag");
                } else if (i > 0) {
                    throw new IllegalArgumentException(urlDocxFile +
                                                       "\n No find  end tag : <S/> the end file ");
                } else {
                    throw new IllegalArgumentException(urlDocxFile +
                                                       "\n Not exist start tag, although exist end tag");
                }

            } else {// search start tag is success
                curP = paragraphs.get(i); // start tag
                nextP = (i < paragraphs.size() - 1) ? paragraphs.get(i + 1) : curP;

                // Если tag <S> в конце параграфа и за ним ничего не идет
                if (nextP == curP) {
                    throw new IllegalArgumentException(urlDocxFile +
                                                       "\nNext paragraph achieved end file. " +
                                                       "\nStart tag is last paragraph in file");
                }
                if (!isNumbering(nextP)) { // если за тегом <S> нет нумерованного списка
                    throw new IllegalArgumentException(urlDocxFile +
                                                       "\nNext paragraph is not numeration list");
                }

                // if all required check is fulfilled
                // Then...
                topic = extractValFromStartTag(curP);
                if (topic == null) {   // don't must null
                    throw new NullPointerException(urlDocxFile +
                                                   "\nProgrammist you made a mistake");
                }
                topic = (topic.equals("")) ? "topic_" + i : topic;

                listQ = new ArrayList<>();
                i++;
                while (i < paragraphs.size() && isNumbering(curP = paragraphs.get(i))) {
                    Question ques = new Question();
                    ques.add(curP);
                    int j = i + 1;
                    while (j < paragraphs.size() &&
                           (!isNumbering(curP = paragraphs.get(j)) && !isEndTag(curP))) {

                        if (isStartTag(curP)) {
                            throw new IllegalArgumentException(urlDocxFile +
                                                               "\nBy reading numbering list no find  " + "end tag : <S/>");
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
                    throw new IllegalArgumentException(urlDocxFile + "\nBy reading numbering" +
                                                       " list no find  end tag : <S/> the end file");
                }
            }
        }
        return mapQ;
    }

    /**
     * The method checks whether the paragraph is a numbered
     *
     * @param p class object {@link XWPFParagraph}
     * @return true is numbering else false
     */
    private static boolean isNumbering(XWPFParagraph p) {
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
    private static boolean isStartTag(XWPFParagraph p) {
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
    private static boolean isEndTag(XWPFParagraph p) {
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
    private static String extractValFromStartTag(XWPFParagraph p) {
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
    private static int searchParaEndTag(XWPFParagraph curPara) {
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
    private static int searchParaStartTag(XWPFParagraph curPara) {
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
