package bntu.fitr.gorbachev.ticketsgenerator.main.util.logger;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.FilesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Slf4j(topic = "bntu.fitr.gorbachev.ticketsgenerator.main")
public class LoggerUtil {
    public static final String SYS_PROP_LOG_CONFIG = "configuration.log4j";
    // how config: https://stackoverflow.com/a/30132945/14707802
    // how config2: https://poe.com/s/3MyIu5G2LTF3pP9vjGfS

    public static void init() throws LoggerException {
        try {
            // Path of the configuration file you maybe received outside jar of file or inside jar of file.
            // To specify path for configuration log4j you should specify property: log4j.configuration from JVM options
            // For example: jar
            String configFile = Objects.requireNonNullElse(System.getProperty(SYS_PROP_LOG_CONFIG), "/resources/log4j2.xml");
            ConfigurationSource config = new ConfigurationSource(Objects.requireNonNull(FilesUtil.resolveSourceLocation(configFile)));
            Configurator.initialize(LoggerUtil.class.getClassLoader(), config);
        } catch (Exception ex) {
            throw new LoggerException(ex);
        }

    }
}