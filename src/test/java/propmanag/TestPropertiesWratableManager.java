package propmanag;

import bntu.fitr.gorbachev.ticketsgenerator.main.TicketGeneratorUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.impl.PropertiesManagerBase;
import model.Person;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

public class TestPropertiesWratableManager {
    static String fileName = "test.properties";
    static PropertiesManagerBase prop;

    @BeforeAll
    static void init() throws IOException {
        prop = PropertiesManagerBase.builder().build(Path.of(fileName).toAbsolutePath().toFile());
        prop.getContent().forEach((key, value) -> {
            System.out.println(String.format("Key: %s, value: %s", key, value));
        });
    }

    @AfterAll
    static void destory() throws IOException {
        System.out.println("--------------------------- Destroy ---------------");
        prop.setValue("timeSaving", LocalDate.now().toString());
        prop.save();
        prop.getContent().forEach((key, value) -> {
            System.out.println("key:" + key + " value: " + value);
        });
    }

    @BeforeEach
    public void repeatEach() {
        System.out.println("----------------Test-------------------");
    }

    @Test
    public void testR() {
        System.out.println(Arrays.toString(prop.getValues("boolean.value")));
    }

    @Test
    public void testR1() {
        System.out.println(prop.getMap("map.value"));
    }

    @Test
    public void testR2() {
        System.out.println(Arrays.toString(prop.getDoubles("double.values")));
    }

    @Test
    public void testR3() {
        System.out.println(prop.getBoolean("boolean.value"));
        ;
    }

    @Test
    public void testR4() {
        System.out.println(Arrays.toString(prop.getBooleans("boolean.values")));
        ;
    }

    @Test
    public void testW1() {
        prop.setValue("string.value", "Hi broo iam string");
    }

    @Test
    public void testW2() {
        prop.setValue("string.array.value", new int[]{1, 2, 34, 2, 9, 34, 3, 34, 3, 4});
    }

    @Test
    public void testW3() {
        prop.setValue("string.array.value", new String[]{"fskdjf sd", "sdfjsdklfsd"});
    }

    @Test
    public void testW4() throws IOException {
        prop.setValue("map.value", Map.of(
                "key1", "value1",
                "key2", "value1",
                "Key3", "value3"));
    }

    @Test
    public void testW5() {
        prop.setValue("object.base64preview", new Person("Ilya Gorbachev", 22));
    }

    @Test
    public void testW6() {
        prop.setValue("double.value", 0.001);
    }

    @Test
    public void testW7() {
        prop.setValue("double.values", new double[]{342.23, 22.1, 9999.01, 9.23423333});
    }

    @Test
    public void testW8() {
        prop.setValue("boolean.value", false);
    }

    @Test
    public void testW9() {
        prop.setValue("boolean.values", new boolean[]{false, true, false, true, true});
    }

    @Test
    public void test() {
        Path path = Path.of(""); // D:\1)1-4 курс Универ\3 курс\2) Программирование на Java\ConstructorTickets
        System.out.println(path.toAbsolutePath());
        System.out.println(path.toAbsolutePath().toFile().exists());
    }

    @Test
    public void test1() {
        Path path = Path.of(fileName); // D:\1)1-4 курс Универ\3 курс\2) Программирование на Java\ConstructorTickets\test.properties
        System.out.println(path.toAbsolutePath());
        System.out.println(path.toAbsolutePath().toFile().exists());
    }

    @Test
    public void test2() {
        Path path = Path.of("."); // D:\1)1-4 курс Универ\3 курс\2) Программирование на Java\ConstructorTickets\.
        System.out.println(path.toAbsolutePath());
        System.out.println(path.toAbsolutePath().toFile().exists());
    }

    @Test
    public void test21() {
        Path path = Path.of("./" + fileName); //D:\1)1-4 курс Универ\3 курс\2) Программирование на Java\ConstructorTickets\.\test.properties
        System.out.println(path.toAbsolutePath());
        System.out.println(path.toAbsolutePath().toFile().exists());
    }

    @Test
    public void test3() {
        Path path = Path.of("/"); // D:\
        System.out.println(path.toAbsolutePath());
        System.out.println(path.toAbsolutePath().toFile().exists());
    }

    @Test
    public void test4() { // с текстом text3 можно заметить что если "/" то D:\ ну а если, то что текст test4 то видим что D:\info.properties
        Path path = Path.of("/" + fileName); // D:\info.properties
        System.out.println(path.toAbsolutePath());
        System.out.println(path.toAbsolutePath().toFile().exists());
    }

    @Test
    public void test5() {
        Path path = Path.of("src/main/java"); //D:\1)1-4 курс Универ\3 курс\2) Программирование на Java\ConstructorTickets\src\main\java
        System.out.println(path.toAbsolutePath());
        System.out.println(path.toAbsolutePath().toFile().exists());
    }

    @Test
    public void test6() throws URISyntaxException {
        Path path = Path.of(TicketGeneratorUtil.class.getResource("").toURI());
        //D:\1)1-4 курс Универ\3 курс\2) Программирование на Java\ConstructorTickets\target\classes\bntu\fitr\gorbachev\ticketsgenerator\main
        System.out.println(path);
    }

    @Test
    public void test7() throws URISyntaxException {
        Path path = Path.of(TicketGeneratorUtil.class.getResource("/").toURI());
        //D:\1)1-4 курс Универ\3 курс\2) Программирование на Java\ConstructorTickets\target\test-classes
        System.out.println(path);
    }
}
