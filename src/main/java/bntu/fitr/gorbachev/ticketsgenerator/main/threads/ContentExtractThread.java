package bntu.fitr.gorbachev.ticketsgenerator.main.threads;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Question;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.ContentExtractException;
import org.apache.poi.xwpf.usermodel.*;

import java.util.*;
import java.util.concurrent.Callable;

import static bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.ExtractorDefinerParaProperties.*;

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

                // Если tag <S> в конце параграфа и за ним ничего не идет
                if (nextP == curP) {
                    throw new ContentExtractException(urlDocxFile +
                                                      "\nNext paragraph achieved end file. " +
                                                      "\nStart tag is last paragraph in file");
                }
                if (!isNumbering(nextP)) { // если за тегом <S> нет нумерованного списка
                    throw new ContentExtractException(urlDocxFile +
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
                            throw new ContentExtractException(urlDocxFile +
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
                    throw new ContentExtractException(urlDocxFile + "\nBy reading numbering" +
                                                      " list no find  end tag : <S/> the end file");
                }
            }
        }
        return mapQ;
    }

    private boolean isEndTagAfterStartTag(int i, int iEndTag) throws ContentExtractException {
        if ((i < 0 && iEndTag < 0) || (iEndTag > 0 && i > iEndTag) || (i > 0 && iEndTag < 0) || (i < 0 && iEndTag > 0)) {
            if (i < 0 && iEndTag < 0) {
                return false; // if no searched start and end tags

                // in other case throw exceptions
            } else if (iEndTag > 0 && i > iEndTag) {
                throw new ContentExtractException(urlDocxFile +
                                                  "\n No specified start tag, although exist end tag");
            } else if (i > 0) {
                throw new ContentExtractException(urlDocxFile +
                                                  "\n No find  end tag : <S/> the end file ");
            } else {
                throw new ContentExtractException(urlDocxFile +
                                                  "\n Not exist start tag, although exist end tag");
            }
        }
        return true;
    }
}