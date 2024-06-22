package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.reflectionapi;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.reflectapi.ann.TableViewUI;

import javax.swing.text.TableView;
import java.util.Arrays;
import java.util.Objects;

public class ReflectionListDataBaseHelper {

    public static void checkClassesOnTheModelViewTable(Class<?> clazz) throws RuntimeException {
        Objects.requireNonNull(clazz);
    }

    public static void checkClassesOnTheModelViewTable(Class<?>[] classes) throws RuntimeException {
        Objects.requireNonNull(classes);
        for (Class<?> clazz : classes) {
            checkClassesOnTheModelViewTable(clazz);
        }
    }

    public static String extractTableViewName(Class<?> clazz) throws RuntimeException {
        checkClassesOnTheModelViewTable(clazz);
        TableViewUI ann = clazz.getAnnotation(TableViewUI.class);
        return ann.name() != null && !ann.name().isBlank() ? ann.name() : clazz.getSimpleName();
    }

    public static String[] extractTableViewName(Class<?>[] classes) throws RuntimeException {
        return Arrays.stream(classes).map(ReflectionListDataBaseHelper::extractTableViewName).toArray(String[]::new);
    }
}
