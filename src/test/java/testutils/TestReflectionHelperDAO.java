package testutils;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.AbstractDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.UniversityDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl.*;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.University;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.utils.ReflectionHelperDAO;
import model.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Stream;

public class TestReflectionHelperDAO {

    Class<? extends AbstractDAO> daoClazz = UniversityDAOImpl.class;


    @Test
    void test1() {
        Assertions.assertTrue(AbstractDAO.class.isAssignableFrom(daoClazz));

    }


    @Test
    void test2() {
        Type[] types = daoClazz.getGenericInterfaces();
        System.out.println(Arrays.toString(types));
        Assertions.assertTrue(types.length > 0);
    }


    @Test
    void test3() {

        Type type = daoClazz.getGenericSuperclass();
        System.out.println(type);

        Assertions.assertTrue(ParameterizedType.class.isInstance(type));
        ParameterizedType paraType = (ParameterizedType) type;


        Type typeEntity = paraType.getActualTypeArguments()[0];
        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertEquals(University.class, Class.forName(typeEntity.getTypeName()));
        });
    }

    @Test
    void test4() {
        Assertions.assertTrue(Stream.of(daoClazz.getInterfaces())
                .anyMatch(AbstractDAO.class::isAssignableFrom)
        );
    }

    @Test
    void test5() {
        Class<?> subclazz = daoClazz.getSuperclass();
        System.out.println(subclazz);
        Assertions.assertEquals(subclazz, AbstractDAOImpl.class);
    }

    @Test
    void test6() {
        Class<?> interfaceUniv = UniversityDAO.class;
        Class<?>[] supperIn = interfaceUniv.getInterfaces();
        System.out.println(Arrays.toString(supperIn));

        Type[] paramTypes = interfaceUniv.getGenericInterfaces();
        System.out.println(Arrays.toString(paramTypes));


    }

    @Test
    void test7() {
        Class<UniversityDAO> clazz = UniversityDAO.class;
        Type[] types = clazz.getInterfaces();
        System.out.println(Arrays.toString(types));
        System.out.println(types.length);
        System.out.println();

        types = clazz.getGenericInterfaces();
        System.out.println(Arrays.toString(types));
        System.out.println(types.length);
    }

    @Test
    void test8() {
    }

    //----------------------------------------------------------------------------------

    @Test
    void testFindSupperClass() {
        Assertions.assertNull(
                ReflectionHelperDAO.findSupperGenericClass(UniversityDAOImpl.class,
                        AbstractDAO.class)
        );
    }

    @Test
    void testFindInterface() {
        ParameterizedType paramType = null;

        paramType = ReflectionHelperDAO.findSupperGenericInterface(UniversityDAOImpl.class,
                AbstractDAO.class);
        Assertions.assertNotNull(paramType);
        System.out.println(paramType);


        paramType = ReflectionHelperDAO.findSupperGenericInterface(UniversityAbstractDAOImpl.class,
                AbstractDAO.class);
        Assertions.assertNull(paramType);
        System.out.println(paramType);

    }

    @ParameterizedTest
    @ValueSource(classes = {
            UniversityDAOImpl.class,
            UniversityAbstractDAOImpl.class,
            UniversityAbstractDAPImpl2.class,
            UniversityAbstractDAOImpl3.class
    })
    void testFindSupperGenericClassOrInterface1(Class<?> clazz) {
        ParameterizedType paramType = null;
        paramType = ReflectionHelperDAO.findSupperGenericClassOrInterface(clazz, AbstractDAO.class);
        System.out.println(paramType);
    }
}
