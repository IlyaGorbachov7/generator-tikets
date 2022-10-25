package bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools;

import com.microsoft.schemas.office.word.STWrapType;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

public class OutputDefinerParaProperties {

    /**
     * If resource run contains picture, then copies all picture from resource run in destination run.
     *
     * @param resR resource run
     * @param desR destination run
     * @throws Exception in case general troubles
     */
    public static void clonePictureRun(XWPFRun resR, XWPFRun desR)
            throws Exception {
        // checking on the contains picture in main run that will by copy in created run
        List<XWPFPicture> listPictures = resR.getEmbeddedPictures();
        CTR resCTR = resR.getCTR();
        for (int indexDrawing = 0; indexDrawing < listPictures.size(); ++indexDrawing) {
            XWPFPicture picture = listPictures.get(indexDrawing);

            XWPFPictureData pictureData = picture.getPictureData();
            // add picture in early created run
            desR.addPicture(pictureData.getPackagePart().getInputStream(),
                    pictureData.getPictureType(), pictureData.getFileName(),
                    Units.toEMU(picture.getWidth()), Units.toEMU(picture.getDepth()));

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
    public static void cloneRunProperties(XWPFRun source, XWPFRun dest) {
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
    public static void setRunProperties(XWPFRun run)
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
    public static void cloneParagraphProperties(XWPFParagraph source,
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
    public static void setParagraphProperties(XWPFParagraph p)
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
    public static void setConfig(XWPFParagraph p, ParagraphAlignment align,
                                  String text, boolean capitalized, boolean bold) {
        p.setAlignment(align);
        XWPFRun config = p.createRun();
        config.setFontFamily("Times New Roman");
        config.setFontSize(14);
        config.setText(text);
        config.setCapitalized(capitalized);
        config.setBold(bold);
    }

    /**
     * This method sets a new decimal paragraph numeration
     *
     * @param document      docx file
     * @param abstractNumID current numbering id
     * @return new numbering id
     */
    public static BigInteger getNewDecimalNumberingId(XWPFDocument document,
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
    public static CTAnchor getAnchorWithGraphicByTemplate(CTGraphicalObject graphicRsc,
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
    public static CTAnchor getAnchorWithGraphicByWrapType(WrapXml wrapXml,
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
