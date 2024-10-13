package bntu.fitr.gorbachev.ticketsgenerator.main.util.logger;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.FilesUtil;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Конфигурационный класс полезны тем, что в {@link bntu.fitr.gorbachev.ticketsgenerator.main.TicketGeneratorUtil}
 * я могу получить доступ непосредственно к объекту этой конфигурации.
 * Это позволяет быть более гибкой и иметь больше контроля над объектами. Так же это позволяет собрать все конфиги
 * в один класс и через getter получать необходимый config.
 * <p>
 * Раньше я конфиги писал в static {} контексте. И при загрузке класса происходила конфигурация. НОО был <b>недостаток</b>
 * <i>Я больше не имел доступа к этой конфигурации <b>а верее к ОБЪЕКТУ-конфигурации</b></i>
 * <p>
 * Таким образом <code>ПРАВИЛО</code> такое:
 * <p>
 * НЕ создавай инициализацию в статическом контексте для конфигурации какой-то единицы framework-а: Local, Logger, Serialize,
 * ThemeApp. <b>Лучше создай 'SameUnit'Configuration< и в конструкторе задай конфигурацию </b>
 * <p>
 * <b><code>Яркий пример</code></b> это {@link bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.LocalsConfiguration} и получение этого
 * instance в TicketGeneratorUtils
 */
public class LoggerConfiguration {
    public final String SYS_PROP_LOG_CONFIG = "configuration.log4j";

    public final String DEF_CONFIG_FILE_PATH = "applog4j2.xml";
    // how config: https://stackoverflow.com/a/30132945/14707802
    // how config2: https://poe.com/s/3MyIu5G2LTF3pP9vjGfS

    // TODO тут вообще можно было задать String параметр для указания пути к  файлу и вынести SYS_PROP_LOG_CONFIG в TicketGeneratorUtil
    // TODO и этот класс просто оставить гибким System.getProperty(SYS_PROP_LOG_CONFIG) эту логику вынести в TicketGeneratorUtil
    public LoggerConfiguration() throws LoggerException {
        try {
            // Path of the configuration file you maybe received outside jar of file or inside jar of file.
            // To specify path for configuration log4j you should specify property: log4j.configuration from JVM options
            // For example: jar
            String configFile = Objects.requireNonNullElse(System.getProperty(SYS_PROP_LOG_CONFIG), DEF_CONFIG_FILE_PATH);
            ConfigurationSource config = new ConfigurationSource(Objects.requireNonNull(FilesUtil.resolveSourceLocationAsInputStream(configFile)));
            Configurator.initialize(null, config);
        } catch (Exception ex) {
            throw new LoggerException(ex);
        }
    }

    public Logger getRootLogger() {
        return LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    }

}
