package subtest1;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.JListDataBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

public class Test1 {

    @Test
    public void test1(){
        Assertions.assertTrue(Panel.class.hashCode() != JPanel.class.hashCode());
        System.out.println(Panel.class.hashCode() + " " + JPanel.class.hashCode());
        System.out.println(Panel.class.equals(JPanel.class));
    }

    @Test
    public void test2(){
        JListDataBase jlist = new JListDataBase(new Class[]{Panel.class, JPanel.class}, null);
        Assertions.assertEquals(jlist.getJTblsDataTable().size(), 2);
        jlist.getJTblsDataTable().forEach((kay, value)->{
            System.out.println("kay : "+ kay + "  =  value : "+ value);
        });
    }
}
