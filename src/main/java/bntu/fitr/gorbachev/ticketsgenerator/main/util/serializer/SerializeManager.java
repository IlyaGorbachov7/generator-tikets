package bntu.fitr.gorbachev.ticketsgenerator.main.util.serializer;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SerializeManager {
    private static final Map<Path, Serializer> context = new ConcurrentHashMap<>(4);

    private static final List<SerializeListener> handlers = new ArrayList<>();

    public static <T extends Serializable> Serializer getSerializer(Path targetDirectory) throws AccessDeniedException {
        context.putIfAbsent(targetDirectory.toAbsolutePath(), new Serializer(targetDirectory.toAbsolutePath()));
        return context.get(targetDirectory);
    }

    public static void addListener(SerializeListener listener) {
        handlers.add(listener);
    }

    public static void removeListener(SerializeListener listener) {
        handlers.remove(listener);
    }


    public static void runSerialize() throws IOException {
        for (var h : handlers) {
            h.serialize(getSerializer(h.serializeTargetDirectory()));
        }
    }

    public static void runDeserialize() throws IOException {
        for(var h: handlers){
            h.deserialize(getSerializer(h.serializeTargetDirectory()));
        }
    }
}
