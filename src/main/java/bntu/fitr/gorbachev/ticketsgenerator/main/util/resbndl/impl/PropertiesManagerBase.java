package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.impl;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Properties;

public class PropertiesManagerBase extends PropertiesWritableManager {

    @Setter
    @Getter
    protected File fileStore;

    protected PropertiesManagerBase(@NonNull Properties prop) {
        super(prop);
    }

    public void save() throws IOException {
        if (Objects.isNull(fileStore)) throw new IOException("fileStore underfeed");
        store(fileStore);
    }

    public static PropertiesManagerBase.Builder builder() {
        return new PropertiesManagerBase.Builder();
    }

    public static class Builder {
        protected File fileStore;

        public Builder setFileSore(File fileStore) {
            this.fileStore = fileStore;
            return this;
        }

        public Builder() {
        }

        public PropertiesWritableManager build() {
            Properties prop = new Properties();
            return new PropertiesManagerBase(prop);
        }

        private static void checkCredential(File file) throws FileNotFoundException, AccessDeniedException {
            if (!Files.exists(file.getAbsoluteFile().toPath())) {
                throw new FileNotFoundException(String.format("File %s not found or not access for reading", file));
            }
            if (!Files.isReadable(file.getAbsoluteFile().toPath())) {
                throw new AccessDeniedException(String.format("File: %s not access for reading", file));
            }
        }

        public PropertiesManagerBase build(File file) throws AccessDeniedException, FileNotFoundException {
            checkCredential(file);
            try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                PropertiesManagerBase res = build(reader);
                if(Objects.isNull(res.fileStore)) { // by default
                    res.fileStore = file;
                }
                return res;
            } catch (IOException e) {
                throw new FileNotFoundException(e.getMessage());
            }
        }

        public PropertiesManagerBase build(Properties propDefault) {
            Properties prop = new Properties(propDefault);
            return combine(prop);
        }

        public PropertiesManagerBase build(Reader reader) throws IOException {
            Properties prop = new Properties();
            prop.load(reader);
            return combine(prop);
        }

        public PropertiesManagerBase build(InputStream inputStream) throws IOException {
            Properties prop = new Properties();
            prop.load(inputStream);
            return combine(prop);
        }

        public PropertiesManagerBase buildFromXml(InputStream inputStreamFromXML) throws IOException {
            Properties prop = new Properties();
            prop.load(inputStreamFromXML);
            return combine(prop);
        }

        protected PropertiesManagerBase combine(Properties prop) {
            PropertiesManagerBase res = new PropertiesManagerBase(prop);
            res.fileStore = this.fileStore;
            return res;
        }
    }
}
