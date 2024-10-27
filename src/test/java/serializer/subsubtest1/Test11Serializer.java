package serializer.subsubtest1;

import bntu.fitr.gorbachev.ticketsgenerator.main.TicketGeneratorUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.serializer.Serializer;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.AppThemeManager;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.ThemeApp;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.ThemeAppWrapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
public class Test11Serializer {

    static Serializer serializer;

    @BeforeAll
    public static void init() throws IOException {
        serializer = Serializer.getSerializer(Path.of(TicketGeneratorUtil.getFileUserDirectory().toString(), "sp"));
    }

    /*
    Перед тем как выполнять этот тест,
    Сперва запусти тест -- Создадутся эти файлы
    Потом закрой полностью доступ к файлу "ааааа" и дальше смотри результат !!!
    Попробуй поиграть и ты поймешь
    ЗАМЕЧАНИЕ !!!!
    file.exists() -> возвращает true
     Files.exists(path) -> возвращает false
     Поэтому следует использовать file.exists() -- этот способ, потому что file.isDirectory() и
     Files.isDirectory(path) оба возвр. true: То есть если это дириктория то файл существет.

     */
    @Test
    public void testDir() throws IOException {
        Path path = Path.of(TicketGeneratorUtil.getFileUserDirectory().toString(), "aaaaaa");
        File file = path.toFile();

        System.out.println(file.exists());
        System.out.println("exist : " + Files.exists(path));
        System.out.println(file.isDirectory());
        System.out.println("dir : " + Files.isDirectory(path));
        System.out.println();

        // стери этот if и посомтри результат, что Files.isWritable(path) == false и се круто
        System.out.println("canRead: " + file.canRead());
        System.out.println("isReadable: " + Files.isReadable(path));
        System.out.println("canWrite: " + file.canWrite());
        System.out.println("isWritable: " + Files.isWritable(path));
    }

    /*
    Перед тем как выполнять этот тест,
    Сперва запусти тест -- Создадутся эти файлы
    Потом закрой полностью доступ к файлу "ааааа" и дальше смотри результат !!!
     */
    @Test
    public void testDirCr() throws IOException {
        Path path = Path.of(TicketGeneratorUtil.getFileUserDirectory().toString(), "aaaaaa/txt");
        System.out.println("exist : " + Files.exists(path));
        System.out.println("toFile exist : " + path.toFile().exists());
        System.out.println("dir : " + Files.isDirectory(path));
        System.out.println("toFile isDir: " + path.toFile().isDirectory());

        // если разрешить все, кроме write записи в папку, то toFile().canWrite() = true - что в корне не верно
        // Files.isWritable(path) = false что верно. что за хуйня
        System.out.println("file.canREad: "+path.toFile().canRead());
        System.out.println("Files.canReadable: " + Files.isReadable(path));
        System.out.println("file.canWrite: "+path.toFile().canWrite());
        System.out.println("Files.canWriteble: " + Files.isWritable(path));

        // стери этот if и посомтри результат, что Files.isWritable(path) == false и се круто
        if (path.toFile().canWrite()) {
            Files.createFile(Path.of(path.toString(), "file.txt"));
        }
        if (Files.isWritable(path)) {
            Files.createFile(Path.of(path.toString(), "file.txt"));
        }
        Files.createDirectories(path);
    }

    @Test
    public void testDirOtherThrow() throws IOException {
        Path path = Path.of(TicketGeneratorUtil.getFileUserDirectory().toString(), "aaaaaa/txt");
        if (Files.exists(path)) {
            System.out.println("1");
            Files.createDirectories(Path.of(path.toFile().getCanonicalPath(), "hooolo"));
        } else {
            System.out.println("2");
            Files.createDirectories(path);
        }
    }

    @Test
    public void testFileOtherThrow() throws IOException {
        Path path = Path.of(TicketGeneratorUtil.getFileUserDirectory().toString(), "aaaaaa/txt.txt");
        if (Files.exists(path)) {
            System.out.println("1");
        } else {
            System.out.println("2");
            Files.createFile(path);
        }
    }

    @Test
    public void testDirFileOtherThrow() throws IOException {
        Path path = Path.of(TicketGeneratorUtil.getFileUserDirectory().toString(), "dirNonExist/txt.txt");
        if (Files.exists(path)) {
            System.out.println("1");
        } else {
            System.out.println("2");
            try {
                System.out.println(3);
                Files.createFile(path);
            } catch (IOException e) {
                if (Files.exists(path.getParent())) {
                    throw new IOException("access dir only for reading: ", e);
                } else {
                    Files.createDirectories(path.getParent());
                    System.out.println("parentPath: toFiel.exist: " + path.getParent().toFile().exists());
                    System.out.println("parentpath : exist: " + Files.exists(path.getParent()));
                    System.out.println("parentpath : isDir: " + Files.isDirectory(path.getParent()));
                    System.out.println("parentpath : canRead: " + Files.isReadable(path.getParent()));
                    System.out.println("parentpath : canWrite: " + Files.isWritable(path.getParent()));
                    Files.createFile(path);
                    throw new IOException("parant dir don't exist", e);
                }

            }
        }
    }

    @Test
    void test11() throws IOException {
        System.out.println(serializer.deserialize(ThemeAppWrapper.class).size());
    }

    @Test
    void testSer() throws IOException {
        ThemeAppWrapper wrapper = new ThemeAppWrapper(ThemeApp.LIGHT);
        serializer.serialize(wrapper);
    }

    @Test
    void testPoint(){
        System.out.println(Path.of(System.getProperty("user", "."), "defaultSerializeDir"));
    }
}
