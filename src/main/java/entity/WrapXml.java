package entity;

import com.microsoft.schemas.office.word.STWrapType;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapText;

/**
 * The class needed for more easy work xml format also
 * goals shorting repeated code.
 *
 * @author Gorbachev I. D.
 * @version 04.04.2022
 */
public final class WrapXml {
    /**
     * Single instance of the class
     */
    private static final WrapXml instance = new WrapXml();

    private String xmlString;
    private STWrapType.Enum wrapType;
    private STWrapText.Enum wrapText;

    /**
     * Constructor without parameters
     */
    private WrapXml() {
    }

    /**
     * @return wrap type
     */
    public STWrapType.Enum getWrapType() {
        return wrapType;
    }

    /**
     * @return wrap text
     */
    public STWrapText.Enum getWrapText() {
        return wrapText;
    }

    /**
     * Update needed parameters single instance of the class
     *
     * @param wrapType  wrap type
     * @param wrapText  wrap text
     * @param xmlString xml represent string
     * @return object this class
     */
    private WrapXml update(STWrapType.Enum wrapType,
                           STWrapText.Enum wrapText,
                           String xmlString) {
        this.wrapType = wrapType;
        this.wrapText = wrapText;
        this.xmlString = xmlString;
        return this;
    }

    /**
     * @return represent object as a string
     */
    @Override
    public synchronized String toString() {
        return xmlString;
    }

    /**
     * The method for retrieve instance of the class
     *
     * @param wrapType wrap type
     * @param wrapText wrap text
     * @return instance of the class
     */
    public static synchronized WrapXml getInstance(STWrapType.Enum wrapType,
                                                   STWrapText.Enum wrapText) {
        if (wrapType == null)
            throw new NullPointerException("wrapType="
                                           + null +
                                           " => the value must not be null");
        if ((wrapType == STWrapType.TIGHT ||
             wrapType == STWrapType.SQUARE ||
             wrapType == STWrapType.THROUGH)
            && wrapText == null) {
            throw new IllegalArgumentException("wrapType : "
                                               + wrapType +
                                               "not compatible wrapType : "
                                               + wrapType);
        }
        String xmlString = "";
        if (wrapType == STWrapType.TIGHT) {
            xmlString = "<wp:wrapTight wrapText=\"" + wrapText + "\">"
                        + "<wp:wrapPolygon edited=\"0\">"
                        + "<wp:start x=\"0\" y=\"0\"/>"
                        + "<wp:lineTo x=\"0\" y=\"21600\"/>"
                        + "<wp:lineTo x=\"21600\" y=\"21600\"/>"
                        + "<wp:lineTo x=\"21600\" y=\"0\"/>"
                        + "<wp:lineTo x=\"0\" y=\"0\"/>"
                        + "</wp:wrapPolygon>"
                        + "</wp:wrapTight>";
        } else if (wrapType == STWrapType.SQUARE) {
            xmlString = "<wp:wrapSquare wrapText=\"" + wrapText + "\"/>";
        } else if (wrapType == STWrapType.THROUGH) {
            xmlString = "<wp:wrapThrough wrapText=\"" + wrapText + "\">"
                        + "<wp:wrapPolygon edited=\"0\">"
                        + "<wp:start x=\"0\" y=\"0\"/>"
                        + "<wp:lineTo x=\"0\" y=\"21600\"/>"
                        + "<wp:lineTo x=\"21600\" y=\"21600\"/>"
                        + "<wp:lineTo x=\"21600\" y=\"0\"/>"
                        + "<wp:lineTo x=\"0\" y=\"0\"/>"
                        + "</wp:wrapPolygon>"
                        + "</wp:wrapThrough>";
        } else if (wrapType == STWrapType.TOP_AND_BOTTOM) {
            xmlString = "<wp:wrapTopAndBottom/>";
        } else if (wrapType == STWrapType.NONE) {
            xmlString = "<wp:wrapNone/>";
        }
        return instance.update(wrapType, wrapText, xmlString);
    }

}