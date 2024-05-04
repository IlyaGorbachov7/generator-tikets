package handlerann;

import ann.VariableSource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.stream.Stream;

@Slf4j
public class VariableSourceProvider implements AnnotationConsumer<VariableSource>, ArgumentsProvider {
    String variableName;

    @Override
    public void accept(VariableSource variableSource) {
        variableName = variableSource.value();
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return extensionContext.getTestClass()
                .map(this::getField)
                .map(this::getValue)
                .orElseThrow(() -> new IllegalArgumentException("Filed to load test arguments"));
    }


    @SneakyThrows
    protected Field getField(Class<?> cls) {
        return cls.getDeclaredField(variableName);
    }

    @SneakyThrows
    protected Stream<? extends Arguments> getValue(Field field) {
        Type genericType = field.getGenericType();
        Class<?> collectClazz = field.getType();

        if (!(genericType instanceof ParameterizedType) ||
            !(ReflectionUtils.isAssignableTo(collectClazz, Collection.class)) &&
            !(ReflectionUtils.isAssignableTo(collectClazz, Stream.class))) {
            throw new IllegalArgumentException("Variable type should be List or Stream.class");
        }

        field.setAccessible(true);
        Object variableValue = field.get(null);
        field.setAccessible(false);
        if (ReflectionUtils.isAssignableTo(collectClazz, Collection.class)) {
            return retrieveToStream((Collection<?>) variableValue);
        }
        return retrieve((Stream<?>) variableValue);
    }

    protected Stream<Arguments> retrieveToStream(Collection<?> collection) {
        return retrieve(collection.stream());
    }

    protected Stream<Arguments> retrieve(Stream<?> stream) {
        return stream.map(this::convertToArgument);
    }


    protected Arguments convertToArgument(Object elem) {
        if (!(elem instanceof Arguments)) {
            return Arguments.of(elem);
        }
        return (Arguments) elem;
    }
}
