package bntu.fitr.gorbachev.ticketsgenerator.main.util;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public class ReflectionUtil {

    public static <T extends Serializable> T newInstance(Class<T> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
