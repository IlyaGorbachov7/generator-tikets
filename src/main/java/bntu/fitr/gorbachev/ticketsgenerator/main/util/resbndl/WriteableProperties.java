package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl;

import java.util.Hashtable;
import java.util.NoSuchElementException;

public abstract class WriteableProperties extends ReadableProperties implements Writable {

    protected void set(String key, Object value) {
        if (properties.containsKey(key)) {
            properties.put(key, value);
        }
        throw new NoSuchElementException(String.format("Not found key: %s", key));
    }

    @Override
    public void setValue(String key, String value) {
        set(key, value);
    }

    @Override
    public void setValue(String key, String[] values) {
        set(key, values);

    }

    @Override
    public void setValue(String key, int value) {
        set(key, value);

    }

    @Override
    public void setValue(String key, int[] values) {
        set(key, values);

    }

    @Override
    public void setValue(String key, long value) {
        set(key, value);

    }

    @Override
    public void setValue(String key, long[] values) {
        set(key, values);

    }

    @Override
    public void setObject(String key, Object value) {
        set(key, value);
    }
}
