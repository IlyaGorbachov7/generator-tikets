package bntu.fitr.gorbachev.ticketsgenerator.main.test;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Iterator;
import java.util.ServiceLoader;

public class Test6 {
    public static void main(String[] args) {
        Iterator<Driver> drivers= DriverManager.getDrivers().asIterator();

        while (drivers.hasNext())
            System.out.println(drivers.next());

        System.out.println("---------------");
        ServiceLoader<Driver> loader = ServiceLoader.load(Driver.class);
        loader.forEach(System.out::println);
    }
}
