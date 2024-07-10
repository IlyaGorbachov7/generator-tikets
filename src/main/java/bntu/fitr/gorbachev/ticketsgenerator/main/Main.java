package bntu.fitr.gorbachev.ticketsgenerator.main;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.FrameDialogFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.FrameType;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.PanelType;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.Serializer;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.thememanag.AppThemeManager;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    // TODO: нужно сделать какой то мэнеджер  А не работать через Main класс
    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        AppThemeManager.run();
        FrameDialogFactory.getInstance().createJFrame(FrameType.SPLASH_SCREEN, PanelType.SPLASH_SCREEN).setVisible(true);
    }

    public static Path getUserDirectory() {
        return Path.of(System.getProperty("user.home"));
    }

    public static File getFileUserDirectory() throws IOException {
        checkFileCredentials(getUserDirectory());
        File dirUserHome = getUserDirectory().toFile();
        dirUserHome.mkdir();
        return dirUserHome;
    }

    public static Path getAppDirectory() {
        return Path.of(getUserDirectory().toString(), ".tickets-generator");
    }

    public static File getFIleAppDirectory() throws IOException {
        checkFileCredentials(getAppDirectory());
        File dirApp = getAppDirectory().toFile();
        dirApp.mkdir();
        return dirApp;
    }

    public static String getFileSeparator() {
        return System.getProperty("file.separator");
    }

    public static void checkFileCredentials(Path file) throws IOException {
//        if (Files.isReadable(file)) {
//            throw new IOException("File don't readable: "+file);
//        } else if (Files.isWritable(file)) {
//            throw new IOException("File don't writable: "+file);
//        } else if (Files.isExecutable(file)) {
//            throw new IOException("File is executable: "+file);
//        }
    }


    public static void serialize() {
        try {
            Serializer.serialize(AppThemeManager.serialize(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
