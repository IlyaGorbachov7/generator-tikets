package bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.util.List;
import java.util.Objects;

public class ExtractorDefinerParaProperties {

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
    public static boolean checkTagCondition(XWPFParagraph p) {
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
