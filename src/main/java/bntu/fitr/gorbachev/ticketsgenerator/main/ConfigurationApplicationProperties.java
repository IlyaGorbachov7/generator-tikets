package bntu.fitr.gorbachev.ticketsgenerator.main;

import bntu.fitr.gorbachev.ticketsgenerator.main.exep.TicketGeneratorException;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.FilesUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.impl.PropertiesManagerBase;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.ReadableProperties;
import lombok.Getter;

import java.util.Objects;
import java.util.Optional;

/**
 * Problem when I had to solve:
 * <p>
 * this very importer because file applog4j2.xml exist text, which contains property key from application.properties
 * So I must add this key=value from application.properties earlier than will be performed logger configuration
 */
@Getter
public class ConfigurationApplicationProperties {
    public static final String SYS_PROP_APP_PROP = "configuration.application";

    public static final String APP_STORAGE_KEY = "app.storage";

    public static final String APP_NAME_KEY = "app.dir-name";

    public static final String DIR_APP_KEY = "app.directory";

    public static final String DIR_SERIALIZE_KEY = "app.directory.serializer";

    public static final String DIR_LOGS_KEY = "app.directory.logs";

    public static final String APP_ICON = "app.icon";

    public static final String USE_LOCALE_KEY = "app.locale.use";

    public static final String DEFAULT_LOCALE_KEY = "app.locale.default";

    public static final String THEME_APP_DEF_KEY = "app.theme.default";

    public static final String DELAY_STEP_GENERATION = "app.generator.tickets.delay.step";


    public static final String DATABASE_CONN_WAIT = "database.connection.wait";

    public static final String MAIL_ENABLE = "mail.enable";

    public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";

    public static final String MAIL_SMTP_TTLS = "mail.smtp.starttls.enable";

    public static final String MAIL_SMTP_HOST = "mail.smtp.host";

    public static final String MAIL_SMTP_PORT = "mail.smtp.port";

    public static final String MAIL_USERNAME = "mail.username";

    public static final String MAIL_PASSWORD = "mail.password";

    private final String sourceFile;

    private final ReadableProperties appProp;

    /**
     * You can specify path to file configuration of application in <b>JVM options</b>.
     * <p>
     * <i>key</i>=<b>configuration.application</b>
     * <p>
     * <i>value</i> may be as file path to located inside jar file and outside located
     */
    public ConfigurationApplicationProperties() throws TicketGeneratorException {
        this(Objects.requireNonNullElse(System.getProperty(SYS_PROP_APP_PROP), "application.properties"));
    }

    public ConfigurationApplicationProperties(String sourceFile) throws TicketGeneratorException {
        this.sourceFile = sourceFile;
        try {
            appProp = PropertiesManagerBase.builder().build(FilesUtil.resolveSourceLocationAsInputStream(sourceFile));
        } catch (Exception e) {
            throw new TicketGeneratorException(e);
        }
    }

    /**
     * {@link #APP_NAME_KEY} -- this key should be required.
     */
    public String getDirAppName() {
        return appProp.getValue(APP_NAME_KEY);
    }

    public Optional<String> getAppStore() {
        return Optional.ofNullable(appProp.getValue(APP_STORAGE_KEY,null));
    }

    /**
     * return value by key or <b>null</b> if key  is don't specified
     */
    public Optional<String> getDirApp() {
        return Optional.ofNullable(appProp.getValue(DIR_APP_KEY, null));
    }

    /**
     * return value by key or <b>null</b> if key  is don't specified
     */
    public Optional<String> getDirSerialize() {
        return Optional.ofNullable(appProp.getValue(DIR_SERIALIZE_KEY, null));
    }


    /**
     * return value by key or <b>null</b> if key  is don't specified
     */
    public Optional<String> getDirLogs() {
        return Optional.ofNullable(appProp.getValue(DIR_LOGS_KEY, null));
    }

    public Optional<String> getDefaultLocale() {
        return Optional.ofNullable(appProp.getValue(DEFAULT_LOCALE_KEY, null));
    }

    public Optional<String[]> getLocaleToUse() {
        return Optional.ofNullable(appProp.getValues(USE_LOCALE_KEY, null));
    }

    /**
     * return value by key or <b>null</b> if key  is don't specified
     */
    public Optional<String> getThemeAppDef() {
        return Optional.ofNullable(appProp.getValue(THEME_APP_DEF_KEY, null));
    }

    public Optional<Long> getDelayStepGeneration() {
        return Optional.of(appProp.getLong(DELAY_STEP_GENERATION, 100));
    }

    public Optional<Double> getDatabaseConnWait() {
        return Optional.of(appProp.getDouble(DATABASE_CONN_WAIT, 1)); // in minutes
    }
    public Optional<String> getAppIcon() {
        return Optional.ofNullable(appProp.getValue(APP_ICON, "pictures/iconCoursework.png"));
    }

    public Optional<Boolean> getMailEnable() {
        return Optional.of(appProp.getBoolean(MAIL_ENABLE, false));
    }

    public Optional<Boolean> getMailAuth() {
        return Optional.of(appProp.getBoolean(MAIL_SMTP_AUTH));
    }

    public Optional<Boolean> getMailTTLS() {
        return Optional.of(appProp.getBoolean(MAIL_SMTP_TTLS));
    }

    public Optional<String> getMailHost() {
        return Optional.of(appProp.getValue(MAIL_SMTP_HOST));
    }

    public Optional<Integer> getMailPort() {
        return Optional.of(appProp.getInt(MAIL_SMTP_PORT, 589));
    }

    public Optional<String> getUsername() {
        return Optional.of(appProp.getValue(MAIL_USERNAME));
    }

    public Optional<String> getPassword() {
        return Optional.of(appProp.getValue(MAIL_PASSWORD));
    }
}
