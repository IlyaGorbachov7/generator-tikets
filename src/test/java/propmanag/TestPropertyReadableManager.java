package propmanag;

import bntu.fitr.gorbachev.ticketsgenerator.main.TicketGeneratorUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.exep.TicketGeneratorException;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.FilesUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.PropertiesManagerBase;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.exep.NotAccessForReadToFileException;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.ReadableProperties;
import org.hibernate.engine.jdbc.ReaderInputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

public class TestPropertyReadableManager {
    static ReadableProperties prop;

    @BeforeAll
    public static void init() throws IOException {
        prop = PropertiesManagerBase.builder().build(TicketGeneratorUtil.class.getResourceAsStream("/application.properties"));
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "str.values",
    })
    void test1(String key) throws TicketGeneratorException {
        System.out.println(Arrays.toString(prop.getValues(key)));
    }

    @Test
    void test2() {
        System.out.println(prop.getInt("int.value"));
    }

    @Test
    void test3() {
        System.out.println(Arrays.toString(prop.getInts("int.values")));
    }

    @Test
    void test4NastyArray() {
        System.out.println(Arrays.toString(prop.getValues("str.withsys")));
    }

    @Test
    void test5Nasty() {
        System.out.println(prop.getValue("str.nasty"));
    }

    @Test
    void testMap() {
        prop.getMap("app.map").forEach((key, value) -> {
            System.out.println(key + " : " + value);
        });
    }

    @Test
    void testMap2() {
        prop.getMap("app.map2").forEach((key, value)-> {
            System.out.println(key + " : "+value );
        });
    }

    @Test
    void testMap3() {
        prop.getMap("app.map3").forEach((key, value)-> {
            System.out.println(key + " : "+value );
        });
    }
    /**
     * PropertyReadableManager don't take in account "file separator" depending on the operation system,
     * But you should remember that if use <i>File file = new file(path)</i>, then  file separator become valid
     * Даже если в property файле вписать не соответствующие операционной системе "файловый разделитель"
     */
    @Test
    void testFileCheckPathSeparator() {
        String path = prop.getValue("app.directory");
        System.out.println(path);
        Assertions.assertDoesNotThrow(() -> {
            File file = new File(path);
            System.out.println(file.getPath());
            System.out.println(file.getAbsoluteFile());
            System.out.println(file.exists());
            System.out.println(Files.exists(file.toPath()));

            System.out.println();

            Path p = Path.of(path);
            System.out.println(p);
        }, "exception");
    }

    /**
     * При использовании  <i>ClassName.class.<b>getResourceAsStream(path)</b></i>
     * <p>
     * Данный метод ищет файлы в приделах jar-файла
     * <p>
     * Если по указанному пути нет такого файла то возвращается inputStream = null;
     */
    @Test
    void testGetPathByString() {
        // around thi directory project
        String path = "/resources/application.properties";

        // location on the other disk
        String pathOther = "C:/Users/SecuRiTy/Documents/text.txt";

        Assertions.assertDoesNotThrow(() -> {
            try (InputStream is = TicketGeneratorUtil.class.getResourceAsStream(path);
                 ReaderInputStream rd = new ReaderInputStream(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                System.out.println("file from this jar file is current loaded");
            }
        });

        Assertions.assertDoesNotThrow(() -> {
            try (InputStream is = TicketGeneratorUtil.class.getResourceAsStream(pathOther)) {
                if (is == null) {
                    System.out.println("inputstream is null. File from other disk is undefined");
                }
            }
        }, "exepetion!!!");
    }

    @Test
    public void testGetPathByStringVersion2() {
        // around thi directory project
        String path = "/resources/application.properties";

        // location on the other disk
        String pathOther = "C:/Users/SecuRiTy/Documents/text.txt";

        Assertions.assertThrows(FileNotFoundException.class, () -> {
            try (InputStream is = new FileInputStream(path)) {
                System.out.println("file from this jar file is current loaded");
                System.out.println(is);
            }
        });
        Assertions.assertDoesNotThrow(() -> {
            try (InputStream is = new FileInputStream(pathOther)) {
                System.out.println("inputstream is null. File from other disk is undefined");
                ;
                System.out.println(is);
            }
        });
    }

    @Test
    public void testGetPathByStringResolveLocation() {
        // around thi directory project
        String path = "/resources/application.properties";

        // location on the other disk
        String pathOther = "C:/Users/SecuRiTy/Documents/text.txt";

        Assertions.assertDoesNotThrow(() -> {
            try (InputStream is = resolveSourceLocation(path)) {
                System.out.println("file from this jar file is current loaded");
                System.out.println(is);
            }
        });
        Assertions.assertDoesNotThrow(() -> {
            try (InputStream is = resolveSourceLocation(pathOther)) {
                System.out.println("inputstream is null. File from other disk is undefined");
                System.out.println(is);
            }
        });
    }


    /**
     * При использовании  <i>ClassName.class.<b>getResourceAsStream(path)</b></i>
     * <p>
     * Данный метод ищет файлы в приделах jar-файла
     * <p>
     * Если по указанному пути нет такого файла, то возвращается inputStream = null;
     * Даже если вы укажите путь существующего файла, но который находится ВНЕ jar файла,
     * то этот метод вернет NULL.
     * <p>
     * // around this jar project
     * <p>
     * String path = "/resources/application.properties"; ==> Result: InputStream != null
     * <p>
     * // location on the other disk (outside)
     * <p>
     * String pathOther = "C:/Users/SecuRiTy/Documents/text.txt"; Result InputStream == null
     * <p>
     * Хочу заметить что метод getResourceAsStream не выбрасывает исключения если файла нет, вернет null.
     * <p>
     * <b>Теперь к реализации этого метода.</b>
     * Цель данного метода в том, чтобы можно было получить <b>InputStream</b> передав строку пути, которая
     * может указывать на файл в приделах jar-файла или всей файловой системы.
     *
     * @see {@link TestPropertyReadableManager#testGetPathByStringResolveLocation()}
     * @see {@link TestPropertyReadableManager#testGetPathByStringVersion2()}
     */
    InputStream resolveSourceLocation(String path) throws IOException, FileNotFoundException, AccessDeniedException {
        InputStream is = TicketGeneratorUtil.class.getResourceAsStream(path);
        if (is == null) {
            // try to receive file outside jar file
            is = new BufferedInputStream(new FileInputStream(path)); // if file don't found than throw FileNotFoundException
            System.out.println(String.format("File %s located outside jar file", path));
            return is;
        }
        System.out.println(String.format("File %s located inside jar file", path));
        return is;
    }


    @Test
    void testCredantaions() throws IOException {
// around thi directory project
        String path = "/resources/application.properties";

        // location on the other disk
        String pathOther = "C:/Users/SecuRiTy/Documents/text.txt";

        Assertions.assertThrows(NotAccessForReadToFileException.class, () -> {
            FilesUtil.checkFileReading(Path.of(path));
        });


        Assertions.assertDoesNotThrow(() -> {
            FilesUtil.checkFileReading(Path.of(pathOther));
        });

        Assertions.assertDoesNotThrow(() -> {
            FilesUtil.checkFileCredentials(Path.of(pathOther));
        });
    }


    /**
     * Если файла нет или (он есть, но нельзя читать), то метод mkdir
     * не выбрасывает исключение. А просто возвращает false
     */
    @Test
    void testCreateDir() throws IOException {
        String s = "C:/Users/SecuRiTy/.tickets-generator/folder";
        Path path = Path.of(s);
        File dir = path.toFile();
        System.out.println("file eixist: " + Files.exists(path));
        System.out.println("file read: " + Files.isReadable(path));
        System.out.println("file write:" + Files.isWritable(path));
        if (dir.mkdir()) {
            System.out.println("dir is crated : " + path);
        } else {
            System.out.println("dir don't created: " + path);
            dir = dir.getParentFile();
            path = dir.toPath();
            System.out.println("Check credentials parent dir: " + path);
            System.out.println("parentfile eixist: " + Files.exists(path));
            System.out.println("parentfile read: " + Files.isReadable(path));
            System.out.println("parentfile write:" + Files.isWritable(path));
            FilesUtil.checkFileCredentials(path);
        }
    }

    @Test
    void testOptinal() {
        Optional<String> optinal = Optional.ofNullable("/serialzizer/path/other");

        System.out.println("RESULT : " +
                           optinal.map(str -> {
                               System.out.println("value : " + str);
                               return Path.of(str);
                           }));


        System.out.println("--------------------------------");


        optinal = Optional.ofNullable(null);

        System.out.println("RESULT : " +
                           optinal.map(str -> {
                               System.out.println("value : " + str);
                               return Path.of(str);
                           }));

    }


    @Test
    public void testMkDir() {
        File file = new File("C:\\Users\\SecuRiTy\\.tickets-generator\\serializer\\sub\\serial\\hooo");
        System.out.println(Files.exists(file.toPath()));
        System.out.println(file.mkdir());
    }

}
