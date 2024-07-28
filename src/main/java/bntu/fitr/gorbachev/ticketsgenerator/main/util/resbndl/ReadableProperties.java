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

/**
 * Resolve situation:
 * For example you have properties file with properties value:
 * <p>
 * <b>app.directory</b>=.ticket-generator
 * <p>
 * <b>app.directory.serializer</b>=&{  app.directory }/serializer
 * <p>
 * Then value => <I>&{app.directory}/serializer</I> --> <i>.ticket-generator/serializer</i>
 * <p>
 * ___________________Example 1________________
 * <p>
 * <b>int.value=4</b>
 * <p>
 * <b>int.values= 1, 2, ${int.value}, 5</b>
 * <p>
 * <b>str.value = Hello Word</b>
 * <p>
 * <b>str.values = read, learn, Okey ${str.value}</b>
 * <p>
 * ___________________Example 2________________
 * <p>
 * <b>app.dir=&{sys: user.home}/${app.dir}/anyFolder</b>
 * </p>
 * <i>Result :: app.dir=User/userName/appdir/anyFolder</i>
 * <p>
 * <p>
 * Supported: ${} or &{} and you get access system properties from ${sys: systemProperty}
 * <p>
 * <b>Also supported nesting of keys</b>
 * <p>
 * <i>For example: </i> app.with.nesty=${str.values},${str.value},
 * where<p>
 * <b>str.values=String1, String2, ${str.value}, ${str.value2}</b>
 * <p>
 * <i>Res: [String1, String2, HiBrooo, AAAAA, Hello Word]</i>
 * <p>
 * ###############      Attention! ################
 * <p>
 * PropertyReadableManager don't take in account "file separator" depending on the operation system,
 * But you should remember that if use <i>File file = new file(path)</i>, then  file separator become valid.
 * <p>
 * For example:
 * <p>
 * If you have property => <i>dir.app=</i><b>${sys: user.home}/appName/logs</b>
 * <p>
 * Res:for => Windows OS => C:<b>\</b>Users<b>\</b>SecuRiTy<b>/</b>appName<b>/</b>logs | Linux OS => root<b>/</b>SecuRiTy<b>/</b>appName<b>/</b>logs
 * <p>
 * But if you use <i>File class</i> => new File(res) -> for WindowsC:<b>\</b>Users<b>\</b>SecuRiTy<b>\</b>appName<b>\</b>logs
 * for Linux -> root<b>/</b>SecuRiTy<b>/</b>appName<b>/</b>logs
 *
 * @version 28.07.2024
 */

public abstract class ReadableProperties implements Readable {

    @Getter(value = AccessLevel.PROTECTED)
    protected Hashtable<Object, Object> properties;

    protected abstract void initProperties();

    protected String get(String key) {
        return (String) properties.get(key);
    }

    @Override
    public String getValue(String key) {
        String value = resolveValue(get(key));
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
            return splitValue(value);
        }
        throw new NoSuchElementException(String.format("Not found key: %s", key));
    }

    @Override
    public String[] getValues(String key, String[] defaultValue) {
        return properties.contains(key) ? getValues(key) : defaultValue;
    }

    @Override
    public int getInt(String key) {
        String value = resolveValue(get(key));
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
        String value = resolveValue(get(key));
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
     * <p>
     * ___________________Example 1________________
     * <p>
     * <b>int.value=4</b>
     * <p>
     * <b>int.values= 1, 2, ${int.value}, 5</b>
     * <p>
     * <b>str.value = Hello Word</b>
     * <p>
     * <b>str.values = read, learn, Okey ${str.value}</b>
     * <p>
     * ___________________Example 2________________
     * <p>
     * <b>app.dir=&{sys: user.home}/${app.dir}/anyFolder</b>
     * </p>
     * <i>Result :: app.dir=User/userName/appdir/anyFolder</i>
     * <p>
     * <p>
     * Supported: ${} or &{} and you get access system properties from ${sys: systemProperty}
     * <p>
     * <b>Also supported nesting of keys</b>
     * <p>
     * <i>For example: </i> app.with.nesty=${str.values},${str.value},
     * where<p>
     * <b>str.values=String1, String2, ${str.value}, ${str.value2}</b>
     * <p>
     * <i>Res: [String1, String2, HiBrooo, AAAAA, Hello Word]</i>
     */
    protected String resolveValue(String value) {
//        Pattern pattern = Pattern.compile("[&$]\\{\\s*(.+?)\\s*}");
        Pattern pattern = Pattern.compile("[&$]\\{\\s*(sys\\s*:\\s*)?\\s*(.+?)\\s*}");
        Matcher matcher = pattern.matcher(value);
        StringBuilder stringBuilder = new StringBuilder(value);
        while (matcher.find()) {
            String sysProp = matcher.group(1);
            String k = matcher.group(2);
            String v;
            if (sysProp != null) {
                v = System.getProperty(k);
            } else {
                Object obj = getProperties().getOrDefault(k, null);
                v = ((Objects.nonNull(obj)) ? ((obj instanceof String) ?
                        (String) obj : obj.toString()) : null);
            }
            if (v != null) {
                v = resolveValue(v); // This code solves the problem of nesting properties
                int indexStart = matcher.start();
                int indexEnd = matcher.end();
                String replaceKeyInstruction = value.substring(indexStart, indexEnd);
                indexStart = stringBuilder.indexOf(replaceKeyInstruction);
                indexEnd = indexStart + replaceKeyInstruction.length();
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
