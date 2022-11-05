package bntu.fitr.gorbachev.ticketsgenerator.main.test;

import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.InvalidLexicalException;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.AttributeAny;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.AttributeTag;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.AttributeTagsPatterns;

import java.beans.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Matcher;

public class Test5 {
    public static void main(String[] args) throws InvalidLexicalException {
        test2();
//        test1();
    }

    static void test1() throws InvalidLexicalException {
        Object attributTagObject = extractAndFullingDTO("k=23; r=11;n=Bkmz;", Arrays.asList("k", "l", "n"), BeanObj.class);

        System.out.println(attributTagObject);

        attributTagObject = extractAndFullingDTO("k=23; r=11;n=Bkmz;", Arrays.asList("n", "r", "l"), AttributeTag.class);

        System.out.println(attributTagObject);
    }

    static void test2() throws InvalidLexicalException {
        Object attrib = extractAndFullingDTO("avarage=23; a= 11;age=12;",
                Arrays.asList("name", "age", "avarage"),
                AttributeAny.class);
        System.out.println(attrib);
    }


    static Object extractAndFullingDTO(String strAttributes, List<String> attributes, Class<?> clazz) throws InvalidLexicalException {
        Object object;
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
        System.out.println(nameFields);
        System.out.println(attributes);
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

        execute(strAttributes, setterMethods, object);
        return object;
    }

    static void execute(String strAttributes, Map<String, Method> setterMethods, Object obj)
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
                    throw new InvalidLexicalException("Lexical mistake attribute: " + someAttrib + ". Awaiting: " +
                                                      entry.getValue().getParameterTypes()[0].getSimpleName());
                }
                Class<?> clazzParam = entry.getValue()
                        .getParameterTypes()[0];
                Object objParam = convertStringToPrimitiveType(value, clazzParam);
                try {
                    entry.getValue().invoke(obj, objParam);
                } catch (IllegalAccessException | InvocationTargetException e) { // инфа для программиста
                    e.printStackTrace();
                }

            }

        }

    }

    private static AttributeTagsPatterns definerMatcher(Method method) {
        Parameter[] parameters = method.getParameters();
        if (parameters.length == 0) throw new RuntimeException("setter: " + method + " without parameters !");
        if (parameters.length > 1)
            throw new RuntimeException("setter" + method + " having more, then one parameters !");

        Class<?> clazzParam = parameters[0].getType();
        if (clazzParam.getSuperclass() == Number.class || clazzParam == int.class || clazzParam == double.class
            || clazzParam == float.class || clazzParam == short.class) {
            return AttributeTagsPatterns.NUMBER_REGEX;
        } else if (clazzParam == String.class || clazzParam == boolean.class || clazzParam == Boolean.class) {
            return AttributeTagsPatterns.STRING_REGEX;
        }
        throw new RuntimeException("setter with such parameter : " + clazzParam + " does not support");
    }

    static Object convertStringToPrimitiveType(String value, Class<?> convertType)
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
