package bntu.fitr.gorbachev.ticketsgenerator.main.test;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Question;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.QuestionExt;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.constants.LexicalPatterns;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools.constants.TagPatterns;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.util.List;
import java.util.regex.Matcher;

public class Test7 {
    public static void main(String[] args) throws CloneNotSupportedException {
        String s= "<S>>";
        Matcher matcher = TagPatterns.LIST_START_TAG.getMatcher(s);
        System.out.println(matcher.matches());

        QuestionExt question = new QuestionExt();
        question.clone();
    }

}
