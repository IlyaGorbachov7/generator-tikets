package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.utils;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.AbstractDAO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.*;
import java.util.stream.Stream;

public class ReflectionHelperDAO {

    @SuppressWarnings("unchecked")
    public static <T> Class<T> extractEntityClassFromDao(Class<? extends AbstractDAO> daoClazz) {
        ParameterizedType paramType = Objects.requireNonNull(findSupperGenericClassOrInterface(daoClazz, AbstractDAO.class));
        if (paramType.getActualTypeArguments().length == 2) {
            Arrays.stream(paramType.getActualTypeArguments()).findFirst().ifPresent((type -> {
                if (type instanceof TypeVariable) {
                    throw new IllegalArgumentException("DAP class don't have certain definition Entity class");
                }
            }));

            return (Class<T>) paramType.getActualTypeArguments()[0]; // position entity class is index = 0
        } else {
            throw new IllegalArgumentException("DAO class may be only with 2 generic of type");
        }
    }

    /**
     * This method extract entity name from {@link Entity} annotation.
     * <p>
     * If annotation don't specified under Entity Class, then return null, because any entity class
     * should be having {@link Entity}.
     * <p>
     * if annotation is specified, however attribute <b>name - don't specified</b>, then return simple name of class,
     * else return specified value from attribute <i>name</i>
     */
    public static String extractEntityNameFromJakartaAnnEntity(Class<?> clazzEntity) {
        Entity annEntity = clazzEntity.getAnnotation(Entity.class);
        if (!Objects.isNull(annEntity)) {
            return annEntity.name().isBlank() ? clazzEntity.getSimpleName() : annEntity.name();
        }
        return null;
    }

    /**
     * Extracting <b>column name of table</b> from {@link Column} annotation specified under field the entity class. If {@link Column} annotation don't specified, then return fieldName
     * in corresponding with JPA specification.
     */
    public static String extractColumnNameFromJakartaAnnColumn(Class<?> clazzEntity, String fieldName) {
        Field field = getFieldFoundByFieldName(clazzEntity, fieldName);
        Column annColumn = field.getAnnotation(Column.class);
        return !Objects.isNull(annColumn) ? !annColumn.name().isBlank() ? annColumn.name()
                : field.getName() : field.getName();
    }

    public static <V> V getValueFromFieldFindByName(Object obj, String fieldName) {
        Class<?> clazz = obj.getClass();
        Field field = getFieldFoundByFieldName(clazz, fieldName);
        return getValueFromField(field, obj);
    }

    public static <V> V getValueFromFieldFindByAnnotation(Object obj, Class<? extends Annotation> clazzAnnotation)
            throws RuntimeException {
        Class<?> clazz = obj.getClass();
        List<Field> fields = getFieldsFoundByAnnotation(clazz, clazzAnnotation);
        if (fields.isEmpty()) {
            throw new RuntimeException("Field with given =annotation no found");
        }
        if (fields.size() > 1) {
            throw new RuntimeException(String.format("""
                    Should be specified only one annotation under field class.
                    However was found %d fields with given annotation""", fields.size()));
        }
        return getValueFromField(fields.get(0), obj);
    }

    public static String getFieldNameFindByAnnotation(Class<?> entityClazz, Class<? extends Annotation> annClazz) {
        List<Field> fields = getFieldsFoundByAnnotation(entityClazz, annClazz);
        if (fields.isEmpty()) {
            throw new RuntimeException("Field with given =annotation no found");
        }
        if (fields.size() > 1) {
            throw new RuntimeException(String.format("""
                    Should be specified only one annotation under field class.
                    However was found %d fields with given annotation""", fields.size()));
        }
        return fields.get(0).getName();
    }

    private static Field getFieldFoundByFieldName(Class<?> clazz, String fieldName) {
        Field field = null;
        while (!clazz.equals(Object.class)) {
            Optional<Field> optionalField = Arrays.stream(clazz.getDeclaredFields())
                    .dropWhile(f -> !f.getName().equals(fieldName))
                    .findFirst();
            if (optionalField.isPresent()) {
                field = optionalField.get();
                break;
            }
            clazz = clazz.getSuperclass();
        }
        if (Objects.isNull(field)) {
            throw new NoSuchElementException(
                    new NoSuchFieldException(String.format("Field with field name:%s  not found", fieldName)));
        }
        return field;
    }

    private static List<Field> getFieldsFoundByAnnotation(Class<?> clazz, Class<? extends Annotation> clazzAnn) {
        Stream.Builder<Field> streamBuilder = Stream.builder();
        while (!clazz.equals(Object.class)) {
            Arrays.stream(clazz.getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(clazzAnn))
                    .forEach(streamBuilder);
            clazz = clazz.getSuperclass();
        }
        return streamBuilder.build().toList();
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

    public static ParameterizedType findSupperGenericClass(Class<?> clazzWhere, Class<?> clazzThat) {
        if (clazzWhere.isInterface()) {
            throw new IllegalArgumentException("args: (clazzWhere) should be class of type");
        }
        if (clazzThat.getTypeParameters().length == 0) {
            throw new IllegalArgumentException("arg: clazzThat should be generic of type");
        }
        while (clazzWhere != Object.class) {
            Class<?> superclass = clazzWhere.getSuperclass();
            if (superclass == clazzThat) {
                return (ParameterizedType) clazzWhere.getGenericSuperclass();
            }
            clazzWhere = superclass;
        }

        return null;
    }

    public static ParameterizedType findSupperGenericInterface(Class<?> clazzOrInterfaceWhere, Class<?> interfaceThat) {
        if (interfaceThat.getTypeParameters().length == 0) {
            throw new IllegalArgumentException("arg: interfaceThat should be generic of type");
        }
        if (!interfaceThat.isInterface()) {
            throw new IllegalArgumentException("arg: interfaceThat should be interface of type");
        }
        Class<?>[] clazzInterfaces = clazzOrInterfaceWhere.getInterfaces();
        int i = 0;
        for (var interfaceWhere : clazzInterfaces) {
            ParameterizedType paramType = findSupperGenericInterface(interfaceWhere, interfaceThat);
            if (paramType != null) {
                return paramType;
            }
            // assume that paramType is null
            if (interfaceWhere == interfaceThat) {// interfaceThat == interfaceWhere == getGenericInterfaces()[i]
                return (ParameterizedType) clazzOrInterfaceWhere.getGenericInterfaces()[i];
            }
            ++i;
        }


        return null;
    }

    /**
     * This method will be search for the passed type: <i>clazzThat</i> - is generic type, inside <i>classWhere</i>.
     * Searching will be return ParameterizedType given type or subtype.
     *
     * <p>
     * <b>Return type have several case ParameterizedType:</b>
     * <p>
     * 1) Passed type: <i>clazzThat</i> or hir subtype.
     * <p>
     * 2) null - if passed type not found or  <i>clazzThat</i> == <i>clazzWhere</i>
     * <p>
     * 3) Type: AnyClassName< T,T1 > - if passed type: <i>classThat</i> is found, however certain definintion
     * generic type absent
     *
     * @param classWhere class where will be search given <i>classThat</i>.
     * @param classThat  may be or class or interface. <b>This class or interface should be generic type</b>
     */
    public static ParameterizedType findSupperGenericClassOrInterface(Class<?> classWhere, Class<?> classThat) {
        ParameterizedType paramType;
        if (classThat.isInterface()) {
            if (classWhere == Object.class) {
                return null;
            }
            paramType = findSupperGenericInterface(classWhere, classThat);
            if (paramType == null) {
                paramType = findSupperGenericClassOrInterface(classWhere.getSuperclass(), classThat);
                if (paramType != null && Arrays.stream(paramType.getActualTypeArguments()).anyMatch(type -> type instanceof TypeVariable)) {
                    if (Arrays.stream(((ParameterizedType) classWhere.getGenericSuperclass()).getActualTypeArguments()).anyMatch(type -> type instanceof TypeVariable)) {
                        return paramType;
                    }
                    return (ParameterizedType) classWhere.getGenericSuperclass();
                }
            }
        } else {
            paramType = findSupperGenericClass(classWhere, classThat);
            if (paramType != null && Arrays.stream(paramType.getActualTypeArguments()).anyMatch(type -> type instanceof TypeVariable)) {
                if (Arrays.stream(((ParameterizedType) classWhere.getGenericSuperclass()).getActualTypeArguments()).anyMatch(type -> type instanceof TypeVariable)) {
                    return paramType;
                }
                return (ParameterizedType) classWhere.getGenericSuperclass();
            }
        }

        return paramType;
    }
}