package bntu.fitr.gorbachev.ticketsgenerator.main.util.loc;

import bntu.fitr.gorbachev.ticketsgenerator.main.TicketGeneratorUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.ReadableProperties;
import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Localizer {

    @Getter
    private static LocalsConfiguration localsConfiguration;

    @Getter
    private static final LocaleResolver resolver;

    @Getter
    private static final String splitSymbol = ". ";

    static {
        localsConfiguration = TicketGeneratorUtil.getLocalsConfiguration();
        resolver = localsConfiguration.getLocaleResolver();
    }

    public static ReadableProperties getLocaleProperties() {
        return localsConfiguration.getLocaleProperties();
    }

    /**
     * @param key         don't null
     * @param relatedKeys can be null
     * @return
     */
    public static String get(String key, String... relatedKeys) {
        if (!getLocaleProperties().contains(key)) return null;
        String localizeStringV = getLocaleProperties().getValue(key);
        String[] localizeStringVs = Arrays.stream(relatedKeys).map(relatedKey -> getLocaleProperties()
                .getValue(relatedKey)).toArray(String[]::new);
        return resolver.assemble(localizeStringV, localizeStringVs);
    }

    public static String getWithValues(String key, String... values) {
        if (!getLocaleProperties().contains(key)) return null;
        String localizeStringV = getLocaleProperties().getValue(key);
        return resolver.assemble(localizeStringV, values);
    }

    public static String getJoinValue(String... values) {
        return String.join(splitSymbol, values);
    }

    public static String getJoinValue(char[] symbolJoin, String... values) {
        return String.join(String.valueOf(symbolJoin), values);
    }

    public static String getCombined(List<String> keys) {
        return getCombined(splitSymbol, keys);
    }

    public static String getCombined(String splitSymbol, List<String> keys) {
        return keys.stream().map(getLocaleProperties()::getValue).collect(Collectors.joining(splitSymbol));
    }

    /**
     * @param keyAndRelatedKeys key -don't null , String[] value = can be null
     */
    @SafeVarargs
    public static String getCombined(Map.Entry<String, String[]>... keyAndRelatedKeys) {
        return getCombined(splitSymbol, keyAndRelatedKeys);
    }

    @SafeVarargs
    public static String getCombined(@NonNull String splitSymbol, @NonNull Map.Entry<String, String[]>... keyAndRelatedKeys) {
        return Arrays.stream(keyAndRelatedKeys).map(entry -> {
            String key = entry.getKey();
            String[] relatedKeys = entry.getValue();
            return get(key, relatedKeys);
        }).collect(Collectors.joining(splitSymbol));
    }
}
