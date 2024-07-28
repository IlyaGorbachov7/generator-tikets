package bntu.fitr.gorbachev.ticketsgenerator.main;

import bntu.fitr.gorbachev.ticketsgenerator.main.exep.TicketGeneratorException;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.FilesUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.exep.NotAccessForReadToFileException;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.exep.NotAccessForWriteToFileException;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.logger.LoggerUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.AppThemeManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Slf4j
public class TicketGeneratorUtil {
    @Getter
    private static ConfigurationApplicationProperties config;

    public static Path getPathUserDirectory() {
        return Path.of(System.getProperty("user.home"));
    }

    public static File getFileUserDirectory() throws IOException {
        Path path = getPathUserDirectory();
        return FilesUtil.createDirIfNotExist(path);
    }

    public static Path getPathAppDirectory() {
        return Path.of(config.getDirApp());
    }

    public static File getFileAppDirectory() throws IOException {
        Path path = getPathAppDirectory();
        return FilesUtil.createDirIfNotExist(path);
    }

    public static Path getPathSerializeDirectory() {
        return Path.of(config.getDirSerialize());
    }

    public static File getFileSerializeDirectory() throws IOException {
        Path path = getPathSerializeDirectory();
        return FilesUtil.createDirIfNotExist(path);
    }

    public static Path getPathLogsDirectory() {
        return Path.of(config.getDirLogs());
    }

    public static File getFileLogsDirectory() throws IOException {
        Path path = getPathLogsDirectory();
        ;
        return FilesUtil.createDirIfNotExist(path);
    }

    public static AppThemeManager.ThemeApp getThemeAppDefault() {
        String value = config.getThemeAppDef();
        AppThemeManager.ThemeApp def = AppThemeManager.ThemeApp.NIGHT;
        try {
            def = AppThemeManager.ThemeApp.valueOf(value);
        } catch (IllegalArgumentException ex) {
            log.warn("Specified  in property file value: {} of theme application don't matched with exists values ThemeApp. Set default value: {} ",
                    value, def);
        }
        return def;
    }

    public static String getFileSeparator() {
        return System.getProperty("file.separator");
    }

    public static void init() {
        try {
            // This sequence must be such!
            config = new ConfigurationApplicationProperties("/resources/application.properties"); // see this Class, that you understand this record
            LoggerUtil.init();
            AppThemeManager.run();
        } catch (NotAccessForReadToFileException | NotAccessForWriteToFileException ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Access undefined", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException();
        } catch (IOException ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Undefinded Exception", "", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException();
        } catch (TicketGeneratorException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Undefinded Exception", "", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException();
        }

    }
}
