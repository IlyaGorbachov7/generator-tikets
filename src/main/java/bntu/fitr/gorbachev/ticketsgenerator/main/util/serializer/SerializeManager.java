package bntu.fitr.gorbachev.ticketsgenerator.main.util.serializer;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

public class SerializeManager {

    private static final List<SerializeListener> handlers = new ArrayList<>();

    public static void addListener(SerializeListener listener) {
        handlers.add(listener);
    }

    public static void removeListener(SerializeListener listener) {
        handlers.remove(listener);
    }


    public static void runSerialize() throws IOException, AccessDeniedException {
        for (var h : handlers) {
            h.serialize();
        }
    }
}
