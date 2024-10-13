package propmanag;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.impl.PropertiesManagerBase;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.resolver.RegexResolverToString;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.resolver.ResolverToArrayInt;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;

public class InitializeProperties {

    @Test
    public void test() throws AccessDeniedException, FileNotFoundException {
        PropertiesManagerBase prop = PropertiesManagerBase.builder().build(Path.of("test.properties").toFile());
        prop.getContent().forEach((key, value) -> {
            System.out.println(String.format("Key: %s, Value: %s", key, value));
        });
    }

    @Test
    public void test2() throws IOException {
        PropertiesManagerBase prop = PropertiesManagerBase.builder().setFileSore(Path.of("target/testRes.properties").toFile())
                .build(Path.of("test.properties").toFile());

        prop.save();
    }

    @Test
    public void test4() throws IOException {
        PropertiesManagerBase prop = PropertiesManagerBase.builder()
                .setResolverRegex(RegexResolverToString.builder().buildNullable())
                .setResolverToArrayInt(ResolverToArrayInt.builder().build())
                .build(Path.of("test.properties").toFile());
    }
}
