package bntu.fitr.gorbachev.ticketsgenerator.main;

import bntu.fitr.gorbachev.ticketsgenerator.main.exep.TicketGeneratorException;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.FilesUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.exep.NotAccessToFileException;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.LocalizerException;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.LocalsConfiguration;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.logger.LoggerConfiguration;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.logger.LoggerException;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.AppThemeManager;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.ThemeApp;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.ThemeAppConfiguration;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.util.Strings;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Optional;

import static bntu.fitr.gorbachev.ticketsgenerator.main.ConfigurationApplicationProperties.*;

/*If out want specify @Log41 or @Slf4j annotation, then logger will be don't worked because Logger now don't initialized */
public class TicketGeneratorUtil {
    private static final String SYS_PROP_DEFAULT_LOCALE = THEME_APP_DEF_KEY;

    @Getter
    private static final ConfigurationApplicationProperties config;

    @Getter
    private static final LoggerConfiguration loggerConfiguration;

    @Getter
    private static final LocalsConfiguration localsConfiguration;

    @Getter
    private static final ThemeAppConfiguration themeAppConfiguration;

    private static final Logger log;

    static {
        try {
            // This sequence must be such!
            System.out.println("--------Initialize Context of Application--------");
            config = new ConfigurationApplicationProperties();
            System.setProperty(DIR_APP_KEY, getPathAppDirectory().toString());
            System.setProperty(DIR_SERIALIZE_KEY, getPathSerializeDirectory().toString());
            // this very importer because file applog4j2.xml exist text, which contains property key from application.properties
            // So I must add this key=value from application.properties earlier than will be performed logger configuration
            System.setProperty(DIR_LOGS_KEY, getPathLogsDirectory().toString());
            loggerConfiguration = new LoggerConfiguration();
            localsConfiguration = new LocalsConfiguration();
            themeAppConfiguration = new ThemeAppConfiguration();
            log = LogManager.getLogger(TicketGeneratorUtil.class);
            AppThemeManager.updateTheme();
            log.info("Completed initialize context of application");
        } catch (Throwable ex) {
            showAlertDialog(ex);
            throw new RuntimeException();
        }
    }

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
    public static ThemeApp getThemeAppDefault() {
        ThemeApp def = ThemeApp.LIGHT;
        ThemeApp theme = def;
        String value = Strings.toUpperCase(config.getThemeAppDef().orElseGet(() -> {
            log.warn("key:{} don't specified in configuration file.  The default value will be used", THEME_APP_DEF_KEY);
            return def.toString();
        }));
        try {
            theme = ThemeApp.valueOf(value);
        } catch (IllegalArgumentException ex) {
            log.warn("Specified  in property file value: {} of theme application don't matched with exists values ThemeApp. Set default value: {} ",
                    value, theme);
        }
        return theme;
    }

    /**
     * Default value define in next events:
     * <p>
     * Firstly check if was specified <b>SYSTEM OF PROPERTIES</b>
     * if is present than return this value.
     * <p>
     * <i>ELSE</i> Next check if was specified <b>APPLICATION OF PROPERTIES</b> in <u>application.properties</u>
     * if is present than return this value.
     * <p>
     * <i>ELSE</i> return Locale.getDefault()
     *
     * @implNote if you want to create <b>exe file</b> I can specify this system of property {@link #SYS_PROP_DEFAULT_LOCALE}, when this app will be run.
     * If programmer don't specify this system properties when defaultLocale <code>must be necessary specified in <u>application.properties</u></code>
     * Else return <b>Locale.getDefault</b>
     * <p>
     * <code>However if Locale.getDefault don't supported this of application then will be throw exception when will be execute configuration of locale</code>
     * So you should specify correct locale in <u>application.properties</u>
     */
    public static Locale getDefaultLocale() {
        return Optional.ofNullable(System.getProperty(SYS_PROP_DEFAULT_LOCALE)).map(Locale::new)
                .or(() -> config.getDefaultLocale().map(Locale::new))
                .orElseGet(Locale::getDefault);
    }

    public static Long getDelayStepGeneration() {
        return config.getDelayStepGeneration().orElse(100L);
    }

    public static String getFileSeparator() {
        return System.getProperty("file.separator");
    }


    public static void sleepFor(long millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
            } else if (t instanceof TicketGeneratorException | t instanceof LoggerException | t instanceof LocalizerException | t instanceof IOException) {
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
