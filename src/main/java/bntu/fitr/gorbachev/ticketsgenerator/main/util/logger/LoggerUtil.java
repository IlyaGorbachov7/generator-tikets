package bntu.fitr.gorbachev.ticketsgenerator.main.util.logger;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.FilesUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Objects;

public class LoggerUtil {
    public static final String SYS_PROP_LOG_CONFIG = "configuration.log4j";
    // how config: https://stackoverflow.com/a/30132945/14707802
    // how config2: https://poe.com/s/3MyIu5G2LTF3pP9vjGfS

    public static void init() throws LoggerException {
        try {
            // Path of the configuration file you maybe received outside jar of file or inside jar of file.
            // To specify path for configuration log4j you should specify property: log4j.configuration from JVM options
            // For example: jar
            String configFile = Objects.requireNonNullElse(System.getProperty(SYS_PROP_LOG_CONFIG), "/resources/applog4j2.xml");
            ConfigurationSource config = new ConfigurationSource(Objects.requireNonNull(FilesUtil.resolveSourceLocation(configFile)));
            Configurator.initialize(null, config);
        } catch (Exception ex) {
            throw new LoggerException(ex);
        }

    }

    public static Logger getRootLogger() {
        return LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    }

}