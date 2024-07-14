package bntu.fitr.gorbachev.ticketsgenerator.main.util.serializer;

import java.io.IOException;
import java.io.Serializable;

public interface SerializerListener {

    void serialize() throws IOException;

}
