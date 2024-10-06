package propmanag;

import bntu.fitr.gorbachev.ticketsgenerator.main.TicketGeneratorUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.PropertiesManagerBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

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

    @AfterEach
    public void repeatEach() {
        System.out.println();
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
