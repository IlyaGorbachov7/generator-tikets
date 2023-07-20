package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.utils;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.AbstractDAO;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Objects;

public class ReflectionHelperDAO {

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
        ParameterizedType paramType = null;
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