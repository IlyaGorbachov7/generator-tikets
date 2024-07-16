package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.WriteableProperties;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Properties;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class PropertiesWritableManager extends WriteableProperties {
    @NonNull
    protected Properties prop;

    @Override
    protected void initProperties() {
        properties = prop;
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
