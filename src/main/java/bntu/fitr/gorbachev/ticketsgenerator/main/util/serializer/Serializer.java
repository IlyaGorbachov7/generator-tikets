package bntu.fitr.gorbachev.ticketsgenerator.main.util.serializer;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.ReflectionUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.exep.NotAccessForReadToFileException;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.exep.NotAccessForWriteToFileException;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Hi This file were created weekday ago, however I customize logic now in the day 14.07.2024
 *
 * @version 14.07.2024 21:16
 */
@Log4j2
public class Serializer {

    private static final Map<Path, Serializer> context = new ConcurrentHashMap<>(4);

    public static <T extends Serializable> Serializer getSerializer(Path targetDirectory) throws AccessDeniedException {
        context.putIfAbsent(targetDirectory.toAbsolutePath(), new Serializer(targetDirectory.toAbsolutePath()));
        return context.get(targetDirectory);
    }

    public static Map<Path, Serializer> getSerializers() {
        return context.entrySet().stream().collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Getter
    private final Path targetDir;

    Serializer(Path targetDir) throws AccessDeniedException {
        this.targetDir = targetDir;
        try {
            if (Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
                log.info("Created serializer directory: {}", targetDir);
            }
        } catch (AccessDeniedException | FileAlreadyExistsException ex) {
            // See : package serializer.subsubtest1.testDirCr() and testDir()
            throw new AccessDeniedException(ex.getFile(), ex.toString(), ex.getReason());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @SafeVarargs
    public final <T extends Serializable> Stream<File> serialize(T... objects) throws IOException {
        return serialize(true, objects);
    }

    @SafeVarargs
    public final <T extends Serializable> Stream<File> serialize(boolean deleteOld, T... objects) throws IOException {
        Stream.Builder<File> builderStream = Stream.builder();
        for (T object : objects) {
            builderStream.accept(serialize(object, deleteOld));
        }
        return builderStream.build();
    }

    public <T extends Serializable> File serialize(T object, boolean deleteOld) throws IOException {
        Class<? extends Serializable> objectClazz = object.getClass();
        if (deleteOld) {
            log.info("Deleted: {} files by class: {}", deleteAllFiles(objectClazz), objectClazz);
        }
        File file = new File(getFileSerializableDirectory(), generateUniqueFileName(objectClazz));
        try (ObjectOutputStream objOutputStream =
                     new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            objOutputStream.writeObject(object);
            log.info("Serialized object: {}", object);
        }
        return file;
    }

    public <T extends Serializable> List<T> deserialize(Class<T> clazz) throws IOException {
        File[] findFiles = findByClazz(clazz).toArray(File[]::new);
        List<T> objs = new ArrayList<>(findFiles.length);
        for (var file : findFiles) {
            objs.add(deserialize(clazz, file));
            log.debug("Deserialized file: {} to object by class: {}", file, clazz);
        }
        return objs;
    }


    @SuppressWarnings("unchecked")
    private <T extends Serializable> T deserialize(Class<T> clazz, File file) throws IOException {
        try (ObjectInputStream objInputStream =
                     new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            return (T) objInputStream.readObject();
        } catch (ClassNotFoundException e) {
            log.warn("Deserialized object class from file: {} don't compared with object of class: {}. ClassNotFoundException",
                    file, clazz);
            log.warn("Deleted: {} files all old objects by class: {}", deleteAllFiles(clazz), clazz);
            T obj = ReflectionUtil.newInstance(clazz);
            log.warn("Created default object by class: {}", clazz);
            return obj;
        }
    }


    public <T extends Serializable> String getSimpleFileName(Class<T> objectClazz) {
        return objectClazz.getName();
    }

    public <T extends Serializable> String generateUniqueFileName(Class<T> objectClazz) {
        return String.join("_", getSimpleFileName(objectClazz), UUID.randomUUID().toString());
    }

    public <T extends Serializable> String getFileSuffix(Class<T> objectClazz, File file) {
        return file.getName().substring(getSimpleFileName(objectClazz).length() + 1);
    }


    public File getFileSerializableDirectory() throws IOException {
        checkFileCredentials(getTargetDir());
        File file = getTargetDir().toFile();
        file.mkdir();
        return file;
    }

    private boolean checkFileCredentials(Path file) throws NotAccessForReadToFileException, NotAccessForWriteToFileException {
        if (Files.exists(file)) {
            if (!Files.isReadable(file)) {
                throw new NotAccessForReadToFileException(file);
            }
            if (!Files.isWritable(file)) {
                throw new NotAccessForWriteToFileException(file);
            }
            return true;
        }
        return false;
    }

    public <T extends Serializable> long deleteAllFiles(Class<T> clazz) throws IOException {
        return findByClazz(clazz).map(file -> {
            System.out.print(file);
            boolean del = file.delete();
            log.debug("Deleted file {} == {} ", file, del);
            return del;
        }).filter(isDeleted -> isDeleted).count();
    }

    public <T extends Serializable> Stream<File> findByClazz(Class<T> clazz) throws IOException {
        return Files.find(getFileSerializableDirectory().toPath(), 1,
                        (path, attributes) -> {
                            boolean same = path.getFileName().toString().startsWith(getSimpleFileName(clazz));
                            if (same) log.debug("Found {}", path);
                            return same;
                        })
                .map(Path::toFile);
    }

}
