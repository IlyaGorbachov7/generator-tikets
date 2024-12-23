package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.impl.PropertiesManagerBase;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.resolver.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.function.Supplier;

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
@Getter
public abstract class ReadableProperties implements Readable {

    @Setter(value = AccessLevel.PUBLIC)
    protected Hashtable<Object, Object> properties;

    protected RegexResolverToString resolverRegex;

    protected SplitResolverToArrayString resolverSplit;

    protected DeserializeResolverToObject resolverDeserialize;

    protected ResolverToInt resolverToInt;

    protected ResolverToArrayInt resolverToArrayInt;

    protected ResolverToLong resolverToLong;

    protected ResolverToArrayLong resolverToArrayLong;


    protected ResolverToDouble resolverToDouble;


    protected ResolverToArrayDouble resolverToArrayDouble;


    protected ResolverToBoolean resolverToBoolean;


    protected ResolverToArrayBoolean resolverToArrayBoolean;

    protected ResolverToMap resolverMap;

    protected abstract void initProperties();

    {
        initResolvers();
    }

    protected void initResolvers() {
        resolverRegex = initRegexResolver();
        resolverSplit = initSplitResolver();
        resolverDeserialize = initDeserializeResolver();
        resolverToInt = initIntResolver();
        resolverToArrayInt = initArrayIntResolver();
        resolverToLong = initLongResolver();
        resolverToArrayLong = initArrayLongResolver();
        resolverToDouble = initDoubleResolver();
        resolverToArrayDouble = initArrayDoubleResolver();
        resolverToBoolean = initBooleanResolver();
        resolverToArrayBoolean = initArrayBooleanResolver();
        resolverMap = initMapResolver();
    }

    public Map<Object, Object> getContent() {
        return Collections.unmodifiableMap(properties);
    }

    protected RegexResolverToString initRegexResolver() {
        return RegexResolverToString.builder().properties(this).build();
    }

    /**
     * This setter should be protected see PropertiesManagerBase
     *
     * @see PropertiesManagerBase
     */
    protected void setResolverRegex(RegexResolverToString resolverRegex) {
        if (resolverRegex.getProperties() != this) {
            throw new IllegalArgumentException("resolverRegex.getProperties() != this");
        }
        this.resolverRegex = resolverRegex;
        resolverSplit = initSplitResolver();
        resolverToArrayInt = initArrayIntResolver();
        resolverToArrayLong = initArrayLongResolver();
        resolverToArrayDouble = initArrayDoubleResolver();
        resolverToArrayBoolean = initArrayBooleanResolver();
        resolverMap = initMapResolver();
    }

    protected SplitResolverToArrayString initSplitResolver() {
        return SplitResolverToArrayString.builder().resolverRegex(resolverRegex).build();
    }

    /**
     * This setter should be protected see PropertiesManagerBase
     *
     * @see PropertiesManagerBase
     */
    protected void setResolverSplit(SplitResolverToArrayString splitResolver) {
        if (splitResolver.getRegexResolver() != resolverRegex) {
            throw new IllegalArgumentException("splitResolver.getRegexResolver() != resolverRegex");
        }
        this.resolverSplit = splitResolver;
        resolverToArrayInt = initArrayIntResolver();
        resolverToArrayLong = initArrayLongResolver();
        resolverToArrayDouble = initArrayDoubleResolver();
        resolverToArrayBoolean = initArrayBooleanResolver();
        resolverMap = initMapResolver();
    }

    protected DeserializeResolverToObject initDeserializeResolver() {
        return DeserializeResolverToObject.builder().build();
    }

    /**
     * This setter should be protected see PropertiesManagerBase
     *
     * @see PropertiesManagerBase
     */
    protected void setResolverDeserialize(DeserializeResolverToObject deserializeResolver) {
        this.resolverDeserialize = deserializeResolver;
    }

    protected ResolverToInt initIntResolver() {
        return ResolverToInt.builder().build();
    }

    /**
     * This setter should be protected see PropertiesManagerBase
     *
     * @see PropertiesManagerBase
     */
    protected void setResolverToInt(ResolverToInt resolverToInt) {
        this.resolverToInt = resolverToInt;
    }

    protected ResolverToArrayInt initArrayIntResolver() {
        return ResolverToArrayInt.builder().resolverSplit(resolverSplit).resolverToInt(resolverToInt).build();
    }

    /**
     * This setter should be protected see PropertiesManagerBase
     *
     * @see PropertiesManagerBase
     */
    protected void setResolverToArrayInt(ResolverToArrayInt resolverToArrayInt) {
        if (resolverToArrayInt.getResolverInt() != this.resolverToInt) {
            throw new IllegalArgumentException("resolverToArrayInt.getResolverInt() != this.resolverToInt");
        }
        if (resolverToArrayInt.getResolverSplit() != this.resolverSplit) {
            throw new IllegalArgumentException("resolverToArrayInt.getResolverSplit() != this.resolverSplit");
        }
        this.resolverToArrayInt = resolverToArrayInt;
    }

    protected ResolverToLong initLongResolver() {
        return ResolverToLong.builder().build();
    }

    /**
     * This setter should be protected see PropertiesManagerBase
     *
     * @see PropertiesManagerBase
     */
    protected void setResolverToLong(ResolverToLong resolverToLong) {
        this.resolverToLong = resolverToLong;
    }

    protected ResolverToArrayLong initArrayLongResolver() {
        return ResolverToArrayLong.builder().resolverSplit(resolverSplit).resolverToLong(resolverToLong).build();
    }

    /**
     * This setter should be protected see PropertiesManagerBase
     *
     * @see PropertiesManagerBase
     */
    protected void setResolverToArrayLong(ResolverToArrayLong resolverToArrayLong) {
        if (resolverToArrayLong.getResolverLong() != this.resolverToLong) {
            throw new IllegalArgumentException("resolverToArrayLong.getResolverLong() != this.resolverToLong");
        }
        if (resolverToArrayLong.getResolverSplit() != this.resolverSplit) {
            throw new IllegalArgumentException("resolverToArrayLong.getResolverSplit() != this.resolverSplit");
        }
        this.resolverToArrayLong = resolverToArrayLong;
    }

    protected ResolverToBoolean initBooleanResolver() {
        return ResolverToBoolean.builder().build();
    }

    /**
     * This setter should be protected see PropertiesManagerBase
     *
     * @see PropertiesManagerBase
     */
    protected void setResolverToBoolean(ResolverToBoolean resolverToBoolean) {
        this.resolverToBoolean = resolverToBoolean;
    }

    protected ResolverToArrayBoolean initArrayBooleanResolver() {
        return ResolverToArrayBoolean.builder().resolverSplit(resolverSplit).resolverToBoolean(resolverToBoolean).build();
    }

    /**
     * This setter should be protected see PropertiesManagerBase
     *
     * @see PropertiesManagerBase
     */
    protected void setResolverToArrayBoolean(ResolverToArrayBoolean resolverToArrayBoolean) {
        if (resolverToArrayBoolean.getResolverBoolean() != this.resolverToBoolean) {
            throw new IllegalArgumentException("resolverToArrayBoolean.getResolverBoolean() != this.resolverToBoolean");
        }
        if (resolverToArrayBoolean.getResolverSplit() != this.resolverSplit) {
            throw new IllegalArgumentException("resolverToArrayBoolean.getResolverSplit() != this.resolverSplit");
        }
        this.resolverToArrayBoolean = resolverToArrayBoolean;
    }

    protected ResolverToDouble initDoubleResolver() {
        return ResolverToDouble.builder().build();
    }

    /**
     * This setter should be protected see PropertiesManagerBase
     *
     * @see PropertiesManagerBase
     */
    protected void setResolverToDouble(ResolverToDouble resolverToDouble) {
        this.resolverToDouble = resolverToDouble;
    }

    protected ResolverToArrayDouble initArrayDoubleResolver() {
        return ResolverToArrayDouble.builder().resolverToDouble(this.resolverToDouble).resolverSplit(resolverSplit).build();
    }

    /**
     * This setter should be protected see PropertiesManagerBase
     *
     * @see PropertiesManagerBase
     */
    protected void setResolverToArrayDouble(ResolverToArrayDouble resolverToArrayDouble) {
        if (resolverToArrayDouble.getResolverDouble() != this.resolverToDouble) {
            throw new IllegalArgumentException("resolverToArrayDouble.getResolverDouble() != this.resolverToDouble");
        }
        if (resolverToArrayDouble.getResolverSplit() != this.resolverSplit) {
            throw new IllegalArgumentException("resolverToArrayDouble.getResolverSplit() != this.resolverSplit");
        }
        this.resolverToArrayDouble = resolverToArrayDouble;
    }

    protected ResolverToMap initMapResolver() {
        return ResolverToMap.builder().resolverSplit(resolverSplit).build();
    }

    /**
     * This setter should be protected see PropertiesManagerBase
     *
     * @see PropertiesManagerBase
     */
    protected void setResolverToMap(ResolverToMap resolverToMap) {
        if (resolverToMap.getSplitResolver() != this.resolverSplit) {
            throw new IllegalArgumentException("resolverToMap.getSplitResolver() != this.resolverSplit");
        }
        this.resolverMap = resolverToMap;
    }

    protected String get(String key) {
        String value = (String) properties.get(key);
        if (Objects.isNull(value)) throw new NoSuchElementException(String.format("Not found key: %s", key));
        return value;
    }

    @Override
    public boolean contains(String key) {
        return properties.containsKey(key);
    }

    @Override
    public String getValue(String key) {
        return resolverRegex.assemble(get(key));
    }

    @Override
    public String getValue(String key, String defaultValue) {
        return contains(key) ? getValue(key) : defaultValue;
    }

    @Override
    public String[] getValues(String key) {
        return resolverSplit.assemble(getValue(key));
    }

    @Override
    public String[] getValues(String key, String[] defaultValue) {
        return contains(key) ? getValues(key) : defaultValue;
    }

    @Override
    public int getInt(String key) {
        return resolverToInt.assemble(get(key));
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return contains(key) ? getInt(key) : defaultValue;
    }

    @Override
    public int[] getInts(String key) {
        return resolverToArrayInt.assemble(get(key));
    }

    @Override
    public int[] getInts(String key, int[] defaultValue) {
        return contains(key) ? getInts(key) : defaultValue;
    }

    @Override
    public long getLong(String key) {
        return resolverToLong.assemble(get(key));
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return contains(key) ? getLong(key) : defaultValue;
    }

    @Override
    public long[] getLongs(String key) {
        return resolverToArrayLong.assemble(get(key));
    }

    @Override
    public long[] getLongs(String key, long[] defaultValue) {
        return contains(key) ? getLongs(key) : defaultValue;
    }

    @Override
    public double getDouble(String key) {
        return resolverToDouble.assemble(get(key));
    }

    @Override
    public double getDouble(String key, double defaultValue) {
        return contains(key) ? getDouble(key) : defaultValue;
    }

    @Override
    public double[] getDoubles(String key) {
        return resolverToArrayDouble.assemble(get(key));
    }

    @Override
    public double[] getDoubles(String key, double[] defaultValue) {
        return contains(key) ? getDoubles(key) : defaultValue;
    }

    @Override
    public boolean getBoolean(String key) {
        return resolverToBoolean.assemble(get(key));
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return contains(key) ? getBoolean(key) : defaultValue;
    }

    @Override
    public boolean[] getBooleans(String key) {
        return resolverToArrayBoolean.assemble(get(key));
    }

    @Override
    public boolean[] getBooleans(String key, boolean[] defaultValue) {
        return contains(key) ? getBooleans(key) : defaultValue;
    }

    @Override
    public Object getObject(String key) {
        return resolverDeserialize.assemble(get(key));
    }

    @Override
    public Object getObject(String key, Object defaultValue) {
        return contains(key) ? getObject(key) : defaultValue;
    }

    @Override
    public Map<String, String> getMap(String key) {
        return resolverMap.assemble(get(key));
    }

    @Override
    public Map<String, String> getMap(String key, Map<String, String> defaultValue) {
        return contains(key) ? getMap(key) : defaultValue;
    }

    @Override
    public Map<String, String> getMap(String key, Supplier<Map<String, String>> supplierMap) {
        return resolverMap.assemble(get(key), supplierMap);
    }

    @Override
    public Map<String, String> getMap(String key, Map<String, String> defaultValue, Supplier<Map<String, String>> supplierMap) {
        return contains(key) ? getMap(key, supplierMap) : defaultValue;
    }
}
