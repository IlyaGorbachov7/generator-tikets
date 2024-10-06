package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.resolver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class DeserializeResolverToObject implements Resolver<Object>{

    @Override
    public Object assemble(String value) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(value.getBytes()))) {
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
