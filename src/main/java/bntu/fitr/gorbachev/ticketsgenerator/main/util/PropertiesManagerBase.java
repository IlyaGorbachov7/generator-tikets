package bntu.fitr.gorbachev.ticketsgenerator.main.util;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.impl.PropertiesWritableManager;
import lombok.NonNull;

import java.io.*;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.util.Properties;

public class PropertiesManagerBase extends PropertiesWritableManager {

    protected PropertiesManagerBase(@NonNull Properties prop) {
        super(prop);
    }

    public static ResourceBundleManagerBase.Builder builder() {
        return new ResourceBundleManagerBase.Builder();
    }

    public static class Builder {

        public Builder() {
        }

        public PropertiesWritableManager build() {
            Properties prop = new Properties();
            return new PropertiesManagerBase(prop);
        }

        private void checkCredential(File file) throws FileNotFoundException, AccessDeniedException {
            if (Files.exists(file.getAbsoluteFile().toPath())) {
                throw new FileNotFoundException(String.format("File %s not found or not access for reading", file));
            }
            if (!Files.isReadable(file.getAbsoluteFile().toPath())) {
                throw new AccessDeniedException(String.format("File: %s not access for reading", file));
            }
        }

        public PropertiesWritableManager build(File file) throws AccessDeniedException, FileNotFoundException {
            checkCredential(file);
            Properties prop = new Properties();
            return new PropertiesManagerBase(prop);
        }

        public PropertiesWritableManager build(Properties propDefault) {
            Properties prop = new Properties(propDefault);
            return new PropertiesManagerBase(prop);
        }

        public PropertiesWritableManager build(Reader reader) throws IOException {
            Properties prop = new Properties();
            prop.load(reader);
            return new PropertiesManagerBase(prop);
        }

        public PropertiesWritableManager build(InputStream inputStream) throws IOException {
            Properties prop = new Properties();
            prop.load(inputStream);
            return new PropertiesManagerBase(prop);
        }

        public PropertiesWritableManager buildFromXml(InputStream inputStreamFromXML) throws IOException {
            Properties prop = new Properties();
            prop.load(inputStreamFromXML);
            return new PropertiesManagerBase(prop);
        }
    }
}
