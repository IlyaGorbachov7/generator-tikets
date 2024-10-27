package bntu.fitr.gorbachev.ticketsgenerator.main.basis.threads.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.basis.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.QuestionExt;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.WriterTicketProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.exceptions.OutputContentException;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.threads.AbstractOutputContentThread;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.threads.tools.constants.TextPatterns;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.Localizer;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

public class OutputContentWriter extends AbstractOutputContentThread<Ticket<Question2>> {
    protected WriterTicketProperty property;
    protected int twipsPerInch = 1440;

    protected int PAGE_W = 12240;   //12240 Twips = 12240/20 = 612 pt = 612/72 = 8.5"
    protected int PAGE_H = 15840;   //15840 Twips = 15840/20 = 792 pt = 792/72 = 11"
    protected int PAGE_TOP = (int) (0.1968 * twipsPerInch); // 0.5 см
    protected int PAGE_BOTTOM = (int) (0.2952 * twipsPerInch); // 0.75 см

    protected int INDENTATION_LEFT = (int) (-0.4 * twipsPerInch);

    protected int SPACING_BEFORE_0p = 0;
    protected int SPACING_BEFORE_1p = 20; // отступ 1п в доке -> 1п/0.05 = 20

    protected int SPACING_AFTER_0p = 0; // 0
    protected int SPACING_AFTER_1p = 20; // отступ 1п в доке -> 1п/0.05 = 20

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

    public OutputContentWriter(List<Ticket<Question2>> listTickets, WriterTicketProperty writerTicketProperty) {
        super(listTickets);
        property = Objects.requireNonNullElse(writerTicketProperty, new WriterTicketProperty());
    }

    @Override
    public List<Ticket<Question2>> getListTickets() {
        return super.getListTickets();
    }

    @Override
    public XWPFDocument call() throws OutputContentException {
        XWPFDocument docxDes = new XWPFDocument();
        int quantityTicketsOnSinglePage = property.getQuantityOnSinglePage();

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
            boolean isPresentText = false;
            var text = ticket.getInstitute();
            if (property.isIncludeUniversity()) {
                createPara(text, docxDes, INDENTATION_LEFT, SPACING_BEFORE_0p, SPACING_AFTER_0p,
                        ParagraphAlignment.CENTER, true, true);
                isPresentText = true;
            }

            text = ticket.getFaculty();
            if (property.isIncludeFaculty()) {
                createPara(text, docxDes, INDENTATION_LEFT, SPACING_BEFORE_0p, SPACING_AFTER_0p,
                        ParagraphAlignment.CENTER, false, true);
                isPresentText = true;
            }

            text = ticket.getDepartment();
            if (property.isIncludeDepartment()) {
                createPara(text, docxDes, INDENTATION_LEFT, SPACING_BEFORE_0p, SPACING_AFTER_0p,
                        ParagraphAlignment.CENTER, false, false);
                /*.setBorderBottom((!property.isIncludeSpecialization()) ? Borders.SINGLE : Borders.NONE);*/
                isPresentText = true;
            }

            // not necessary or necessary
            text = ticket.getSpecialization();
            if (property.isIncludeSpecialization()) {
                createPara(text, docxDes, INDENTATION_LEFT, SPACING_BEFORE_0p, SPACING_AFTER_0p,
                        ParagraphAlignment.CENTER, false, false);
                /*.setBorderBottom(Borders.SINGLE);*/
                isPresentText = true;
            }
            // this is line
/*            if (isPresentText) { // if present at least one text, then added spacing after line
                docxDes.createParagraph().setSpacingAfter(SPACING_AFTER_0p);
            }*/

            if (property.isExam()) {
                text = Localizer.getWithValues("output.ticket-exam", String.valueOf((iter + 1)));
            } else {
                text = Localizer.getWithValues("output.ticket", String.valueOf(iter + 1));
            }
            createPara(text, docxDes, INDENTATION_LEFT, SPACING_BEFORE_0p, SPACING_AFTER_0p,
                    ParagraphAlignment.CENTER, true, true);
            XWPFParagraph p = null;
            if (property.isIncludeDiscipline()) {
                text = Localizer.getWithValues("output.discipline", ticket.getDiscipline());
                p = createPara(text, docxDes, INDENTATION_LEFT, SPACING_BEFORE_0p, SPACING_AFTER_0p,
                        ParagraphAlignment.CENTER, false, false);
//                setConfig(p, ParagraphAlignment.CENTER, text, false, false);
            }


            String strForm = "";
            Matcher matcher = TextPatterns.DATE_PATTERN.getMatcher(ticket.getDate());
            if (matcher.find()) {
                strForm = matcher.group(3);
                strForm += (ticket.getType() == Ticket.SessionType.WINTER) ?
                        ("/" + (Integer.parseInt(strForm) + 1)) : "";
            }
            if (property.isIncludeSessionType()) {
                text = Localizer.getWithValues("output.session-type", ticket.getType().toString(), strForm);
                createPara(text, docxDes, INDENTATION_LEFT, SPACING_BEFORE_0p, SPACING_AFTER_0p, ParagraphAlignment.CENTER,
                        false, false);
            }


            BigInteger newNumID = getNewDecimalNumberingId(docxDes, BigInteger.valueOf(iter++));
            createListParaQuestions(docxDes, ticket, newNumID);

            createTable(docxDes, ticket);


            if (property.isIncludeProtocol()) {
                text = Localizer.get("output.approval");
                p = createPara(text, docxDes, INDENTATION_LEFT, SPACING_BEFORE_0p, SPACING_AFTER_1p, ParagraphAlignment.BOTH,
                        false, false);
                text = ticket.getDate() + ", ";
                setConfig(p, ParagraphAlignment.LEFT, text,
                        false, true);
                text = Localizer.getWithValues("output.protocol", ticket.getProtocolNumber());
                setConfig(p, ParagraphAlignment.LEFT, text,
                        false, true);
            }
            if (iter < listTickets.size()) {
                if(p == null) {
                    p = createPara("", docxDes, INDENTATION_LEFT, SPACING_BEFORE_0p, SPACING_AFTER_1p, ParagraphAlignment.BOTH, false, false);
                }
                if (quantityTicketsOnSinglePage <= 1 || property.isTicketOnSinglePage()) {
                    quantityTicketsOnSinglePage = property.getQuantityOnSinglePage();

                    p.createRun().addBreak(BreakType.PAGE);
                } else {
                    p = docxDes.createParagraph();
                    p.setIndentationLeft((int) (-1.1 * twipsPerInch));
                    p.setIndentationRight((int) (((BigInteger) pageSz.getW()).intValue() * 1.1 * twipsPerInch));
                    p.setBorderTop(Borders.SOUTHWEST);
                    --quantityTicketsOnSinglePage;
                }
            }
        }
        return docxDes;
    }

    /**
     * Override method to set font size for created XWPFRun
     */
    @Override
    protected void setConfig(XWPFParagraph p, ParagraphAlignment align, String text, boolean capitalized, boolean bold) {
        super.setConfig(p, align, text, capitalized, bold);
        var list = p.getRuns();
        if (!list.isEmpty()) { // getting XWPFRun, which is last and setting font size from property
            list.get(list.size() - 1).setFontSize(property.getSizeFont());
        }
    }

    @Override
    protected void setStandardPropForRun(XWPFRun run) {
        super.setStandardPropForRun(run);
        run.setFontSize(property.getSizeFont());
    }

    protected XWPFParagraph createPara(String text, XWPFDocument docxDes, int indentationLeft,
                                       int spacingBefore, int spacingAfter, ParagraphAlignment alignment,
                                       boolean capitalized, boolean bold) {
        var newP = docxDes.createParagraph();
        newP.setIndentationLeft(indentationLeft);
        newP.setSpacingAfter(spacingAfter);
        newP.setSpacingBefore(spacingBefore);
        setConfig(newP, alignment, text, capitalized, bold);
        return newP;
    }

    protected void createListParaQuestions(XWPFDocument docxDes,
                                           Ticket<? extends QuestionExt> ticket, BigInteger newNumID)
            throws OutputContentException {
        int indexQuestion = 0;
        int sizeQuestions = ticket.getQuestions().size();
        for (var question : ticket.getQuestions()) {
            int indexParagraph = 0;
            for (var resP : question.getListParagraphs()) {
                var desP = docxDes.createParagraph();
                if (indexQuestion == 0) {
                    desP.setSpacingBefore(SPACING_BEFORE_1p * 10);
                    desP.setSpacingAfter(SPACING_AFTER_1p * 3);
                } else if (indexQuestion == sizeQuestions - 1) {
                    desP.setSpacingAfter(SPACING_AFTER_1p * 10);
                } else {
                    desP.setSpacingAfter(SPACING_AFTER_1p * 3);
                }
                try {
                    setParagraphProperties(desP);
                } catch (XmlException e) {
                    throw new OutputContentException("setParagraphProperty", e);
                }
                if (resP.getNumID() != null) {
                    desP.setNumID(newNumID);
                    if (!desP.getRuns().isEmpty()) {
                        desP.getRuns().forEach(this::setStandardPropForRun);
                    }
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
                            setStandardPropForRun(desR);

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
                            ctoMath = getNewModifiedCTOMath(ctoMath);
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
                ++indexParagraph;
            }
            ++indexQuestion;
        }
    }

    /**
     * This method replace <b>ctoMath</b> object on the new modified object
     * <p>
     * This setting size font in math expression according to value giving from {@code WriterTicketProperty}
     */
    protected CTOMath getNewModifiedCTOMath(CTOMath ctoMath) {
        try {
            String xmlMathString = ctoMath.xmlText().replaceAll("w:sz w:val=\"\\d*\"",
                    String.format("w:sz w:val=\"%d\"", property.getSizeFont() * 2));
            ctoMath = CTOMath.Factory.parse(xmlMathString);
        } catch (XmlException ignore) {
        }
        return ctoMath;
    }

    protected void createTable(XWPFDocument docxDes, Ticket<? extends QuestionExt> ticket) {
        if (!property.isIncludeHeadDepartment() && !property.isIncludeTeacher()) {
            return;
        }
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
        row.getCell(0).setWidth(String.valueOf(WIDTH_CALL_1)); // width == 4 дюйма == 1 см
        row.getCell(1).setWidth(String.valueOf(WIDTH_CALL_2)); // 3 дюйма = 8 см

        if (property.isIncludeHeadDepartment()) {
            setHeadDepartment(cell, ticket);
        }
        if (property.isIncludeTeacher()) {
            cell = row.getCell(property.isIncludeHeadDepartment() ? 1 : 0);
            setTeacher(cell, ticket);
        }
    }

    private void setHeadDepartment(XWPFTableCell cell, Ticket<? extends QuestionExt> ticket) {
        var p = cell.getParagraphs().get(0);
        p.setSpacingAfter(SPACING_AFTER_0p);
        p.setVerticalAlignment(TextAlignment.CENTER);
        setConfig(p, ParagraphAlignment.BOTH, Localizer.get("output.head-dep"),
                false, false);
        String strForm = "";
        Matcher matcher = TextPatterns.PERSON_NAME_PATTERN_V2.getMatcher(ticket.getHeadDepartment());
        if (matcher.find()) {
            strForm += matcher.group(2) + " " + matcher.group(3).charAt(0)
                       + ". " + matcher.group(6).charAt(0) + ".";
        }
        if (strForm.isEmpty()) {
            strForm = ticket.getHeadDepartment();
        }
        setConfig(p, ParagraphAlignment.LEFT, strForm,
                false, false);

    }

    protected void setTeacher(XWPFTableCell cell, Ticket<? extends QuestionExt> ticket) {
        var p = cell.getParagraphs().get(0);
        p.setSpacingAfter(SPACING_AFTER_0p);
        p.setVerticalAlignment(TextAlignment.CENTER);
        setConfig(p, ParagraphAlignment.BOTH, Localizer.get("output.teacher"),
                false, false);
        String strForm = "";
        Matcher matcher = TextPatterns.PERSON_NAME_PATTERN_V2.getMatcher(ticket.getTeacher());
        if (matcher.find()) {
            strForm += matcher.group(2) + " " + matcher.group(3).charAt(0) +
                       ". " + matcher.group(6).charAt(0) + ".";
        }
        if (strForm.isEmpty()) {
            strForm = ticket.getHeadDepartment();
        }
        setConfig(p, ParagraphAlignment.LEFT, strForm,
                false, false);
    }
}
