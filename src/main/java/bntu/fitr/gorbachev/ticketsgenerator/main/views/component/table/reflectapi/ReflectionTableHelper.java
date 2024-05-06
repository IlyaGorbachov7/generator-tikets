package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.reflectapi;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.reflectionapi.ReflectionListDataBaseHelper;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.reflectapi.ann.ColumnViewUI;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

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
            ColumnViewUI ann = field.getDeclaredAnnotation(ColumnViewUI.class);
            return ann.name() != null && !ann.name().isBlank() ? ann.name() : field.getName();
        }).toArray(String[]::new);
    }

    private static Field[] getFieldsByAnn(Class<?> clazz, Class<ColumnViewUI> clazzAnn) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter((field) -> Objects.nonNull(field.getAnnotation(clazzAnn)))
                .toArray(Field[]::new);
    }

    private static <V> V getValueFromField(Field field, Object obj) {
        field.setAccessible(true);
        try {
            @SuppressWarnings("unchecked")
            V value = (V) field.get(obj);
            return value;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object[][] extractDataAndTransformToClass(List<?> data, Class<?> toClazz) {
        Objects.requireNonNull(data);
        Objects.requireNonNull(toClazz);
        if (data.isEmpty()) return new Object[0][];
        checkInstancesComplianceOnTheClass(data, toClazz);

        List<Object[]> listData = new ArrayList<>(data.size());
        Field[] fieldsByAnn = getFieldsByAnn(toClazz, ColumnViewUI.class);
        data.forEach(row -> {
            listData.add(Arrays.stream(fieldsByAnn)
                    .map(field -> getValueFromField(field, row))
                    .toArray());
        });

        return listData.toArray(Object[][]::new);
    }

    private static void checkInstancesComplianceOnTheClass(List<?> data, Class<?> toClazz) throws RuntimeException {
        data.forEach(ins -> {
            if (ins.getClass() != toClazz) {
                throw new RuntimeException(
                        String.format("Element of the list is incorrect the class: %s. Should be class : %s ",
                                ins.getClass(), toClazz));
            }
        });
    }
}
