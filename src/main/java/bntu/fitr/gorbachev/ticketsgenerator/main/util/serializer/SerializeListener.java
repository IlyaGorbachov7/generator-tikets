package bntu.fitr.gorbachev.ticketsgenerator.main.util.serializer;

import java.io.IOException;
import java.nio.file.Path;

public interface SerializeListener {

    void serialize(Serializer serializer) throws IOException;

    default void deserialize(Serializer serializer) throws IOException {
    }

    default Path serializeTargetDirectory() {
        return Path.of(System.getProperty("user.home"), "defaultSerializeDir");
    }
}
