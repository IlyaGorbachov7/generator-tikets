package propmanag;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class SystemPropertiesTest {

    public static void main(String[] args) {
        /**
         * Аргументы программы мы их получаем из метода main. Они не представлю собой key=value.
         * Это просто массив аргументов программы. Можно что-то преобразовывать и т.д.
         */
        System.out.println(Arrays.toString(args));// аргументы программы

        /**
         * Я могу добавить свою переменную среды при конфигурации запуска приложения.
         * Я могу получить данное значение по ключик.
         * Но программно я не могу установить или изменить список evn variables.
         * Нет метода типа такого: setEnv(Key,Value).
         * Можно получить спиоск всех evn.
         */
        System.out.println(System.getenv("my.env.int"));// переменные среды
        System.out.println(System.getenv());
        System.getenv().put("my.env.int", "999");
        System.out.println(System.getenv("my.env.int"));



        System.out.println(System.getProperty("my.virtualmashin.options.int")); // переменные VM виртуальной машины

    }

    /**
     * Before run this test. Check configuration of test. Add in environment property key=value:
     */
    @Test
    void test() {
        Assertions.assertNull(System.getProperty("myproperty.int"));
        Assertions.assertNotNull(System.getenv("myproperty.int"));
    }
}
