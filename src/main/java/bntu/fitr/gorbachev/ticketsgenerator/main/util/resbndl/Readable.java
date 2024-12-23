package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl;

import java.util.Map;
import java.util.function.Supplier;

public interface Readable {

    boolean contains(String key);

    String getValue(String key);

    String getValue(String key, String defaultValue);

    String[] getValues(String key);

    String[] getValues(String key, String[] defaultValue);

    int getInt(String key);

    int getInt(String key, int defaultValue);

    int[] getInts(String key);

    int[] getInts(String key, int[] defaultValue);

    long getLong(String key);

    long getLong(String key, long defaultValue);

    long[] getLongs(String key);

    long[] getLongs(String key, long[] defaultValue);

    double getDouble(String key);

    double getDouble(String key, double defaultValue);

    double[] getDoubles(String key);

    double[] getDoubles(String key, double[] defaultValue);

    boolean getBoolean(String key);

    boolean getBoolean(String key, boolean defaultValue);

    boolean[] getBooleans(String key);

    boolean[] getBooleans(String key, boolean[] defaultValue);


    Object getObject(String key);

    Object getObject(String key, Object defaultValue);

    Map<String, String> getMap(String key);

    Map<String, String> getMap(String key, Map<String, String> defaultValue);

    Map<String, String> getMap(String key, Supplier<Map<String, String>> supplierMap);

    Map<String, String> getMap(String key, Map<String, String> defaultValue,
                               Supplier<Map<String, String>> supplierMap);
}
