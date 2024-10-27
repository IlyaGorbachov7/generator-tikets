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

    public static final String DIR_APP_KEY = "app.directory";
    public static final String DIR_SERIALIZE_KEY = "app.directory.serializer";
    public static final String DIR_LOGS_KEY = "app.directory.logs";
    public static final String DEFAULT_LOCALE_KEY = "app.locale.default";
    public static final String THEME_APP_DEF_KEY = "app.theme.default";
    public static final String DELAY_STEP_GENERATION= "app.generator.tickets.delay.step";

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

    /**
     * return value by key or <b>null</b> if key  is don't specified
     */
    public Optional<String> getThemeAppDef() {
        return Optional.ofNullable(appProp.getValue(THEME_APP_DEF_KEY, null));
    }

    public Optional<Long> getDelayStepGeneration() {
        return Optional.of(appProp.getLong(DELAY_STEP_GENERATION, 100));
    }
}
