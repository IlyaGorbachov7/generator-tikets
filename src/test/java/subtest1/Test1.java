package subtest1;

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

    }

}
