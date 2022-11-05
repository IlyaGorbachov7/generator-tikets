package bntu.fitr.gorbachev.ticketsgenerator.main.test;

public class Test7 {
    public static void main(String[] args) {
       Class<?> clazz = Float.class;

       if(clazz.getSuperclass() == Number.class){
           System.out.println(true);
       }
        System.out.println(Boolean.parseBoolean("true"));
    }
}
