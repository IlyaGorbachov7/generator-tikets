package bntu.fitr.gorbachev.ticketsgenerator.main.util;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.exep.NotAccessForReadToFileException;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.exep.NotAccessForWriteToFileException;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.exep.NotAccessToFileException;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/*If out want specify @Log41 or @Slf4j annotation, then logger will be don't worked because Logger now don't initialized */
public class FilesUtil {


    /**
     * This separator used always for ZIP or JAR path of files
     */
    public static final char ZIP_SEPARATOR = '/';

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
    public static InputStream resolveSourceLocationAsInputStream(String path) throws NotAccessToFileException {
        InputStream is = FileUtils.class.getClassLoader().getResourceAsStream(path);
        if (is == null) {
            // try to receive file outside jar file
            Path of = Path.of(path);
            checkFileReading(of);
            try {
                is = new BufferedInputStream(new FileInputStream(path)); // if file don't found than throw FileNotFoundException
            } catch (FileNotFoundException e) {
                throw new NotAccessToFileException(e, of);
            }
            System.out.println(String.format("File %s located outside of the project", path));
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

    public static void checkFileCredentials(Path path) throws NotAccessToFileException {
        checkFileReading(path);
        if (!Files.isWritable(path)) {
            throw new NotAccessForWriteToFileException(path);
        }
    }


    public static File createDirIfNotExist(Path path) throws NotAccessToFileException {
        File dir;
        if (path == null) return null;
        if (Files.exists(path)) {
            checkFileCredentials(path);
            dir = path.toFile();
        } else {
            dir = path.toFile();
            if (!dir.mkdir()) {
                System.out.println("log: " + path.getParent());
                createDirIfNotExist(path.getParent());
                if (!dir.mkdir()) {
                    checkFileCredentials(path);
                }
            }
        }
        return dir;
    }

    /**
     * This method search and return the higher Path which is existed
     * <p>
     * Simple put if given param-path is existed, then return this path.
     * Else will be take parent path and check it on the existing
     *
     * @return the highest Path is <b>Exist</b>, else return <b>NULL</b>,is parent path don't exist
     * @see @code FilesUtilTest
     */
    public static File findDirIsExist(Path path) {
        File dir = null;
        if (path == null) return null;
        dir = path.toFile();
        if (Files.exists(path)) {
            return dir;
        } else {
            return findDirIsExist(path.getParent());
        }
    }

    /**
     * image.jpg,    => .ipg
     * <p>
     * file.with  => .with
     * <p>
     * example.file.without.extension => .extension
     * <p>
     * exampleFile  => empty
     * <p>
     * em.sdfsdf.sdf. => empty
     */
    public static String getExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex <= 0 || dotIndex == fileName.length() - 1) {
            return "";  // Файл без расширения
        }
        return fileName.substring(dotIndex + 1);
    }

    public static String getExtensionWithPoint(String fileName) {
        String res = getExtension(fileName);
        return (res.isEmpty()) ? "" : "." + res;
    }

    public static String buildUniqueNameInDirForFile(@NonNull Path destinationDir, @NonNull String startPrefix, @NonNull String fileExtension) {
        return buildUniqueFileNameInDir(destinationDir, formatPrefixForFile(startPrefix), fileExtension);
    }

    /**
     * @param destinationDir        dissertation folder where will be defining unique fileName
     * @param startPrefix           start prefix file name
     * @param prefixPartImageWidth  prefix image width, which will be used in prefix fileName
     * @param prefixPartImageHeight prefix image height, which will be used in prefix fileName
     * @param fileExtension         extension build fileName
     * @return return unique name file in destination folder.
     */
    public static String buildUniqueNameInDirForImageFile(@NonNull Path destinationDir, @NonNull String startPrefix, int prefixPartImageWidth, int prefixPartImageHeight, @NonNull String fileExtension) {
        return buildUniqueFileNameInDir(destinationDir, formatPrefixForFileImage(startPrefix, prefixPartImageWidth, prefixPartImageHeight), fileExtension);
    }

    /**
     * @param destinationDir       dissertation folder where will be defining unique fileName
     * @param supplierFormatPrefix support format expression
     * @param fileExtension        file extension
     */
    public static String buildUniqueFileNameInDir(@NonNull Path destinationDir, Supplier<String> supplierFormatPrefix, @NonNull String fileExtension) {
        Set<String> foundUniqueFragmentImgFilesInDir = foundUniqueFragmentFilesInDir(destinationDir, supplierFormatPrefix);
        StringBuilder builderImageName = new StringBuilder(supplierFormatPrefix.get());
        foundUniqueFragmentImgFilesInDir.stream()
                .filter((uniqueFragment) -> {
                    if (uniqueFragment.isEmpty()) {
                        return true; // это говорит о том, что в папке есть файл, имя которое полностью совпадает с переданными префиксами.
                    }
                    Scanner scanner = new Scanner(uniqueFragment);
                    return scanner.hasNextInt();
                }).mapToInt(uniqueFragment -> {
                    if (uniqueFragment.isEmpty()) {
                        return 0;
                    }
                    return Integer.parseInt(uniqueFragment);
                }).max().ifPresentOrElse((maxIndexFragment) -> {
                    builderImageName.append(' ').append(++maxIndexFragment);
                }, () -> {
                    // В том случаи, если вообще не было найдено не одного файла с похожим названием по переданным префиксам
                    // значит файл можно называть значением собранное из префиксов
                });
        return builderImageName.append(fileExtension).toString();
    }

    /**
     * For all files
     */
    public static Set<String> foundUniqueFragmentFilesInDirForFile(@NonNull Path destinationDir, @NonNull String startPrefix) {
        return foundUniqueFragmentFilesInDir(destinationDir, formatPrefixForFile(startPrefix));
    }

    /**
     * For file images
     */
    public static Set<String> foundUniqueFragmentFilesInDirForImageFile(@NonNull Path destinationDir, @NonNull String startPrefix, int prefixPartImageWidth, int prefixPartImageHeight) {
        return foundUniqueFragmentFilesInDir(destinationDir, formatPrefixForFileImage(startPrefix, prefixPartImageWidth, prefixPartImageHeight));
    }


    /**
     * This method return list of uniques fragment name image the files in destination folder, which  start prefix fileName
     * is matches with given params
     * <p>
     * <b>Return {@code empty} </b> if no files are found with this specified prefixes in destination dir
     * <p>
     * Else if {@code not empty} then this told about that in destination dir was founds image files with same prefix.
     * <p>
     * <b>In case if value</b> of any element in list is <b>empty</b> => "", then it told about that in destination dir already exist file,
     * fileName which <i>is equals combined prefixImageName.</i>
     * <p>
     * <b>Else if value</b> of element in list is <b>is not empty</b> => "32" or "33", or "91", then it told that in destination dir exist file with this unique fragment name
     */
    public static Set<String> foundUniqueFragmentFilesInDir(@NonNull Path destinationDir, Supplier<String> supplierFormatPrefix) {
        String prefixName = supplierFormatPrefix.get();
        File[] files = destinationDir.toFile().listFiles(pathname -> {
            System.out.println(pathname.getName());

            return pathname.getName().startsWith(prefixName);
        });
        if (files == null) files = new File[0];
        return Arrays.stream(files).map(File::getName)
                .map(fileName -> {
                    String extension = FilesUtil.getExtensionWithPoint(fileName);
                    int indexStart = prefixName.length(); // is pattern;
                    int indexEnd = fileName.lastIndexOf(extension);
                    String uniqueNameTip = fileName.substring(indexStart, indexEnd);
                    return uniqueNameTip.trim();
                }).collect(Collectors.toSet());
    }

    public static Supplier<String> formatPrefixForFileImage(@NonNull String startPrefix, int prefixPartImageWidth, int prefixPartImageHeight) {
        return () -> combinePrefixes("%s %dх%d", startPrefix, prefixPartImageWidth, prefixPartImageHeight);
    }

    public static Supplier<String> formatPrefixForFile(@NonNull String startPrefix) {
        return () -> combinePrefixes("%s", startPrefix, startPrefix);
    }

    public static String combinePrefixes(String format, Object... args) {
        return String.format(Locale.ROOT, format, args);
    }

    /**
     * FileUtilTest.test10()
     */
    public static void addToZipFile(@NonNull Path zipFilePath, @NonNull Path[] addedFile, @NonNull Path[] addedFileWithNewName)
            throws NotAccessToFileException, ZipException, IOException {
        checkFileReading(zipFilePath);
        if (addedFile.length != addedFileWithNewName.length) {
            throw new IllegalArgumentException("addedFile.length != addedFileWithNewName.length. Should be equals");
        }
        File tempFile = File.createTempFile("tempZip", ".zip");
        tempFile.delete();
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath.toAbsolutePath().toString()));
             ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tempFile))) {
            List<Path> allPath = new ArrayList<>();

            // Копируем существующие файлы из исходного ZIP в временный ZIP
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                zos.putNextEntry(new ZipEntry(entry.getName()));
                zis.transferTo(zos);
                zos.closeEntry();
                zis.closeEntry();
            }
            // Добавляем новый файл в ZIP
            for (int i = 0; i < addedFile.length; i++) {
                File newFile = addedFile[0].toFile().getAbsoluteFile();
                try (FileInputStream fis = new FileInputStream(newFile)) {
                    ZipEntry newEntry = new ZipEntry(addedFileWithNewName[i].toString());
                    zos.putNextEntry(newEntry);
                    fis.transferTo(zos);
                    zos.closeEntry();
                }
            }
        }

        // Заменяем старый ZIP файлом с добавленными элементами
        File oldFile = zipFilePath.toFile();
        Files.delete(zipFilePath);
        tempFile.renameTo(oldFile);
        tempFile.delete();
    }

    public static boolean removeFromZipFile(String zipFilePath, String fileNameToRemove) throws IOException {
        File tempFile = File.createTempFile("tempZip", ".zip");
        tempFile.delete();

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath));
             ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tempFile))) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String entryName = entry.getName();

                // Пропускаем файл, который хотим удалить
                if (entryName.equals(fileNameToRemove)) {
                    System.out.println("File deleted: " + entryName);
                    continue;
                }

                // Копируем остальные файлы в новый ZIP
                zos.putNextEntry(new ZipEntry(entryName));
                zis.transferTo(zos);
                zos.closeEntry();
                zis.closeEntry();
            }
        }

        // Заменяем старый ZIP-файл новым
        File oldFile = new File(zipFilePath);
        if (oldFile.delete()) {
            tempFile.renameTo(oldFile);
            return true;
        } else {
            System.err.println("Don't tried delete old file: " + oldFile);
            return false;
        }
    }


    /**
     * THis method return boolean value. True -- if application launch from jar file or  through IDA
     * <p>
     * #{@linkplain Class#getResource(String)} -- method return string of path where started with "file:" or "jar:"
     */
    public static boolean isRunningFromJar() {
        String fileClazz = FilesUtil.class.getName().replace('.', '/');
        String clazzJar = FilesUtil.class.getResource("/" + fileClazz + ".class").toString();
        return clazzJar.startsWith("jar:");
    }

    /**
     * This method return standard path of the root store for <b>current</b> operating system, where
     * saved anything data applications
     */
    public static Path getRootStore() {
        return getRootStore(OS.getCurrent());
    }

    /**
     * This method return standard path of the root store for specified operating system, where
     * saved anything data applications
     */
    public static Path getRootStore(OS operationSystem) {
        String userHome = System.getProperty("user.home");
        switch (operationSystem) {
            case WINDOWS -> {
                String appsStore = System.getenv("APPDATA");
                return Path.of(Objects.nonNull(appsStore) ? appsStore : userHome);
            }
            case LINUX, SOLARIS -> { // directory : userName/.config - is standard for this OS
                return Path.of(userHome, ".config");

            }
            case OSX -> {// directory : userName/Library/Application Support - is standard for this OS
                return Path.of(userHome, "Library", "Application Support");
            }
            default -> throw new UnsupportedOperationException();
        }
    }

    public enum OS {
        LINUX("linux", "unix", "nix", "nux"), WINDOWS("win"), OSX("mac"), SOLARIS("solaris", "sunos"), UNKNOWN("unknown");

        public static final String NAME = System.getProperty("os.name"), VERSION = System.getProperty("os.version");

        public static final double JAVA_VERSION = getJavaVersion();
        public static final OS CURRENT = getCurrent();
        @Getter
        private final String name;
        private final String[] aliases;
        private static final String[] browsers = new String[]{"google-chrome", "firefox", "opera", "konqueror",
                "mozilla"};

        OS(String... aliases) {
            if (aliases == null)
                throw new NullPointerException();

            this.name = toString().toLowerCase(Locale.ROOT);
            this.aliases = aliases;
        }

        public boolean is(OS os) {
            return this == os;
        }

        private static OS getCurrent() {
            String osName = NAME.toLowerCase(Locale.ROOT);

            for (OS os : values())
                for (String alias : os.aliases)
                    if (osName.contains(alias))
                        return os;

            return UNKNOWN;
        }

        public static boolean is(OS... any) {
            if (any == null)
                throw new NullPointerException();

            if (any.length == 0)
                return false;

            for (OS compare : any)
                if (CURRENT == compare)
                    return true;

            return false;
        }

        private static double getJavaVersion() {
            String version = System.getProperty("java.version");
            int pos, count = 0;

            for (pos = 0; pos < version.length() && count < 2; pos++) {
                if (version.charAt(pos) == '.') {
                    count++;
                }
            }

            --pos; // EVALUATE double

            String doubleVersion = version.substring(0, pos);
            return Double.parseDouble(doubleVersion);
        }
    }
}
