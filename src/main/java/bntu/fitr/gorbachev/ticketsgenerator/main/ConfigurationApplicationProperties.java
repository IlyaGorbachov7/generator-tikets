package bntu.fitr.gorbachev.ticketsgenerator.main;

import bntu.fitr.gorbachev.ticketsgenerator.main.exep.TicketGeneratorException;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.FilesUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.PropertiesManagerBase;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.ReadableProperties;
import lombok.Getter;

/**
 * Problem when I had to solve:
 * <p>
 * this very importer because file log4j2.xml exist text, which contains property key from application.properties
 * So I must add this key=value from application.properties earlier than will be performed logger configuration
 */
@Getter
public class ConfigurationApplicationProperties {
    public static final String DIR_APP_KEY = "app.directory";
    public static final String DIR_SERIALIZE_KEY = "app.directory.serializer";

    public static final String DIR_LOGS_KEY = "app.directory.logs";

    public static final String THEME_APP_DEF_KEY = "app.theme.default";

    private final String sourceFile;
    private final ReadableProperties appProp;

    public ConfigurationApplicationProperties(String sourceFile) throws TicketGeneratorException {
        this.sourceFile = sourceFile;
        try {
            appProp = PropertiesManagerBase.builder().build(FilesUtil.resolveSourceLocation(sourceFile));
            System.setProperty(DIR_APP_KEY, appProp.getValue(DIR_APP_KEY));
            System.setProperty(DIR_SERIALIZE_KEY, appProp.getValue(DIR_SERIALIZE_KEY));
            // this very importer because file log4j2.xml exist text, which contains property key from application.properties
            // So I must add this key=value from application.properties earlier than will be performed logger configuration
            System.setProperty(DIR_LOGS_KEY, appProp.getValue(DIR_LOGS_KEY));
        } catch (Exception e) {
            throw new TicketGeneratorException(e);
        }
    }


    public String getDirApp() {
        return appProp.getValue(DIR_APP_KEY, null);
    }

    public String getDirSerialize() {
        return appProp.getValue(DIR_SERIALIZE_KEY);
    }


    public String getDirLogs() {
        return appProp.getValue(DIR_LOGS_KEY, null);
    }

    public String getThemeAppDef() {
        return appProp.getValue(THEME_APP_DEF_KEY, null);
    }
}
