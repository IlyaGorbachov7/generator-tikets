package subtest2;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test2 {

    @Test
    void test2() {
        int y = 0;
        int x = 0;
        y = x++;
        System.out.println("y: " + y + " x: " + x);
    }

    @Test
    void test3() {
        int totalImtes = 16;
        int itmesOnPage = 15;
        int totalPage = (int) (((totalImtes % itmesOnPage) == 0.0) ? (totalImtes / itmesOnPage) : (totalImtes / itmesOnPage) + 1);
        System.out.println(totalPage);
    }

    @Test
    void test4() {
        System.getProperties().forEach((k, v) -> {
            System.out.println("k =" + k + " v = " + v);
        });
    }

    @Test
    void testCode(){
        Properties prop = new Properties();
        String UTF8_STRING = "Hello 你好";
//        Resource
        byte[] UTF8_BYTES = UTF8_STRING.getBytes(StandardCharsets.UTF_8);
        System.out.println(Arrays.toString(UTF8_BYTES));
    }

    @Test
    void test11(){
        String text = "Egor Alla Anna";
        Pattern pattern = Pattern.compile("A.+?a");

        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            int start=matcher.start();
            int end=matcher.end();
            System.out.println("Match found" + text.substring(start,end) + " с "+ start + " By " + (end-1) + "position");
        }
        System.out.println(matcher.replaceFirst("Ira"));
        System.out.println(matcher.replaceAll("Olga"));
        System.out.println(text);
    }

    @Test
    void testMatch(){
        Pattern pattern = Pattern.compile("&\\{(.+?)}");
        Matcher matcher = pattern.matcher("dsf&{ddfjdfdf}ccccd&{123}9203fsdf");
        while (matcher.find()){
            System.out.println(matcher.group(1));;
        }
        System.out.println(matcher.find());
    }

    @Test
    void test22(){
        Pattern pattern = Pattern.compile("&\\{\\s*(.+?)\\s*}");
        String str = "&{app.directory}/serializer";
        Matcher matcher = pattern.matcher(str);
        StringBuilder stringBuilder = new StringBuilder(str);
        while (matcher.find()) {
            String k = matcher.group(1);
            String v = "xyq";
//            if () {
                int indexStart = matcher.start();
                int indexEnd = matcher.end();
                stringBuilder.replace(indexStart, indexEnd, v);
//            }
        }
        System.out.println(stringBuilder.toString());

    }
}
