package bntu.fitr.gorbachev.ticketsgenerator.main.test;


import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * @version 23.10.2022
 */
public class Test1 {
    static Logger logger = Logger.getLogger(Test1.class.getSimpleName());

    static {
        logger.info("Class " + Test1.class.getName() + " is Loading : classLouder :" + Test1.class.getClassLoader());
    }

    public static void main(String[] args) throws ClassNotFoundException {
        logger.config("Main method");
        System.getProperties().forEach((k, v) -> System.out.println("k=" + k + " v=" + v));
        System.out.println(System.getProperty("jdbc.drivers"));

//        Class.forName("com.mysql.jdbc.Driver");

        Enumeration<Driver> drivers = DriverManager.getDrivers();
        Driver driver = null;
        Iterator<Driver> iterator = drivers.asIterator();
        while (iterator.hasNext()) {
            logger.info(iterator.next().getClass().getName());
        }
    }

}
