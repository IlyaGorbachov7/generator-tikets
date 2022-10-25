package bntu.fitr.gorbachev.ticketsgenerator.main.threads;

import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Ticket;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.OutputDefinerParaProperties.*;

/**
 * This class writes a list of tickets to the class object XWPFDocument.
 * <p>
 * Purpose thread is formation file represent class object {@link XWPFDocument}
 *
 * @author Gorbachev I. D.
 * @version 23.04.2022
 */
public class OutputContentFormationThread implements Callable<XWPFDocument> {

    private final List<Ticket> listTickets;

    /**
     * Constructor without parameters
     *
     * @param listTickets list tickets
     */
    public OutputContentFormationThread(List<Ticket> listTickets) {
        this.listTickets = listTickets;
    }

    /**
     * @return list tickets
     */
    public List<Ticket> getListTickets() {
        return listTickets;
    }

    /**
     * Execution method
     *
     * @return map questions
     * @throws Exception in case general troubles
     */
    @Override
    public XWPFDocument call() throws Exception {
        XWPFDocument docxDes = new XWPFDocument();
        // there must be a styles document, even if it is empty
        docxDes.createStyles();

        // there must be section properties for the page having at least the page size set
        CTSectPr sectPr = docxDes.getDocument().getBody().addNewSectPr();
        CTPageSz pageSz = sectPr.addNewPgSz();
        pageSz.setW(BigInteger.valueOf(12240)); //12240 Twips = 12240/20 = 612 pt = 612/72 = 8.5"
        pageSz.setH(BigInteger.valueOf(15840)); //15840 Twips = 15840/20 = 792 pt = 792/72 = 11"

        int iter = 0;
        for (var ticket : listTickets) {
            var text = ticket.getInstitute();
            var newP = docxDes.createParagraph();
            newP.setIndentationLeft((int) (-0.4 * 1440));
            setConfig(newP, ParagraphAlignment.CENTER, text,
                    true, true);

            newP = docxDes.createParagraph();
            newP.setIndentationLeft((int) (-0.4 * 1440));
            text = "Факультет " + ticket.getFaculty();
            setConfig(newP, ParagraphAlignment.CENTER, text,
                    false, false);

            newP = docxDes.createParagraph();
            newP.setIndentationLeft((int) (-0.4 * 1440));
            newP.setBorderBottom(Borders.SINGLE);
            text = "Кафедра " + ticket.getDepartment();
            setConfig(newP, ParagraphAlignment.CENTER, text,
                    false, false);

            // not necessary or necessary
            if (!ticket.getSpecialization().isEmpty()) {
                newP = docxDes.createParagraph();
                newP.setIndentationLeft((int) (-0.4 * 1440));
                newP.setBorderBottom(Borders.SINGLE);
                text = "Специальность " + ticket.getSpecialization();
                setConfig(newP, ParagraphAlignment.CENTER, text,
                        false, false);
            }

            docxDes.createParagraph();

            newP = docxDes.createParagraph();
            newP.setIndentationLeft((int) (-0.4 * 1440));
            text = "Экзаменационный билет №" + (iter + 1);
            setConfig(newP, ParagraphAlignment.CENTER, text,
                    true, true);

            newP = docxDes.createParagraph();
            newP.setIndentationLeft((int) (-0.4 * 1440));
            text = "Дисциплина ";
            setConfig(newP, ParagraphAlignment.CENTER, text,
                    false, false);
            text = "«" + ticket.getDiscipline() + "»";
            setConfig(newP, ParagraphAlignment.CENTER, text,
                    false, false);


            String strForm = ""; // regex : 03.09.2022 or 03/09/2022 or 03-09-2022
            Pattern pattern = Pattern.compile("^(0[1-9]|3[01]|[1-2][\\d])" +
                    "[\\.\\/-](0[1-9]|1[0-2])[\\.\\/-](\\d{4})$");
            Matcher matcher = pattern.matcher(ticket.getDate());
            if (matcher.find()) {
                strForm = matcher.group(3);
                strForm += (ticket.getType() == Ticket.SessionType.WINTER) ?
                        ("/" + (Integer.parseInt(strForm) + 1)) : "";
            }

            newP = docxDes.createParagraph();
            newP.setIndentationLeft((int) (-0.4 * 1440));
            text = ticket.getType() + " экзаменационная сессия " + strForm + " учебного года";
            setConfig(newP, ParagraphAlignment.CENTER, text,
                    false, false);

            docxDes.createParagraph();

            BigInteger newNumID = getNewDecimalNumberingId(docxDes, BigInteger.valueOf(iter++));

            for (var question : ticket.getQuestions()) {
                int indexParagraph = 0;
                for (var resP : question.getListParagraphs()) {
                    var desP = docxDes.createParagraph();
                    setParagraphProperties(desP);
                    if (resP.getNumID() != null) {
                        desP.setNumID(newNumID);
                        // отступ слева
                        desP.setIndentationLeft((int) Math.round(0.0 * 1440));
                        // отступ первой строки 0.1 - одно деление
                        desP.setFirstLineIndent((int) Math.round(-0.3 * 1440));
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

                                clonePictureRun(resR, desR);
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

            docxDes.createParagraph();

            XWPFTable table = docxDes.createTable(2, 2);
            table.getCTTbl().addNewTblGrid();
            table.getCTTbl().getTblGrid().addNewGridCol().setW((8.5 * 0.4) * 1_440);
            for (int col = 1; col < 2; col++) {
                table.getCTTbl().getTblGrid().addNewGridCol().setW(BigInteger.valueOf((long) ((8.5 * 0.4) * 1_440)));
            }
//            table.setWidth("100%");
//            CTTblWidth ctTblInd = table.getCTTbl().getTblPr().addNewTblInd();
//            ctTblInd.setW(Math.round(0.0 * 1_440)); // углубление (лева)
//            CTTblWidth ctTblWidth = table.getCTTbl().getTblPr().addNewTblW();
//            ctTblWidth.setW((16 * 0.4) * 1_440);// ширина таблицы
            table.getCTTbl().getTblPr().addNewTblLayout().setType(STTblLayoutType.FIXED);
            table.getCTTbl().getTblPr().unsetTblBorders(); // hide borders

            var row = table.getRow(0);
            var cell = row.getCell(0);
            var p = cell.getParagraphs().get(0);
            p.setVerticalAlignment(TextAlignment.CENTER);
            setConfig(p, ParagraphAlignment.BOTH, "Заведующий кафедры",
                    false, false);
            cell = row.getCell(1);
            p = cell.getParagraphs().get(0);
            p.setVerticalAlignment(TextAlignment.CENTER);
            strForm = "";
            pattern = Pattern.compile("(^([А-ЯЁ][а-яё]+)\\s+(([А-ЯЁ][а-яё]+)|([А-ЯЁ]\\.?))" +
                    "\\s+(([А-ЯЁ][а-яё]+)|([А-ЯЁ]\\.?))\\s*)");
            matcher = pattern.matcher(ticket.getHeadDepartment());
            if (matcher.find()) {
                strForm += matcher.group(2) + " " + matcher.group(3).charAt(0)
                        + ". " + matcher.group(6).charAt(0) + ".";
            }
            setConfig(p, ParagraphAlignment.RIGHT, strForm,
                    false, false);

            row = table.getRow(1);
            cell = row.getCell(0);
            p = cell.getParagraphs().get(0);
            p.setVerticalAlignment(TextAlignment.CENTER);
            setConfig(p, ParagraphAlignment.BOTH, "Преподаватель",
                    false, false);
            cell = row.getCell(1);
            p = cell.getParagraphs().get(0);
            p.setVerticalAlignment(TextAlignment.CENTER);
            strForm = "";
            matcher = pattern.matcher(ticket.getTeacher());
            if (matcher.find()) {
                strForm += matcher.group(2) + " " + matcher.group(3).charAt(0) +
                        ". " + matcher.group(6).charAt(0) + ".";
            }
            setConfig(p, ParagraphAlignment.RIGHT, strForm,
                    false, false);

            p = docxDes.createParagraph();
            text = "Утверждено на заседании кафедры ";
            setConfig(p, ParagraphAlignment.LEFT, text,
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

}
