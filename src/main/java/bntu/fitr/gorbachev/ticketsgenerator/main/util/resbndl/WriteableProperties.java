package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl;

import java.io.Serializable;
import java.util.Map;

public abstract class WriteableProperties extends ReadableProperties implements Writable {

    protected void set(String key, String value) {
        properties.put(key, value);
    }

    @Override
    public void setValue(String key, String value) {
        set(key, value);
    }

    @Override
    public void setValue(String key, String[] values) {
        set(key, resolverSplit.assembleToString(values));

    }

    @Override
    public void setValue(String key, int value) {
        set(key, resolverToInt.assembleToString(value));

    }

    @Override
    public void setValue(String key, int[] values) {
        set(key, resolverToArrayInt.assembleToString(values));

    }

    @Override
    public void setValue(String key, long value) {
        set(key, resolverToLong.assembleToString(value));

    }

    @Override
    public void setValue(String key, long[] values) {
        set(key, resolverToArrayLong.assembleToString(values));

    }

    @Override
    public void setValue(String key, Serializable value) {
        set(key, resolverDeserialize.assembleToString(value));
    }

    @Override
    public void setValue(String key, Map<String, String> value) {
        set(key, resolverMap.assembleToString(value));
    }
}
