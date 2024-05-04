package ann;

import handlerann.VariableSourceProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ArgumentsSource(VariableSourceProvider.class)
public @interface VariableSource {
    String value();
}
