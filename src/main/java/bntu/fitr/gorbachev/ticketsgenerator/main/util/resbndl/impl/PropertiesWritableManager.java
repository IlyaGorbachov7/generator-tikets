package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.WriteableProperties;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Hashtable;
import java.util.Objects;
import java.util.Properties;

public abstract class PropertiesWritableManager extends WriteableProperties {
    @NonNull
    protected Properties prop;

    @Setter
    @Getter
    protected File fileStore;

    public PropertiesWritableManager(@NonNull Properties prop) {
        this.prop = prop;
        initProperties();
    }

    public PropertiesWritableManager(@NonNull Properties prop, @NonNull File fileStore) {
        this.prop = prop;
        this.fileStore = fileStore;
        initProperties();
    }

    @Override
    protected void initProperties() {
        properties = prop;
    }

    @Override
    public void setProperties(Hashtable<Object, Object> properties) {
        super.setProperties(properties);
        prop = (Properties) properties;
    }

    public void save() throws IOException {
        if (Objects.isNull(fileStore)) throw new IOException("fileStore underfeed");
        store(fileStore);
    }

    /**
     * if file don't exist. Don't worry This file wii be creat
     */
    public void store(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            store(writer);
        }
    }

    public void store(Writer writer) throws IOException {
        prop.store(writer, LocalDate.now().toString());
    }

    public void store(OutputStream outputStream) throws IOException {
        prop.store(outputStream, LocalDate.now().toString());
    }

    public void storeToXML(OutputStream outputStream) throws IOException {
        prop.storeToXML(outputStream, LocalDate.now().toString(), StandardCharsets.UTF_8);
    }

}
