package bntu.fitr.gorbachev.ticketsgenerator.main.threads;

import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.InvalidLexicalException;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.PreparerPatterns;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.attributes.AttributeService;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.attributes.impl.ListTagAttributeService;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.constants.TagPatterns;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.constants.LexicalPatterns;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.QuestionExt;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.ContentExtractException;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.stream.Collectors;


public abstract class AbstractContentExtractThread<T extends QuestionExt>
        implements Callable<List<T>> {

    protected XWPFDocument docxFile;
    protected String urlDocxFile;
    protected Supplier<T> supplierQuestion;

    protected AbstractContentExtractThread() {
    }

    /**
     * Constructor without parameters
     *
     * @param docxFile    class object {@link XWPFDocument} constants list questions
     * @param urlDocxFile url file
     */
    public AbstractContentExtractThread(XWPFDocument docxFile, String urlDocxFile) {
        this.docxFile = docxFile;
        this.urlDocxFile = urlDocxFile;
        this.supplierQuestion = factoryQuestion();
    }

    /**
     * @return class object {@link XWPFDocument}
     */
    public XWPFDocument getDocxFile() {
        return docxFile;
    }

    /**
     * @return string file url
     */
    public String urlDocxFile() {
        return urlDocxFile;
    }

    public Supplier<T> getSupplierQuestion() {
        return supplierQuestion;
    }

    public void setDocxFile(XWPFDocument docxFile) {
        this.docxFile = docxFile;
    }

    public void setUrlDocxFile(String urlDocxFile) {
        this.urlDocxFile = urlDocxFile;
    }

    public void setSupplierQuestion(Supplier<T> supplierQuestion) {
        this.supplierQuestion = supplierQuestion;
    }

    /**
     * Execution method
     *
     * @return map questions
     * @throws Exception in case general troubles
     */
    @Override
    public List<T> call() throws ContentExtractException {
        List<T> listQuestions = new ArrayList<>();

        var paragraphs = docxFile.getParagraphs();
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
                AttributeService attributeTag;
                try {
                    attributeTag = extractAttributesFromListStartTag(curP);
                } catch (InvalidLexicalException e) {
                    throw new ContentExtractException(e.getMessage());
                }

                i++;
                while (i < paragraphs.size() && isNumbering(curP = paragraphs.get(i))) { // running by one topic
                    T ques = supplierQuestion.get();// 1 question - can contain picture or math-expressions
                    fillerQuestionFields(ques, attributeTag);
                    ques.add(curP);

                    int j = i + 1;
                    while (j < paragraphs.size() && (!isNumbering(curP = paragraphs.get(j)) && !isEndTag(curP))) { // running by one questions
                        if (isListStartTag(curP)) { // required condition
                            throw new ContentExtractException(urlDocxFile +
                                                              "\nBy reading content of the question no found end tag : <\\S>");
                        }
                        ques.add(curP);
                        ++j;
                    }

                    i = j; // then update index, point on the
                    listQuestions.add(ques);
                }
            }
        }
        return listQuestions;
    }

    /**
     * This method represented yourself as <i>factory </i> to implementation {@code some class extends Question }
     *
     * @return supplier class, that supply a class extends {@link QuestionExt}
     */
    protected abstract Supplier<T> factoryQuestion();

    // -------------------- Static context ------------------

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
                                                  "\n No find  end tag : <\\S>");
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
    protected boolean isNumbering(XWPFParagraph p) {
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
    protected boolean isListStartTag(XWPFParagraph p) {
        if (!checkTagSCondition(p)) return false;
        String s = p.getText();
        return TagPatterns.LIST_START_TAG.matches(s);
    }

    /**
     * <b>If paragraph is not a start tag, then throw Exception: InvalidLexicalException</b>
     * <p>
     * if paragraph is start tag, then this method extract attributes if they exist inside tag
     * and packaging their into object a class {@link ListTagAttributeService}
     *
     * @return object a class ListTagAttributeService
     * @throws InvalidLexicalException  a lexical error was made when reading attributes
     * @throws IllegalArgumentException if paragraph is not start tag. <b>This method await, that
     *                                  paragraph contain start teg</b>
     */
    protected AttributeService extractAttributesFromListStartTag(XWPFParagraph p) throws InvalidLexicalException {
        if (!isListStartTag(p)) throw new IllegalArgumentException("paragraph on exist start tag");

        String attributes = null;
        Matcher matcher = TagPatterns.LIST_START_TAG.getMatcher(p.getText());
        while (matcher.find()) {
            attributes = matcher.group(2);
        }
        if (attributes == null) { // if <S>
            attributes = "";
        }
        return extractAndFill(attributes, ListTagAttributeService.class);
    }

    /**
     * The method checks whether the paragraph is a end tag
     * <p>
     * End tag : <b>{@code <\S>}</b>
     *
     * @param p class object {@link XWPFParagraph}
     * @return true is end tag else false
     */
    protected boolean isEndTag(XWPFParagraph p) {
        if (!checkTagSCondition(p)) return false;
        String s = p.getText();
        return TagPatterns.LIST_END_TAG.matches(s);
    }

    /**
     * Search for a paragraph in file containing the end tag
     *
     * @param curPara current paragraph is beginning search
     * @return index paragraph containing the end tag or -1 if search is failed
     */
    protected int searchParaEndTag(XWPFParagraph curPara) {
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
    protected int searchParaStartTag(XWPFParagraph curPara) {
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
            if (isListStartTag(listP.get(curPosPara))) return curPosPara;
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
    protected boolean checkTagSCondition(XWPFParagraph p) {
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


    /**
     * This method full question fields of values, which are supplied by the object implementing {@link AttributeService}
     *
     * @param quest   object question
     * @param service object implements interface {@link AttributeService}. <b>This object belong to class,
     *                that was wrote inside {@code method}: {@link #extractAttributesFromListStartTag(XWPFParagraph)}</b>
     * @apiNote To receive the fields from some object implementing SomeServiceAttributes, you need explicit converting type
     */
    protected void fillerQuestionFields(T quest, AttributeService service) {
        if (service instanceof ListTagAttributeService attributesListStartTag) {
            quest.setSection(attributesListStartTag.getN());
            quest.setLevel(attributesListStartTag.getL());
            quest.setRepeat(attributesListStartTag.getR());
        }
    }

    /**
     * This method will be extract string attributes from  string, names which describes in the specified <i>clazz</i>
     *
     * @param strAttributes string, which content value with given attributes
     * @param clazz         should implement interface {@link AttributeService}
     * @return Filled class object, class which given into the method parameter
     * @throws InvalidLexicalException in case lexical mistake
     * @apiNote Attribute with name: <b>class</b>  is reserved. Java already contains hidden field with name : <b>class</b>
     * @see LexicalPatterns
     */
    public static AttributeService extractAndFill(String strAttributes, Class<? extends AttributeService> clazz)
            throws InvalidLexicalException {
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors(); // all property a class
        Arrays.stream(pds).map(PropertyDescriptor::getName).filter(attribute -> !attribute.equals("class")).forEach(System.out::println);
        System.out.println("--Attributes was generation from class: " + clazz);
        List<String> attributes = Arrays.stream(pds).map(PropertyDescriptor::getName)
                .filter(attribute -> !attribute.equals("class"))
                .collect(Collectors.toList());
        return extractAndFill(strAttributes, attributes, clazz);
    }

    /**
     * This method will be extract attributes from string, names which describes in the specified <i>clazz</i>
     *
     * @param attributes    list of attributes names, which you are want extract from string
     * @param strAttributes string, which content value with given attributes
     * @param clazz         should implement interface {@link AttributeService}
     * @return Filled class object, class which given into the method parameter
     * @throws InvalidLexicalException in case lexical mistake
     * @apiNote 1) Name attributes, which specified in the list of attributes, must have names matched with field names
     * given <i>clazz</i>, where will be written values mapped with attributes.
     * <p>
     * 2) Attribute with name: <b>class</b>  is reserved. Java already contains hidden field with name : <b>class</b>
     * @see LexicalPatterns
     */
    public static AttributeService extractAndFill(String strAttributes, List<String> attributes, Class<? extends AttributeService> clazz)
            throws InvalidLexicalException {
        AttributeService object;
        try {
            object = clazz.getDeclaredConstructor().newInstance();
            System.out.println(object.getClass());
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors(); // all property a class
        List<String> nameFields = Arrays.stream(pds).map(PropertyDescriptor::getName).toList(); // names field from methods
        System.out.println("field: " + nameFields);
        System.out.println("attribute: " + attributes);
        if (!nameFields.containsAll(attributes)) { // checking contain needed whether fields inside specified class
            throw new RuntimeException("Some specified attributes: " + attributes
                                       + " absent, in given class: " + clazz.getName() + " as field name");
        }

        // if success, then continue work
        HashMap<String, Method> setterMethods = new HashMap<>();
        for (PropertyDescriptor pd : pds) {
            if (attributes.contains(pd.getName())) { // take only methods, which have named specified given attributes
                System.out.println(pd.getName());
                Method mw = pd.getWriteMethod();
                if (mw != null) { // if exist setter for attribute bean object
                    setterMethods.put(pd.getName(), mw);
                } else {
                    throw new RuntimeException("attribute : " +
                                               pd.getName() + " no have setter");
                }
            }
        }

        setterMethods.forEach((k, m) -> System.out.println("name: " + k + " method: " + m));

        executeExtracting(strAttributes, setterMethods, object);
        return object;
    }

    private static void executeExtracting(String strAttributes, Map<String, Method> setterMethods, Object obj)
            throws InvalidLexicalException {
        for (var entry : setterMethods.entrySet()) {
            String attribute = entry.getKey();
            int index;
            String str = strAttributes;
            while (!((index = str.indexOf(attribute)) < 0)) {
                boolean cutStr = false;
                if (index > 0) { // if: ->avarage=23; a= 11; age=12; || ->avarage=23; a= 11;age=12;
                    // avarage и age имеют корень age
                    if (str.charAt(index - 1) != ' ' && str.charAt(index - 1) != ';') {
                        str = str.substring(++index);
                        continue;
                    }
                }
                int j = index + attribute.length();
                while (j < str.length() && str.charAt(j) != '=') {
                    if (str.charAt(j) != ' ') {
                        cutStr = true;
                        str = str.substring(j);
                        break;
                    }
                    ++j;
                }
                if (j < str.length() &&
                    !cutStr && str.charAt(j) == '=') {
                    break;
                } else if (j == str.length()) {
                    // if any attribute is at then end of the string, then make: str = "";
                    str = str.substring(j);
                }
            }

            if (index >= 0) {
                String someAttrib = str.substring(index + attribute.length()); // starting with: =  233
                Matcher matcher = definerMatcher(entry.getValue()).getMatcher(someAttrib);
                String value = null;
                while (matcher.find()) {
                    value = matcher.group(1);
                    break;
                }
                if (value == null) {
                    throw new InvalidLexicalException("Lexical mistake attribute: " + strAttributes + "\n" +
                                                      "Awaiting:\n" +
                                                      "    or symbol ';'\n" +
                                                      "    or data type: " + entry.getValue()
                                                              .getParameterTypes()[0].getSimpleName() + "\n" +
                                                      "    or if data type is number,\n" +
                                                      "      then it value should be no more than 99.99\n" +
                                                      "Make sure that the attribute was entered correctly");
                }
                Class<?> clazzParam = entry.getValue()
                        .getParameterTypes()[0];
                Object objParam = converterStringToPrimitiveType(value, clazzParam);
                try {
                    entry.getValue().invoke(obj, objParam);
                } catch (IllegalAccessException | InvocationTargetException e) { // инфа для программиста
                    e.printStackTrace();
                }

            }
        }
    }

    private static PreparerPatterns definerMatcher(Method method) {
        Parameter[] parameters = method.getParameters();
        if (parameters.length == 0) throw new RuntimeException("setter: " + method + " without parameters !");
        if (parameters.length > 1)
            throw new RuntimeException("setter" + method + " having more, then one parameters !");

        Class<?> clazzParam = parameters[0].getType();
        if (clazzParam.getSuperclass() == Number.class || clazzParam == int.class || clazzParam == double.class
            || clazzParam == float.class || clazzParam == short.class) {
            return LexicalPatterns.NUMBER_REGEX;
        } else if (clazzParam == String.class || clazzParam == boolean.class || clazzParam == Boolean.class) {
            return LexicalPatterns.STRING_REGEX;
        }
        throw new RuntimeException("setter with such parameter : " + clazzParam + " does not support");
    }

    private static Object converterStringToPrimitiveType(String value, Class<?> convertType)
            throws InvalidLexicalException {
        try {
            if (convertType == int.class || convertType == Integer.class) {
                return Integer.parseInt(value);
            } else if (convertType == double.class || convertType == Double.class) {
                return Double.parseDouble(value);
            } else if (convertType == float.class || convertType == Float.class) {
                return Float.parseFloat(value);
            } else if (convertType == short.class || convertType == Short.class) {
                return Short.parseShort(value);
            } else if (convertType == boolean.class || convertType == Boolean.class) {
                return Boolean.parseBoolean(value);
            } else if (convertType == String.class) {
                return value;
            }
        } catch (NumberFormatException e) {// это должен знать только программист!
            throw new InvalidLexicalException("value:" + value + " is not " + convertType.getSimpleName());
        }
        // об это должен знать только программист
        throw new RuntimeException("clazz : " + convertType + " does not support");
    }
}
