package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl;

public interface Writable {

    void setValue(String key, String value);

    void setValue(String key, String[] values);

    void setValue(String key, int value);

    void setValue(String key, int[] values);

    void setValue(String key, long value);

    void setValue(String key, long[] values);

    void setObject(String key, Object value);
}
