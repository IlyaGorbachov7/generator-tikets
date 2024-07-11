package bntu.fitr.gorbachev.ticketsgenerator.main.util;

import bntu.fitr.gorbachev.ticketsgenerator.main.Main;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;

public class Serializer {
    // TODO: что будет если пользоавтль сам удалит обект !!! Обработать случай.
    // TODO: разюить  на разные service
// TODO: тоже создать интерфейс для сирилизации обхекта слушатель и хэндлер который бы обработал когда приложение завершается
    // TODL: даже если приложение завершается с ошибкой тоже должно серилизировалться и обрабатываться !!!
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> List<T> deserialize(Class<T> clazz) throws IOException {
        return Files.find(getFileSerializableDirectory().toPath(), 1,
                        (path, attributes) -> {
                            System.out.println(path);
                            return path.getFileName().toString().startsWith(getSimpleFileName(clazz));
                        })
                .map(Path::toFile)
                .map(file -> {
                    try {
                        return (T) deserialize(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
    }

    @SuppressWarnings("unchecked")
    private static <T extends Serializable> T deserialize(File file) throws IOException {
        try (ObjectInputStream objInputStream =
                     new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            return (T) objInputStream.readObject();
        } catch (FileNotFoundException e) {
            throw new IOException(e);
        } catch (IOException e) {
            throw new IOException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static <T extends Serializable> File serialize(T object, boolean single) throws IOException {
        Class<? extends Serializable> objectClazz = object.getClass();
        if (single) {
            System.out.println("Delete : " + deleteAllFiles(objectClazz) + " byClass: " + objectClazz);
        }
        File file = new File(getFileSerializableDirectory(), getUniqueFileName(objectClazz));
        try (ObjectOutputStream objOutputStream =
                     new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            objOutputStream.writeObject(object);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return file;
    }

    @SafeVarargs
    public static <T extends Serializable> Stream<File> serialize(T... objects) throws IOException {
        Stream.Builder<File> builderStream = Stream.builder();
        for (T object : objects) {
            builderStream.accept(serialize(object, true));
        }
        return builderStream.build();
    }


    public static <T extends Serializable> String getSimpleFileName(Class<T> objectClazz) {
        return objectClazz.getSimpleName().toLowerCase(Locale.ROOT);
    }

    public static <T extends Serializable> String getUniqueFileName(Class<T> objectClazz) {
        return String.join("_", getSimpleFileName(objectClazz), UUID.randomUUID().toString());
    }

    public static <T extends Serializable> String getFileSuffix(Class<T> objectClazz, File file) {
        return file.getName().substring(getSimpleFileName(objectClazz).length() + 1);
    }


    public static File getFileSerializableDirectory() throws IOException {
        Main.checkFileCredentials(getSerializableDirectory());
        File file = getSerializableDirectory().toFile();
        file.mkdir();
        return file;
    }

    public static Path getSerializableDirectory() throws IOException {
        return Path.of(Main.getFIleAppDirectory().toString(), "serializable");
    }

    public static <T extends Serializable> long deleteAllFiles(Class<T> clazz) throws IOException {
        return Files.find(getFileSerializableDirectory().toPath(), 1,
                        (path, attributes) -> {
                            System.out.println(path.getFileName().toString());
                            return path.getFileName().toString().startsWith(getSimpleFileName(clazz));
                        })
                .map(Path::toFile)
                .map(file -> {
                    System.out.print(file);
                    boolean del = file.delete();
                    System.out.println("  delete: " + del);
                    return del;
                }).filter(isDeleted -> isDeleted).count();

    }

}
