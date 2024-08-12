package bntu.fitr.gorbachev.ticketsgenerator.main.util;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class ReflectionUtil {

    public static <T extends Serializable> T newInstance(Class<T> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Field getFieldByName(Class<?> clazz, String fieldName) {
        try {
            Field f = clazz.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f;
        } catch (NoSuchFieldException e) {
            System.out.printf("Reflection API: NoSuchFieldException: field: %s , class: %s %n", fieldName, clazz);
            return null;
        }
    }

    /**
     * Чтобы получить значение поля как объекта, можно использовать метод get() и передать ему экземпляр объекта.
     * <b>Для статических полей передайте null.</b>
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFieldObjectByName(Class<?> clazz, Object obj, String fieldName) {
        try {
            Field f = getFieldByName(clazz, fieldName);
            if (f == null) return null;
            return (T) f.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    /*Deep search annotation. This method will search annotation inside another declared annotation*/

    public static boolean containsAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        if (clazz.isAnnotationPresent(annotation)) {
            return true;
        } else {
            for (Annotation annAnn : Arrays.stream(clazz.getAnnotations())
                    /*we filter the annotation in case this annotation is above itself. For example see @Documented @Retention
                     * @Target */
                    .filter(tmpAnn -> tmpAnn.annotationType().equals(clazz)).toArray(Annotation[]::new)) {
                if (containsAnnotation(annAnn.annotationType(), annotation)) {
                    return true;
                }
            }
            return false;
        }

    }

    public static void checkContainsAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        if (!containsAnnotation(clazz, annotation)) {
            throw new IllegalArgumentException(String.format("Class %s should have annotation %s", clazz, annotation));
        }
    }

    /**
     * @see java.lang.reflect.Modifier
     */
    public static void checkMatchModifier(int modifier, Field field) {
        if (modifier != field.getModifiers()) {
            throw new IllegalArgumentException(String.format("Field: %s don't matching modifier: %s. See class %s",
                    field.getName(), modifier, Modifier.class));
        }
    }
}
