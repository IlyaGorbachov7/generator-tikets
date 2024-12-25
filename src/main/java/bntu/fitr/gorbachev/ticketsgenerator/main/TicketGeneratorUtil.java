package bntu.fitr.gorbachev.ticketsgenerator.main;

import bntu.fitr.gorbachev.ticketsgenerator.main.exep.TicketGeneratorException;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.EmailSender;
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
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;

import static bntu.fitr.gorbachev.ticketsgenerator.main.ConfigurationApplicationProperties.*;

/*If out want specify @Log41 or @Slf4j annotation, then logger will be don't worked because Logger now don't initialized */
public class TicketGeneratorUtil {
    private static final String SYS_PROP_DEFAULT_LOCALE = DEFAULT_LOCALE_KEY;

    @Getter
    private static final ConfigurationApplicationProperties config;

    @Getter
    private static final LoggerConfiguration loggerConfiguration;

    @Getter
    private static final LocalsConfiguration localsConfiguration;

    @Getter
    private static final ThemeAppConfiguration themeAppConfiguration;

    private static Logger log;

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
            themeAppConfiguration = new ThemeAppConfiguration();
            AppThemeManager.updateTheme();
            localsConfiguration = new LocalsConfiguration();
            log = LogManager.getLogger(TicketGeneratorUtil.class);
            log.info("Completed initialize context of application");
        } catch (Throwable ex) {
            if (Objects.nonNull(log)) log.error("", ex);
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

    public static File getFileSerializeDirectory() throws NotAccessToFileException {
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

    public static File getFileLogsDirectory() throws NotAccessToFileException {
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
        return config.getDelayStepGeneration().orElse(500L);
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
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream errorPrint = new PrintStream(bos, true, StandardCharsets.UTF_8);
        t.printStackTrace(errorPrint);
        JPanel panel = new JPanel(new BorderLayout());
        JLabel lblMessage = new JLabel();
        JTextArea textArea = new JTextArea();
        textArea.setText(bos.toString(StandardCharsets.UTF_8));
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 250));
        scrollPane.getVerticalScrollBar().setVisible(true);
        panel.add(lblMessage, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        sendExceptionOnMail(textArea.getText());
        boolean find = true;
        while (find) {
            if (t instanceof NotAccessToFileException ex) {
                log.error(ex.getMessage());
                lblMessage.setText(String.format("""
                        No read or write access or not found:
                        %s
                        """, ex.getFilePath()));
                JOptionPane.showMessageDialog(null, panel,
                        "Access undefined", JOptionPane.ERROR_MESSAGE);
                find = false;
            } else if (t instanceof TicketGeneratorException | t instanceof LoggerException | t instanceof LocalizerException | t instanceof IOException) {
                Throwable cause = t.getCause();
                if (cause instanceof NotAccessToFileException) {
                    t = cause;
                    continue;
                }
                lblMessage.setText("Undefined Exception: " + t);
                JOptionPane.showMessageDialog(null, panel, "Error", JOptionPane.ERROR_MESSAGE);
                find = false;
            } else if (t instanceof Throwable) {
                lblMessage.setText("Undefined Exception: " + t);
                JOptionPane.showMessageDialog(null, panel, "Error", JOptionPane.ERROR_MESSAGE);
                find = false;
            }
        }
    }

    public static Runnable handlerExceptionUIAlert(Runnable runnable) {
        return () -> {
            try {
                runnable.run();
            } catch (Throwable e) {
                if (log != null) log.error("", e);
                showAlertDialog(e);
            }
        };
    }

    public static <R> Callable<R> handlerExceptionUIAlert(Callable<R> runnable) {
        return () -> {
            try {
                return runnable.call();
            } catch (Throwable e) {
                if (log != null) log.error("", e);
                showAlertDialog(e);
            }
            return null;
        };
    }

    public static void sendExceptionOnMail(String textException) {
        String tfEmail = TicketGeneratorUtil.getConfig().getUsername().get();
        TicketGeneratorUtil.getConfig().getMailEnable().ifPresent(enableV -> {
            if (enableV) {
                try {
                    EmailSender.sendEmail(tfEmail, "TicketGenerator Exeption", String.format("""
                            Date: %s
                            Exception:
                            %s
                            """, LocalDateTime.now(), textException));
                    if (Objects.nonNull(log)) log.info("Exception Mail send. Successfully!");
                } catch (Exception e) {
                    if (Objects.nonNull(log)) log.error("Mail don't sent. ", e);
                }
            } else {
                if (Objects.nonNull(log)) log.info("Send Exception Mail will be turn off");
            }
        });
    }
}
