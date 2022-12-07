package bntu.fitr.gorbachev.ticketsgenerator.main.test;

import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Test10 {
    @SneakyThrows
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            int i = 0;
            int j = 0;
            for (i = 0; i < 1; i++) {
                for (j = 0; j < 10_000; j++) {
                    if(j == 7777){
                        try {
                            Thread.sleep(5_000);
                        } catch (InterruptedException e) {
                            System.out.println("------------------------------- INTERUPTED ALREADY + " + i + " " + j);
                            return;
                        }
                    }
                    System.out.println(j);
                }
            }
            try {
                System.out.println("-************** sloeap");
                Thread.sleep(5_000);
            } catch (InterruptedException e) {
                System.out.println("------------------------------- INTERUPTED ALREADY + " + i + " " + j);
            }
        });
        t1.start();

        System.out.println("Main sleap");
        System.out.println("Main interrupt t1");
        t1.interrupt();
        System.out.println("Main the end");
    }
}
