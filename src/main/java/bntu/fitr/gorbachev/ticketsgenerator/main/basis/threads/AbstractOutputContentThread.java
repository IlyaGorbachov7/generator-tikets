package bntu.fitr.gorbachev.ticketsgenerator.main.basis.threads;

import bntu.fitr.gorbachev.ticketsgenerator.main.basis.QuestionExt;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.exceptions.OutputContentException;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.threads.tools.constants.TextPatterns;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.threads.tools.WrapXml;
import com.microsoft.schemas.office.word.STWrapType;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import bntu.fitr.gorbachev.ticketsgenerator.main.basis.Ticket;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.regex.Matcher;

/**
 * This class writes a list of tickets to the class object XWPFDocument.
 * <p>
 * Purpose thread is formation file represent class object {@link XWPFDocument}
 *
 * @author Gorbachev I. D.
 * @version 23.04.2022
 */
public abstract class AbstractOutputContentThread<T extends Ticket<? extends QuestionExt>>
        implements Callable<XWPFDocument> {

    protected List<T> listTickets;

    protected AbstractOutputContentThread() {
    }

    /**
     * Constructor without parameters
     *
     * @param listTickets list tickets
     */
    public AbstractOutputContentThread(List<T> listTickets) {
        this.listTickets = listTickets;
    }

    /**
     * @return list tickets
     */
    public List<T> getListTickets() {
        return listTickets;
    }

    public void setListTickets(List<T> listTickets) {
        this.listTickets = listTickets;
    }

    /**
     * Execution method
     *
     * @return map questions
     * @throws Exception in case general troubles
     */
    @Override
    public XWPFDocument call() throws OutputContentException {
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


            String strForm = "";
            Matcher matcher = TextPatterns.DATE_PATTERN.getMatcher(ticket.getDate());
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
                    try {
                        setParagraphProperties(desP);
                    } catch (XmlException e) {
                        throw new OutputContentException("setParagraphProperty", e);
                    }
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
            matcher = TextPatterns.PERSON_NAME_PATTERN_V2.getMatcher(ticket.getHeadDepartment());
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
            matcher = TextPatterns.PERSON_NAME_PATTERN_V2.getMatcher(ticket.getTeacher());
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

    // -------------------- Static context ------------------

    /**
     * If resource run contains picture, then copies all picture from resource run in destination run.
     *
     * @param resR resource run
     * @param desR destination run
     * @throws Exception in case general troubles
     */
    protected void clonePictureRun(XWPFRun resR, XWPFRun desR)
            throws Exception {
        // checking on the contains picture in main run that will by copy in created run
        List<XWPFPicture> listPictures = resR.getEmbeddedPictures();
        CTR resCTR = resR.getCTR();
        for (int indexDrawing = 0; indexDrawing < listPictures.size(); ++indexDrawing) {
            XWPFPicture picture = listPictures.get(indexDrawing);

            XWPFPictureData pictureData = picture.getPictureData();
            // add picture in early created run
            var inputStream = pictureData.getPackagePart().getInputStream();
            var pictureType = pictureData.getPictureType();
            var fileName = pictureData.getFileName();
            desR.addPicture(inputStream, pictureType, fileName,
                    Units.toEMU(picture.getWidth()), Units.toEMU(picture.getDepth()));

            inputStream.close();
            /*
            However also need replace layout added-picture on the same layout resource-picture
            So taken defined drawing from rscRun by index and check anchor-layout
            if will be false, then replace not need, because both run contains inline-layout
            */
            if (!(resCTR.getDrawingArray(indexDrawing).getAnchorList().isEmpty())) {
                 /*
                 if drawing consist anchor-layout then thue.
                 So need replace in early added picture layout on this anchor-layout
                 */
                CTAnchor rscAnchor = resCTR.getDrawingArray(indexDrawing).getAnchorArray(0);

                CTDrawing desDrawing = desR.getCTR().getDrawingArray(indexDrawing);
                CTInline desInline = desDrawing.getInlineArray(0);
                rscAnchor = getAnchorWithGraphicByTemplate(desInline.getGraphic(), rscAnchor);
                desDrawing.removeInline(0);
                desDrawing.setAnchorArray(new CTAnchor[]{rscAnchor});
            }
            ++indexDrawing;
        }
    }

    /**
     * Copies all properties: bold, italic, underline, color texture
     * <p>
     * <i>Clones the underlying w:rPr element</i>
     *
     * @param source source properties
     * @param dest   destination properties
     */
    protected void cloneRunProperties(XWPFRun source, XWPFRun dest) {
        CTRPr rPrSource = source.getCTR().getRPr();
        if (rPrSource == null) return;

        CTRPr rPrDest = (CTRPr) rPrSource.copy();
        CTR tRDest = dest.getCTR();
        tRDest.setRPr(rPrDest);
    }

    /**
     * Setting run properties according to standart requirement
     *
     * @param run run paragraph
     * @throws XmlException in case no correct format
     */
    protected void setRunProperties(XWPFRun run)
            throws XmlException {
        String prXml = "<w:rPr xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">" +
                       "                <w:rFonts w:ascii=\"Times New Roman\" w:hAnsi=\"Times New Roman\" w:cs=\"Times New Roman\"/>" +
                       "                <w:sz w:val=\"28\"/>" +
                       "                <w:szCs w:val=\"28\"/>" +
                       "                <w:lang w:val=\"en-US\"/>" +
                       "            </w:rPr>";

        CTRPr ctParaRPr = CTRPr.Factory.parse(prXml);
        run.getCTR().setRPr(ctParaRPr);
    }

    /**
     * Copy only property font family, font size, color.
     * <p>
     * <i>Speaking short  copies all properties: bold, italic, underline, color texture</i>
     * <p>
     * <i>
     * This will be needed for setting numbering list properties</i>/
     *
     * @param source source paragraph contains properties
     * @param dest   destination paragraph where will be setting configuration
     */
    protected void cloneParagraphProperties(XWPFParagraph source,
                                            XWPFParagraph dest) {
        CTP ctpSource = source.getCTP();
        if (ctpSource.getPPr() == null) return;
        if (ctpSource.getPPr().getRPr() == null) return;

        CTPPr ctpPr = ctpSource.getPPr();
        dest.getCTP().addNewPPr();
        CTParaRPr ctpPrSource = (CTParaRPr) ctpPr.getRPr().copy();
        dest.getCTP().getPPr().setRPr(ctpPrSource);

    }

    /**
     * Setting paragraph properties according to standard requirements
     * <p>
     * font size 14,
     * font family : "Times New Roman", paragraph alignment : both
     *
     * @param p paragraph
     * @throws XmlException in case no correct format
     */
    protected void setParagraphProperties(XWPFParagraph p)
            throws XmlException {
        CTPPr ctpPr = p.getCTP().getPPr();
        if (ctpPr == null) ctpPr = p.getCTP().addNewPPr();

        ctpPr.addNewJc().setVal(STJc.BOTH); // alignment paragraph
        // setting font
        String prXml = "<w:rPr xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">" +
                       "                <w:rFonts w:ascii=\"Times New Roman\" w:hAnsi=\"Times New Roman\" w:cs=\"Times New Roman\"/>" +
                       "                <w:sz w:val=\"28\"/>" +
                       "                <w:szCs w:val=\"28\"/>" +
                       "                <w:lang w:val=\"en-US\"/>" +
                       "            </w:rPr>";

        CTParaRPr ctParaRPr = CTParaRPr.Factory.parse(prXml);
        ctpPr.setRPr(ctParaRPr);
    }

    /**
     * The method establishes configuration paragraph and text content
     */
    protected void setConfig(XWPFParagraph p, ParagraphAlignment align,
                             String text, boolean capitalized, boolean bold) {
        p.setAlignment(align);
        XWPFRun config = p.createRun();
        setStandardPropForRun(config);
        config.setText(text);
        config.setCapitalized(capitalized);
        config.setBold(bold);
    }

    /**
     * This method will be overridden to define customer standart for XWPFRun property
     */
    protected void setStandardPropForRun(XWPFRun run) {
        run.setFontSize(14);
        run.setFontFamily("Times New Roman");
    }

    /**
     * This method sets a new decimal paragraph numeration
     *
     * @param document      docx file
     * @param abstractNumID current numbering id
     * @return new numbering id
     */
    protected BigInteger getNewDecimalNumberingId(XWPFDocument document,
                                                  BigInteger abstractNumID) {
        CTAbstractNum cTAbstractNum = CTAbstractNum.Factory.newInstance();
        cTAbstractNum.setAbstractNumId(abstractNumID);
        CTLvl cTLvl = cTAbstractNum.addNewLvl();
        cTLvl.addNewNumFmt().setVal(STNumberFormat.DECIMAL);
        cTLvl.addNewLvlText().setVal("%1)");
        cTLvl.addNewStart().setVal(BigInteger.valueOf(1));

        XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);

        XWPFNumbering numbering = document.createNumbering();

        abstractNumID = numbering.addAbstractNum(abstractNum);
        return numbering.addNum(abstractNumID);
    }

    /**
     * The method defines a new Anchor object  with graphic content using a template anchor
     *
     * @param graphicRsc     source of graphic data
     * @param anchorTemplate anchor template, based on which the properties of the created template are set
     *                       and also wrapping type
     * @return class object {@link CTAnchor}
     * @throws Exception in case general troubles
     */
    protected CTAnchor getAnchorWithGraphicByTemplate(CTGraphicalObject graphicRsc,
                                                      CTAnchor anchorTemplate) throws Exception {
        WrapXml wrapXml;
        if (!Objects.isNull(anchorTemplate.getWrapTight())) {
            wrapXml = WrapXml.getInstance(STWrapType.TIGHT, anchorTemplate.getWrapTight()
                    .getWrapText());
        } else if (!Objects.isNull(anchorTemplate.getWrapSquare())) {
            wrapXml = WrapXml.getInstance(STWrapType.SQUARE, anchorTemplate.getWrapSquare()
                    .getWrapText());
        } else if (!Objects.isNull(anchorTemplate.getWrapThrough())) {
            wrapXml = WrapXml.getInstance(STWrapType.THROUGH, anchorTemplate.getWrapThrough()
                    .getWrapText());
        } else if (!Objects.isNull(anchorTemplate.getWrapTopAndBottom())) {
            wrapXml = WrapXml.getInstance(STWrapType.TOP_AND_BOTTOM, null);
        } else wrapXml = WrapXml.getInstance(STWrapType.NONE, null);
        return getAnchorWithGraphicByWrapType(wrapXml, graphicRsc, anchorTemplate);
    }

    /**
     * The method defines a new Anchor object  with graphic content by wrapping type
     *
     * @param wrapXml        the object contains xml resources
     * @param graphicRsc     source of graphic data
     * @param anchorTemplate anchor template, based on which the properties of the created template are set
     * @return new anchor
     * @throws Exception in case general troubles
     */
    protected CTAnchor getAnchorWithGraphicByWrapType(WrapXml wrapXml,
                                                      CTGraphicalObject graphicRsc,
                                                      CTAnchor anchorTemplate) throws Exception {

        String drawingDescr = anchorTemplate.getDocPr().getDescr();
        long distL = anchorTemplate.getDistL();
        long distT = anchorTemplate.getDistT();
        long distR = anchorTemplate.getDistR();
        long distB = anchorTemplate.getDistB();
        long relativeH = anchorTemplate.getRelativeHeight();
        int left = anchorTemplate.getPositionH().getPosOffset();
        int top = anchorTemplate.getPositionV().getPosOffset();
        long width = anchorTemplate.getExtent().getCx();
        long height = anchorTemplate.getExtent().getCy();
        Object l = anchorTemplate.getEffectExtent().getL();
        Object t = anchorTemplate.getEffectExtent().getT();
        Object r = anchorTemplate.getEffectExtent().getR();
        Object b = anchorTemplate.getEffectExtent().getB();
        // решает ЗА текстом или ПЕРЕД текстом
        boolean behindAnchor = anchorTemplate.getBehindDoc();

        String anchorXML = "<wp:anchor xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
                           + "distT=\"" + distT + "\" distB=\"" + distB + "\" distL=\"" + distL + "\" distR=\"" + distR + "\" simplePos=\"0\" relativeHeight=\""
                           + relativeH + "\" behindDoc=\"" + behindAnchor + "\" locked=\"0\" layoutInCell=\"1\" allowOverlap=\"1\">"
                           + "<wp:simplePos x=\"0\" y=\"0\"/>"
                           + "<wp:positionH relativeFrom=\"column\"><wp:posOffset>" + left + "</wp:posOffset></wp:positionH>"
                           + "<wp:positionV relativeFrom=\"paragraph\"><wp:posOffset>" + top + "</wp:posOffset></wp:positionV>"
                           + "<wp:extent cx=\"" + width + "\" cy=\"" + height + "\"/>"
                           + "<wp:effectExtent l=\"" + l + "\" t=\"" + t + "\" r=\"" + r + "\" b=\"" + b + "\"/>"
                           + wrapXml
                           + "<wp:docPr id=\"" + "1" + "\" name=\"Drawing " + "1" + "\" descr=\"" + drawingDescr + "\"/>"
                           + "<wp:cNvGraphicFramePr/>"
                           + "</wp:anchor>";

        CTDrawing drawing = CTDrawing.Factory.parse(anchorXML);
        CTAnchor anchor = drawing.getAnchorArray(0);
        anchor.setGraphic(graphicRsc);
        return anchor;
    }
}
