package bntu.fitr.gorbachev.ticketsgenerator.main.util.serializer;

import java.io.Serializable;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SerializeManager {
    private static final Map<Path, Serializer> context = new ConcurrentHashMap<>(4);

    public static <T extends Serializable> Serializer getSerializer(Path targetPath) throws AccessDeniedException {
        context.putIfAbsent(targetPath.toAbsolutePath(), new Serializer(targetPath.toAbsolutePath()));
        return context.get(targetPath);
    }
}
