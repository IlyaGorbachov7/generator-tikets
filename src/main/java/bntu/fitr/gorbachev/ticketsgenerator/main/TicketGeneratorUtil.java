package bntu.fitr.gorbachev.ticketsgenerator.main;

import bntu.fitr.gorbachev.ticketsgenerator.main.exep.TicketGeneratorException;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.FilesUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.exep.NotAccessToFileException;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.logger.LoggerException;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.logger.LoggerUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.AppThemeManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.Strings;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static bntu.fitr.gorbachev.ticketsgenerator.main.ConfigurationApplicationProperties.*;

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

    /**
     * Handled nullable value returned from config object.
     * <p>
     * This situation may be by reason don't specified key in file of properties. In this case Path
     * will be by default
     * <p>
     * map(Path::of) may return Optional<Path>.NULL because config may return Optional<String>.NULL
     */
    public static Path getPathAppDirectory() {
        return config.getDirApp().map(Path::of).orElseGet(
                () -> Path.of(getPathUserDirectory().toString(), ".tickets-generator"));
    }

    public static File getFileAppDirectory() throws IOException {
        Path path = getPathAppDirectory();
        return FilesUtil.createDirIfNotExist(path);
    }

    /**
     * Handled nullable value returned from config object.
     * <p>
     * This situation may be by reason don't specified key in file of properties. In this case Path
     * will be by default
     * <p>
     * map(Path::of) may return Optional<Path>.NULL because config may return Optional<String>.NULL
     */
    public static Path getPathSerializeDirectory() {
        return config.getDirSerialize().map(Path::of).orElseGet(
                () -> Path.of(getPathAppDirectory().toString(), "def-serializer"));
    }

    public static File getFileSerializeDirectory() throws IOException {
        Path path = getPathSerializeDirectory();
        return FilesUtil.createDirIfNotExist(path);
    }

    /**
     * Handled nullable value returned from config object.
     * <p>
     * This situation may be by reason don't specified key in file of properties. In this case Path
     * will be by default
     * <p>
     * map(Path::of) may return Optional<Path>.NULL because config may return Optional<String>.NULL
     */
    public static Path getPathLogsDirectory() {
        return config.getDirLogs().map(Path::of).orElseGet(
                () -> Path.of(getPathAppDirectory().toString(), "def-logs")
        );
    }

    public static File getFileLogsDirectory() throws IOException {
        Path path = getPathLogsDirectory();
        return FilesUtil.createDirIfNotExist(path);
    }

    /**
     * Handled nullable value returned from config object.
     * <p>
     * This situation may be by reason don't specified key in file of properties. In this case Path
     * will be by default
     * <p>
     * map(Path::of) may return Optional<Path>.NULL because config may return Optional<String>.NULL
     */
    public static AppThemeManager.ThemeApp getThemeAppDefault() {
        AppThemeManager.ThemeApp def = AppThemeManager.ThemeApp.LIGHT;
        AppThemeManager.ThemeApp theme = def;
        String value = Strings.toUpperCase(config.getThemeAppDef().orElseGet(() -> {
            log.warn("key:{} don't specified in configuration file.  The default value will be used", THEME_APP_DEF_KEY);
            return def.toString();
        }));
        try {
            theme = AppThemeManager.ThemeApp.valueOf(value);
        } catch (IllegalArgumentException ex) {
            log.warn("Specified  in property file value: {} of theme application don't matched with exists values ThemeApp. Set default value: {} ",
                    value, theme);
        }
        return theme;
    }

    public static String getFileSeparator() {
        return System.getProperty("file.separator");
    }

    public static void init() {
        try {
            // This sequence must be such!
            config = new ConfigurationApplicationProperties();
            System.setProperty(DIR_APP_KEY, getPathAppDirectory().toString());
            System.setProperty(DIR_SERIALIZE_KEY, getPathSerializeDirectory().toString());
            // this very importer because file log4j2.xml exist text, which contains property key from application.properties
            // So I must add this key=value from application.properties earlier than will be performed logger configuration
            System.setProperty(DIR_LOGS_KEY, getPathLogsDirectory().toString());

            LoggerUtil.init();
            AppThemeManager.run();
        } catch (Throwable ex) {
            showAlertDialog(ex);
            throw new RuntimeException();
        }

    }

    public static void showAlertDialog(Throwable t) {
        t.printStackTrace();
        boolean find = true;
        while (find) {
            if (t instanceof NotAccessToFileException ex) {
                log.error(ex.getMessage());
                JOptionPane.showMessageDialog(null, String.format("""
                                No read or write access or not found:
                                %s
                                """, ex.getFilePath()),
                        "Access undefined", JOptionPane.ERROR_MESSAGE);
                find = false;
            } else if (t instanceof TicketGeneratorException | t instanceof LoggerException | t instanceof IOException) {
                Throwable cause = t.getCause();
                if (cause instanceof NotAccessToFileException) {
                    t = cause;
                    continue;
                }
                JOptionPane.showMessageDialog(null, "Undefined Exception: " + t, "Error", JOptionPane.ERROR_MESSAGE);
                find = false;
            } else if (t instanceof Throwable) {
                JOptionPane.showMessageDialog(null, "Undefined Exception: " + t, "Error", JOptionPane.ERROR_MESSAGE);
                find = false;
            }
        }
    }

}
