package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.resolver;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.*;
import java.util.Base64;

/**
 * Метод `ByteArrayOutputStream#toString()` возвращает строку, используя стандартную кодировку (обычно UTF-8). Однако объектный поток (`ObjectOutputStream`) записывает бинарные данные, а не текстовые, и поэтому просто преобразовывать бинарные данные в строку через `.toString()` приведет к ошибке при попытке десериализации.
 * <p>
 * Для корректной работы вам нужно использовать байтовые массивы и правильно обрабатывать их как бинарные данные.
 * <p>
 * Это должно решить проблему с исключением `StreamCorruptedException`, так как теперь данные будут корректно сериализованы и десериализованы.
 *
 * @version 06.07.2024
 */
@Builder(builderClassName = "Builder")
@AllArgsConstructor
public class DeserializeResolverToObject implements Resolver<Serializable> {

    @Override
    public Serializable assemble(String base64String) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
                new ByteArrayInputStream(Base64.getDecoder().decode(base64String)))) {
            return (Serializable) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String assembleToString(Serializable object) {
        try (ByteArrayOutputStream binaryDataByte = new ByteArrayOutputStream(50);
             ObjectOutputStream outputStream = new ObjectOutputStream(binaryDataByte)) {
            outputStream.writeObject(object);
            outputStream.flush();
            String base64String = Base64.getEncoder().encodeToString(binaryDataByte.toByteArray());
            return base64String;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
