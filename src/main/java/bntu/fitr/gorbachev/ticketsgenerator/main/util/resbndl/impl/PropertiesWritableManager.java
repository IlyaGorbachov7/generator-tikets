package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.WriteableProperties;
import lombok.NonNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Properties;

public abstract class PropertiesWritableManager extends WriteableProperties {
    @NonNull
    protected Properties prop;

    public PropertiesWritableManager(@NonNull Properties prop) {
        this.prop = prop;
        initProperties();
    }

    @Override
    protected void initProperties() {
        properties = prop;
    }

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
