package bntu.fitr.gorbachev.ticketsgenerator.main.threads.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.QuestionExt;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.OutputContentException;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.AbstractOutputContentThread;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.constants.TextPatterns;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.math.BigInteger;
import java.util.List;
import java.util.regex.Matcher;

public class OutputContentWriter extends AbstractOutputContentThread<Ticket<Question2>> {
    protected int twipsPerInch = 1440;

    protected int PAGE_W = 12240;   //12240 Twips = 12240/20 = 612 pt = 612/72 = 8.5"
    protected int PAGE_H = 15840;   //15840 Twips = 15840/20 = 792 pt = 792/72 = 11"
    protected int PAGE_TOP = (int) (0.1968 * twipsPerInch); // 0.5 см
    protected int PAGE_BOTTOM = (int) (0.2952 * twipsPerInch); // 0.75 см

    protected int INDENTATION_LEFT = (int) (-0.4 * twipsPerInch);

    protected int SPACING_AFTER_GENERAL = 0; // 1 -> отступ 0.05 в доке
    protected int SPACING_AFTER_LIST = 120; // отступ 6 в доке -> 6/0.05 = 120

    protected int INDENTATION_LEFT_QUEST = (int) (-0.1 * twipsPerInch);
    protected int FIRST_LINE_INDENT_QUEST = (int) (-0.3 * twipsPerInch);

    protected int WIDTH_TABLE = (int) (18.5 * 0.4 * twipsPerInch);
    protected int WIDTH_CALL_1 = 4 * twipsPerInch;
    protected int WIDTH_CALL_2 = 3 * twipsPerInch;

    /**
     * Constructor without parameters
     * <p>
     * 1 inch == 2.54 см
     * <p>
     * 1 twip  = 1/1440 inch. The measure unit is <b>Twip</b> (twentieth of an inch point).
     * <p>
     * 1 inch = 1 * 1440
     *
     * @param listTickets list tickets
     * @see <a href="https://stackoverflow.com/questions/37190366/ctpagemar-values-unit"> unit of the values</a>
     */
    public OutputContentWriter(List<Ticket<Question2>> listTickets) {
        super(listTickets);
    }

    @Override
    public List<Ticket<Question2>> getListTickets() {
        return super.getListTickets();
    }

    @Override
    public XWPFDocument call() throws OutputContentException {
        XWPFDocument docxDes = new XWPFDocument();
        // there must be a styles document, even if it is empty
        docxDes.createStyles();

        // there must be section properties for the page having at least the page size set
        CTSectPr sectPr = docxDes.getDocument().getBody().addNewSectPr();
        CTPageSz pageSz = sectPr.addNewPgSz();
        pageSz.setW(BigInteger.valueOf(PAGE_W));

        // setting page border: top and bottom
        var pgMar = sectPr.addNewPgMar();
        pgMar.setBottom(BigInteger.valueOf(PAGE_BOTTOM));
        pgMar.setTop(BigInteger.valueOf(PAGE_TOP));

        int iter = 0;
        for (var ticket : listTickets) {
            var text = ticket.getInstitute();
            createPara(text, docxDes, INDENTATION_LEFT, SPACING_AFTER_GENERAL,
                    ParagraphAlignment.CENTER, true, true);


            text = "Факультет " + ticket.getFaculty();
            createPara(text, docxDes, INDENTATION_LEFT, SPACING_AFTER_GENERAL,
                    ParagraphAlignment.CENTER, false, true);


            text = "Кафедра " + ticket.getDepartment();
            createPara(text, docxDes, INDENTATION_LEFT, SPACING_AFTER_GENERAL,
                    ParagraphAlignment.CENTER, false, false)
                    .setBorderBottom((ticket.getSpecialization().isEmpty()) ? Borders.SINGLE : Borders.NONE);

            // not necessary or necessary
            if (!ticket.getSpecialization().isEmpty()) {
                text = "Специальность " + ticket.getSpecialization();
                createPara(text, docxDes, INDENTATION_LEFT, SPACING_AFTER_GENERAL,
                        ParagraphAlignment.CENTER, false, false)
                        .setBorderBottom(Borders.SINGLE);
            }

            docxDes.createParagraph().setSpacingAfter(SPACING_AFTER_GENERAL);


            text = "Экзаменационный билет №" + (iter + 1);
            createPara(text, docxDes, INDENTATION_LEFT, SPACING_AFTER_GENERAL,
                    ParagraphAlignment.CENTER, true, true);

            text = "Дисциплина ";
            var p = createPara(text, docxDes, INDENTATION_LEFT, SPACING_AFTER_GENERAL,
                    ParagraphAlignment.CENTER, false, false);
            text = "«" + ticket.getDiscipline() + "»";
            setConfig(p, ParagraphAlignment.CENTER, text, false, false);


            String strForm = "";
            Matcher matcher = TextPatterns.DATE_PATTERN.getMatcher(ticket.getDate());
            if (matcher.find()) {
                strForm = matcher.group(3);
                strForm += (ticket.getType() == Ticket.SessionType.WINTER) ?
                        ("/" + (Integer.parseInt(strForm) + 1)) : "";
            }
            text = ticket.getType() + " экзаменационная сессия " + strForm + " учебного года";
            createPara(text, docxDes, INDENTATION_LEFT, SPACING_AFTER_GENERAL, ParagraphAlignment.CENTER,
                    false, false);


            docxDes.createParagraph().setSpacingAfter(SPACING_AFTER_GENERAL);

            BigInteger newNumID = getNewDecimalNumberingId(docxDes, BigInteger.valueOf(iter++));
            createListParaQuestions(docxDes, ticket, newNumID);

            docxDes.createParagraph().setSpacingAfter(SPACING_AFTER_GENERAL);

            createTable(docxDes, ticket);


            text = "Утверждено на заседании кафедры ";
            p = createPara(text, docxDes, INDENTATION_LEFT, SPACING_AFTER_GENERAL, ParagraphAlignment.BOTH,
                    false, false);
            text = ticket.getDate() + ", ";
            setConfig(p, ParagraphAlignment.LEFT, text,
                    false, true);
            text = "Протокол №" + ticket.getProtocolNumber();
            setConfig(p, ParagraphAlignment.LEFT, text,
                    false, true);
            if (iter < listTickets.size()) {
                p.createRun().addBreak(BreakType.PAGE);
            }

        }
        return docxDes;
    }

    protected XWPFParagraph createPara(String text, XWPFDocument docxDes, int indentationLeft,
                                       int spacingAfter, ParagraphAlignment alignment,
                                       boolean capitalized, boolean bold) {
        var newP = docxDes.createParagraph();
        newP.setIndentationLeft(indentationLeft);
        newP.setSpacingAfter(spacingAfter);
        setConfig(newP, alignment, text, capitalized, bold);
        return newP;
    }

    protected void createListParaQuestions(XWPFDocument docxDes,
                                           Ticket<? extends QuestionExt> ticket, BigInteger newNumID)
            throws OutputContentException {
        for (var question : ticket.getQuestions()) {
            int indexParagraph = 0;
            for (var resP : question.getListParagraphs()) {
                var desP = docxDes.createParagraph();
                desP.setSpacingAfter(SPACING_AFTER_LIST);
                try {
                    setParagraphProperties(desP);
                } catch (XmlException e) {
                    throw new OutputContentException("setParagraphProperty", e);
                }
                if (resP.getNumID() != null) {
                    desP.setNumID(newNumID);
                    // отступ слева
                    desP.setIndentationLeft(Math.round(INDENTATION_LEFT_QUEST));
                    // отступ первой строки 0.1 - одно деление
                    desP.setFirstLineIndent(Math.round(FIRST_LINE_INDENT_QUEST));
                }

                // Begin parse sources paragraph
                int counterRun = 0;
                int counterOMath = 0;
                XmlCursor xmlcursor = resP.getCTP().newCursor();
                while (xmlcursor.hasNextToken()) { // moving along the paragraph
                    xmlcursor.toNextToken();

                    if (xmlcursor.isStart()) {// if position token located in start any tag

                        // Then if prefix = <w:r> - is run, where contains text or picture
                        if (xmlcursor.getName().getPrefix().equalsIgnoreCase("w") &&
                            xmlcursor.getName().getLocalPart().equalsIgnoreCase("r")) {
                            // then taken xml object : CTR - is run
                            CTR resCTR = (CTR) xmlcursor.getObject();
                            // by him define XWPFRun object
                            XWPFRun resR = resP.getRun(resCTR);
                            // and create run from new paragraph
                            XWPFRun desR = desP.createRun();
                            ++counterRun;
                            desR.setText(resR.text()); // if contain text install it
                            // copy properties : bold, italic, color and other
                            cloneRunProperties(resR, desR);
                            // however, setting properties run according standard requirement
                            desR.setFontFamily("Times New Roman");
                            desR.setFontSize(14);

                            try {
                                clonePictureRun(resR, desR);
                            } catch (Exception e) {
                                throw new OutputContentException("clonePictureRun", e);
                            }
                        }
                        // else if prefix - <w:oMath> - is math function
                        else if (xmlcursor.getName().getLocalPart().equalsIgnoreCase("oMath")) {
                            CTOMath ctoMath = (CTOMath) xmlcursor.getObject();
                            desP.getCTP().addNewOMath();
                            desP.getCTP().setOMathArray(counterOMath++, ctoMath);
                        }

                    } else // if position token located in end tag
                    {
                        if (xmlcursor.isEnd()) {
                            //we have to check whether we are at the end of the paragraph
                            xmlcursor.push();
                            xmlcursor.toParent();
                            if (xmlcursor.getName().getLocalPart().equalsIgnoreCase("p")) {
                                break;
                            }
                            xmlcursor.pop();
                        }
                    }

                }
                indexParagraph++;
            }
        }
    }

    protected void createTable(XWPFDocument docxDes, Ticket<? extends QuestionExt> ticket) {
        XWPFTable table = docxDes.createTable(1, 2);

        CTTblWidth ctTblInd = table.getCTTbl().getTblPr().addNewTblInd();
        ctTblInd.setW(Math.round(INDENTATION_LEFT)); // углубление (лева)
        CTTblWidth ctTblWidth = table.getCTTbl().getTblPr().addNewTblW();
        ctTblWidth.setW(WIDTH_TABLE);// ширина таблицы
        table.getCTTbl().getTblPr().addNewTblLayout().setType(STTblLayoutType.FIXED);
        table.getCTTbl().getTblPr().unsetTblBorders(); // hide borders

        var row = table.getRow(0);
        var cell = row.getCell(0);
//        var tcWCell = cell.getCTTc().getTcPr().addNewTcW(); // то же самое
//        tcWCell.setType(STTblWidth.DXA);
//        tcWCell.setW(BigInteger.valueOf(4 * twipsPerInch));
        cell.setWidth(String.valueOf(WIDTH_CALL_1)); // width == 4 дюйма == 1 см

        var p = cell.getParagraphs().get(0);
        p.setSpacingAfter(SPACING_AFTER_GENERAL);
        p.setVerticalAlignment(TextAlignment.CENTER);
        setConfig(p, ParagraphAlignment.BOTH, "Заведующий кафедры: ",
                false, false);
        String strForm = "";
        Matcher matcher = TextPatterns.PERSON_NAME_PATTERN_V2.getMatcher(ticket.getHeadDepartment());
        if (matcher.find()) {
            strForm += matcher.group(2) + " " + matcher.group(3).charAt(0)
                       + ". " + matcher.group(6).charAt(0) + ".";
        }
        setConfig(p, ParagraphAlignment.LEFT, strForm,
                false, false);

        cell = row.getCell(1);
        cell.setWidth(String.valueOf(WIDTH_CALL_2)); // 3 дюйма = 8 см

        p = cell.getParagraphs().get(0);
        p.setSpacingAfter(SPACING_AFTER_GENERAL);
        p.setVerticalAlignment(TextAlignment.CENTER);
        setConfig(p, ParagraphAlignment.BOTH, "Экзаменатор: ",
                false, false);
        strForm = "";
        matcher = TextPatterns.PERSON_NAME_PATTERN_V2.getMatcher(ticket.getTeacher());
        if (matcher.find()) {
            strForm += matcher.group(2) + " " + matcher.group(3).charAt(0) +
                       ". " + matcher.group(6).charAt(0) + ".";
        }
        setConfig(p, ParagraphAlignment.LEFT, strForm,
                false, false);
    }
}
