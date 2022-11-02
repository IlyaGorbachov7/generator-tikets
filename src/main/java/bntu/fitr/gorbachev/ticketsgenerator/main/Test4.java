package bntu.fitr.gorbachev.ticketsgenerator.main;

import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.InvalidLexicalException;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.AttributePatterns;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.AttributeTegS;
import org.apache.xmlbeans.impl.common.InvalidLexicalValueException;

import java.util.List;
import java.util.regex.Matcher;

public class Test4 {
    // extract attribute from string
    public static void main(String[] args) {
        String strAttributes = "  n = fsdDfrfdfs; r l=2; r";

        AttributeTegS attributeTegS = new AttributeTegS();

        String[] attributes = {"n", "l", "r"};

        for (String attribute : attributes) {

            int index;
            String str = strAttributes;
            while (!((index = str.indexOf(attribute)) < 0)) {
                boolean cutStr = false;
                int j = index;
                while (++j < str.length() && str.charAt(j) != '=') {
                    if (str.charAt(j) != ' ') {
                        cutStr = true;
                        str = str.substring(j);
                        break;
                    }
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
                String someAttrib;
                Matcher matcher;
                String value;
                switch (attribute) {
                    case "n" -> {
                        someAttrib = str.substring(index);
                        matcher = AttributePatterns.N.getMatcher(someAttrib);
                        value = null;
                        while (matcher.find()) {
                            value = matcher.group(2);
                            break;
                        }
                        if (value == null) {
                            throw new InvalidLexicalValueException(someAttrib);
                        }
                        attributeTegS.setN(value);
                    }
                    case "l" -> {
                        someAttrib = str.substring(index);
                        matcher = AttributePatterns.L.getMatcher(someAttrib);
                        value = null;
                        while (matcher.find()) {
                            value = matcher.group(2);
                            break;
                        }
                        if (value == null) {
                            throw new InvalidLexicalValueException(someAttrib);
                        }
                        attributeTegS.setL(Integer.parseInt(value));
                    }
                    case "r" -> {
                        someAttrib = str.substring(index);
                        matcher = AttributePatterns.R.getMatcher(someAttrib);
                        value = null;
                        while (matcher.find()) {
                            value = matcher.group(2);
                            break;

                        }
                        if (value == null) {
                            throw new InvalidLexicalValueException(someAttrib);
                        }
                        attributeTegS.setR(Integer.parseInt(value));
                    }
                }
            }
        }

        System.out.println(attributeTegS);

    }
}
