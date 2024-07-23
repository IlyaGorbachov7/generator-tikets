package bntu.fitr.gorbachev.ticketsgenerator.main;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.exep.NotAccessForReadToFileException;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.exep.NotAccessForWriteToFileException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TicketGeneratorUtil {

    public static Path getPathUserDirectory() {
        return Path.of(System.getProperty("user.home"));
    }

    public static File getFileUserDirectory() throws IOException {
        checkFileCredentials(getPathUserDirectory());
        File dirUserHome = getPathUserDirectory().toFile();
        dirUserHome.mkdir();
        return dirUserHome;
    }

    public static Path getPathAppDirectory() {
        return Path.of(getPathUserDirectory().toString(), ".tickets-generator");
    }

    public static File getFileAppDirectory() throws IOException {
        checkFileCredentials(getPathAppDirectory());
        File dirApp = getPathAppDirectory().toFile();
        dirApp.mkdir();
        return dirApp;
    }

    public static Path getPathSerializeDirectory() {
        return Path.of(getPathAppDirectory().toString(), "serialize");
    }

    public static File getFileSerializeDirectory() throws IOException {
        checkFileCredentials(getPathSerializeDirectory());
        File dirSer = getPathSerializeDirectory().toFile();
        dirSer.mkdir();
        return dirSer;
    }

    public static String getFileSeparator() {
        return System.getProperty("file.separator");
    }

    public static void checkFileCredentials(Path file) throws IOException {
        if (!Files.isReadable(file)) {
            throw new NotAccessForReadToFileException(file);
        }
        if (!Files.isWritable(file)) {
            throw new NotAccessForWriteToFileException(file);
        }
    }
}
