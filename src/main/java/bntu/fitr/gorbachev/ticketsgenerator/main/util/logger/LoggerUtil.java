package bntu.fitr.gorbachev.ticketsgenerator.main.util.logger;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.IOException;
import java.util.Objects;

@Slf4j(topic = "bntu.fitr.gorbachev.ticketsgenerator.main")
public class LoggerUtil {
    // how config: https://stackoverflow.com/a/30132945/14707802
    // how config2: https://poe.com/s/3MyIu5G2LTF3pP9vjGfS

    public static void init() throws IOException {
            String configFile = Objects.requireNonNullElse(System.getProperty("log4j.configuration"), "/resources/log4j2.xml");
            ConfigurationSource config = new ConfigurationSource(Objects.requireNonNull(LoggerUtil.class.getResourceAsStream(configFile)));
            Configurator.initialize(LoggerUtil.class.getClassLoader(), config);
    }
}