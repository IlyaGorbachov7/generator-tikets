package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl;

import lombok.AccessLevel;
import lombok.Getter;

import java.io.*;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public abstract class ReadableProperties implements Readable {

    @Getter(value = AccessLevel.PROTECTED)
    protected Hashtable<Object, Object> properties;

    protected abstract void initProperties();

    protected String get(String key) {
        return (String) properties.get(key);
    }

    @Override
    public String getValue(String key) {
        String value = get(key);
        if (Objects.nonNull(value)) {
            return value;
        }
        throw new NoSuchElementException(String.format("Not found key: %s", key));
    }

    @Override
    public String getValue(String key, String defaultValue) {
        return properties.contains(key) ? getValue(key) : defaultValue;
    }

    @Override
    public String[] getValues(String key) {
        String value = getValue(key);
        if (Objects.nonNull(value)) {
            return splitValue(key);
        }
        throw new NoSuchElementException(String.format("Not found key: %s", key));
    }

    @Override
    public String[] getValues(String key, String[] defaultValue) {
        return properties.contains(key) ? getValues(key) : defaultValue;
    }

    @Override
    public int getInt(String key) {
        String value = get(key);
        if (Objects.nonNull(value)) {
            return Integer.parseInt(value);
        }
        throw new NoSuchElementException(String.format("Not found key: %s", key));
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return properties.contains(key) ? getInt(key) : defaultValue;
    }

    @Override
    public int[] getInts(String key) {
        String[] stringArr = getValues(key);
        return Stream.of(stringArr).mapToInt(Integer::parseInt).toArray();
    }

    @Override
    public int[] getInts(String key, int[] defaultValue) {
        return properties.contains(key) ? getInts(key) : defaultValue;
    }

    @Override
    public long getLong(String key) {
        String value = get(key);
        if (Objects.nonNull(value)) {
            return Long.parseLong(value);
        }
        throw new NoSuchElementException(String.format("Not found key: %s", key));
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return properties.contains(key) ? getLong(key) : defaultValue;
    }

    @Override
    public long[] getLongs(String key) {
        String[] stringArr = getValues(key);
        return Stream.of(stringArr).mapToLong(Long::parseLong).toArray();
    }

    @Override
    public long[] getLongs(String key, long[] defaultValue) {
        return properties.contains(key) ? getLongs(key) : defaultValue;
    }

    @Override
    public Object getObject(String key) {
        String value = get(key);
        if (Objects.nonNull(value)) {
            return deserializeObject(key);
        }
        throw new NoSuchElementException(String.format("Not found key: %s", key));
    }

    @Override
    public Object getObject(String key, Object defaultValue) {
        return properties.contains(key) ? getObject(key) : defaultValue;
    }

    /**
     * Resolve situation:
     * For example you have properties file with properties value:
     * <p>
     * <b>app.directory</b>=.ticket-generator
     * <p>
     * <b>app.directory.serializer</b>=&{  app.directory }/serializer
     * <p>
     * Then value => <I>&{app.directory}/serializer</I> --> <i>.ticket-generator/serializer</i>
     */
    protected String resolveValue(String value) {
        Pattern pattern = Pattern.compile("&\\{\\s*(.+?)\\s*}");
        Matcher matcher = pattern.matcher(value);
        StringBuilder stringBuilder = new StringBuilder(value);
        while (matcher.find()) {
            String k = matcher.group(1);
            Object obj = getProperties().getOrDefault(k, null);
            String v = ((Objects.nonNull(obj)) ? ((obj instanceof String) ?
                    (String) obj : obj.toString()) : null);
            if (v != null) {
                int indexStart = matcher.start();
                int indexEnd = matcher.end();
                stringBuilder.replace(indexStart, indexEnd, v);
            }
        }
        return stringBuilder.toString();
    }

    protected String[] splitValue(String value) {
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .map(this::resolveValue).toArray(String[]::new);
    }

    protected Object deserializeObject(String value) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(value.getBytes()))) {
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
