package bntu.fitr.gorbachev.ticketsgenerator.main.util.logger;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.ReflectionUtil;
import org.slf4j.Logger;

import java.lang.reflect.Modifier;
import java.util.Objects;

public interface Slf4jLoggable {
    default void log(String msg, Object... data) {
        log(LoggerUtil.getDefaultLoggerMethod(), msg, data);
    }

    default void log(LoggerMethod loggerMethod, String msg, Object... data) {
        Logger logger = getLogger();
        switch (loggerMethod){
            case trace -> logger.trace(msg,data);
            case debug -> logger.debug(msg,data);
            case info -> logger.info(msg, data);
            case warn -> logger.warn(msg, data);
            case error -> logger.error(msg, data);
        }
    }

    private Logger getLogger() {
        Logger logger;
        Class<?> clazz = this.getClass();
        if (Objects.nonNull(ReflectionUtil.getFieldByName(clazz, getLoggerFieldName()))) {
            ReflectionUtil.checkMatchModifier(Modifier.STATIC, Objects.requireNonNull(ReflectionUtil.getFieldByName(clazz,
                    getLoggerFieldName())));
            logger = ReflectionUtil.getFieldObjectByName(clazz, null, // because field should be NULL
                    getLoggerFieldName());
        } else {
            logger = LoggerUtil.getRootLogger();
        }
        return logger;
    }

    default String getLoggerFieldName() {
        return "log";// By default, This static filed name generated from Lombok framework
    }

}
