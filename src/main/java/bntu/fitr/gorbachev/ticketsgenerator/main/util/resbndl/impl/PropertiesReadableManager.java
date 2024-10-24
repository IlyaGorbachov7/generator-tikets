package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.ReadableProperties;
import lombok.NonNull;

import java.util.Hashtable;
import java.util.Properties;

public abstract class PropertiesReadableManager extends ReadableProperties {

    protected Properties prop;

    public PropertiesReadableManager(@NonNull Properties prop) {
        this.prop = prop;
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
}
