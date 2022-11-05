package bntu.fitr.gorbachev.ticketsgenerator.main.test;

import java.util.ResourceBundle;

public class Test6 {
    public static void main(String[] args) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("resources/patterns");

        resourceBundle.getString("fdfd");
        for(var v : resourceBundle.keySet()){
            System.out.println(v);
        }
    }
}
