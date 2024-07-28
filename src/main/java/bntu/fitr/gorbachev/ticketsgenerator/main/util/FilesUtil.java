package bntu.fitr.gorbachev.ticketsgenerator.main.util;

import bntu.fitr.gorbachev.ticketsgenerator.main.TicketGeneratorUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.exep.NotAccessForReadToFileException;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.exep.NotAccessForWriteToFileException;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.exep.NotAccessToFileException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FilesUtil {


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
     * <p>
     * <b>See:</b> TestPropertyReadableManager#testGetPathByStringResolveLocation()
     * <p>
     * <b>See:</b> TestPropertyReadableManager#testGetPathByStringVersion2()
     */
    public static InputStream resolveSourceLocation(String path) throws NotAccessToFileException {
        InputStream is = TicketGeneratorUtil.class.getResourceAsStream(path);
        if (is == null) {
            // try to receive file outside jar file
            Path of = Path.of(path);
            checkFileReading(of);
            try {
                is = new BufferedInputStream(new FileInputStream(path)); // if file don't found than throw FileNotFoundException
            } catch (FileNotFoundException e) {
                throw new NotAccessToFileException(e, of);
            }
            System.out.println(String.format("File %s located outside jar file", path));
            return is;
        }
        System.out.println(String.format("File %s located inside jar file", path));
        return is;
    }

    public static void checkFileReading(Path file) throws NotAccessToFileException {
        if (!Files.exists(file)) {
            throw new NotAccessToFileException(new FileNotFoundException(file.toString()), file);
        }
        if (!Files.isReadable(file)) {
            throw new NotAccessForReadToFileException(file);
        }
    }

    public static void checkFileCredentials(Path path) throws IOException {
        checkFileReading(path);
        if (!Files.isWritable(path)) {
            throw new NotAccessForWriteToFileException(path);
        }
    }


    public static File createDirIfNotExist(Path path) throws IOException {
        File dir;
        if (Files.exists(path)) {
            checkFileCredentials(path);
            dir = path.toFile();
        } else {
            dir = path.toFile();
            if (!dir.mkdir()) {
                checkFileCredentials(path.getParent());
                // fatal error. Because if getParent() don't throw exception on the checking credentials, then
                // why child dir don't created.
                throw new NotAccessToFileException(String.format("Path %s don't created", path), path);
            }
        }
        return dir;
    }
}
