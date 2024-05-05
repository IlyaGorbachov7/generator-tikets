package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.reflectapi;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.reflectionapi.ReflectionListDataBaseHelper;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.reflectapi.ann.ColumnViewUI;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

public class ReflectionTableHelper {

    public static void checkRuntimeMistakes(Class<?> clazz) {
        ReflectionListDataBaseHelper.checkClassesOnTheModelViewTable(clazz);
        /*
        Нужно здесь полносью проверить весь класс (его поля) чтобы полностью соблюдалось требования использования данных аннотаций
         * Если в аннотации указана свойство typeView = Integer, а тип свойства класса явл. String - это ошибка
         * Если в аннотации указана свойство typeView = String, а тип свойства класса явл. Integer - это допустимо
         * Так же проверить, что это примитивные типы данных, а не какой-нибудь класс, потому что не возможно его как-то обработать*/
    }

    public static String[] extractColumnName(Class<?> clazz) {
        Field[] fields = getFieldsByAnn(clazz, ColumnViewUI.class);
        return Arrays.stream(fields).map(field -> {
            ColumnViewUI ann = field.getAnnotation(ColumnViewUI.class);
            return ann.name() != null && !ann.name().isBlank() ? ann.name() : field.getName();
        }).toArray(String[]::new);
    }

    private static Field[] getFieldsByAnn(Class<?> clazz, Class<ColumnViewUI> clazzAnn) {
        return Arrays.stream(clazz.getFields())
                .filter((field) -> Objects.nonNull(field.getAnnotation(clazzAnn)))
                .toArray(Field[]::new);
    }
}
