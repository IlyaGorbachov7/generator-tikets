package testutils;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.ReflectionUtil;
import handlerann.VariableSourceProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;

public class ReclectinUtllTest {

    @Test
    void test(){
        Assertions.assertTrue(ReflectionUtil.containsAnnotation(ClassA.class, Ann1.class));
    }
    @Test
    void test2(){
        Assertions.assertTrue(ReflectionUtil.containsAnnotation(ClassA.class, ArgumentsSource.class));
    }

    @Test
    void test3(){
        Assertions.assertTrue(ReflectionUtil.containsAnnotation(ClassB.class, Ann2.class));
        Assertions.assertFalse(ReflectionUtil.containsAnnotation(ClassB.class, Ann1.class));
        Assertions.assertFalse(ReflectionUtil.containsAnnotation(ClassB.class, ArgumentsSource.class));
    }


    @Ann1
    private static class ClassA{

    }

    @Ann2
    private static class ClassB{

    }


    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @ArgumentsSource(VariableSourceProvider.class)
    private @interface Ann1{

    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    private  @interface Ann2{

    }
}
