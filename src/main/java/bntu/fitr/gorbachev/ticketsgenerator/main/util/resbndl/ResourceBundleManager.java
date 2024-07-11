package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl;

import java.net.URL;

public interface ResourceBundleManager {

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

    Object getObject(String key);

    Object getObject(String key, Object defaultValue);
}
