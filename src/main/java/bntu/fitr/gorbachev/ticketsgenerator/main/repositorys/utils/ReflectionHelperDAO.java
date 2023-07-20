package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.utils;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.AbstractDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl.AbstractDAOImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Objects;

public class ReflectionHelperDAO {

    public static <T> Class<T> getEntityClazzFromGenericDaoType(Class<? extends AbstractDAO> daoClazz) {
        ParameterizedType paramType = Objects.requireNonNullElseGet(findSupperGenericClass(daoClazz, AbstractDAOImpl.class),
                () -> findSupperGenericInterface(daoClazz, AbstractDAO.class));

        return null;
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
            if(paramType != null && Arrays.stream(paramType.getActualTypeArguments()).anyMatch(type -> type instanceof TypeVariable)){
                if (Arrays.stream(((ParameterizedType) classWhere.getGenericSuperclass()).getActualTypeArguments()).anyMatch(type -> type instanceof TypeVariable)) {
                    return paramType;
                }
                return (ParameterizedType) classWhere.getGenericSuperclass();
            }
        }

        return paramType;
    }
}