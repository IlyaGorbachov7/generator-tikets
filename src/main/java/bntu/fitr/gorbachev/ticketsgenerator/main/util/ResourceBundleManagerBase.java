package bntu.fitr.gorbachev.ticketsgenerator.main.util;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.ResourceBundleManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceBundleManagerBase implements ResourceBundleManager {

    private ResourceBundle resourceBundle;

    @Override
    public String getValue(String key) {
        return resolveValue(resourceBundle.getString(key));
    }

    @Override
    public String getValue(String key, String defaultValue) {
        return resourceBundle.containsKey(key) ? getValue(key) : defaultValue;
    }

    @Override
    public String[] getValues(String key) {
        return Arrays.stream(getValue(key).split(","))
                .map(String::trim)
                .map(this::resolveValue).toArray(String[]::new);
    }

    @Override
    public String[] getValues(String key, String[] defaultValue) {
        return resourceBundle.containsKey(key) ? getValues(key) : defaultValue;
    }

    @Override
    public int getInt(String key) {
        return Integer.parseInt(resourceBundle.getString(key));
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return resourceBundle.containsKey(key) ? getInt(key) : defaultValue;
    }

    @Override
    public int[] getInts(String key) {
        return Arrays.stream(resourceBundle.getStringArray(key)).mapToInt(Integer::parseInt).toArray();
    }

    @Override
    public int[] getInts(String key, int[] defaultValue) {
        return resourceBundle.containsKey(key) ? getInts(key) : defaultValue;
    }

    @Override
    public long getLong(String key) {
        return Long.parseLong(resourceBundle.getString(key));
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return resourceBundle.containsKey(key) ? getLong(key) : defaultValue;
    }

    @Override
    public long[] getLongs(String key) {
        return Arrays.stream(resourceBundle.getStringArray(key)).mapToLong(Long::parseLong).toArray();
    }

    @Override
    public long[] getLongs(String key, long[] defaultValue) {
        return resourceBundle.containsKey(key) ? getLongs(key) : defaultValue;
    }

    @Override
    public Object getObject(String key) {
        return resourceBundle.getObject(key);
    }

    @Override
    public Object getObject(String key, Object defaultValue) {
        return resourceBundle.containsKey(key) ? getObject(key) : defaultValue;
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
    private String resolveValue(String str) {
        Pattern pattern = Pattern.compile("&\\{\\s*(.+?)\\s*}");
        Matcher matcher = pattern.matcher(str);
        StringBuilder stringBuilder = new StringBuilder(str);
        while (matcher.find()) {
            String k = matcher.group(1);
            String v = resourceBundle.containsKey(k) ? resourceBundle.getString(k) : null;
            if (v != null) {
                int indexStart = matcher.start();
                int indexEnd = matcher.end();
                stringBuilder.replace(indexStart, indexEnd, v);
            }
        }
        return stringBuilder.toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        Locale locale;
        ResourceBundle.Control control;

        public Builder() {
        }

        public Builder setLocale(Locale locale) {
            this.locale = locale;
            return this;
        }

        public Builder setControl(ResourceBundle.Control control) {
            this.control = control;
            return this;
        }

        public ResourceBundleManager build(String baseName) {
            ResourceBundle resourceBundle;
            if (locale != null && control != null) {
                resourceBundle = ResourceBundle.getBundle(baseName, locale, control);
            } else if (locale != null) {
                resourceBundle = ResourceBundle.getBundle(baseName, locale);
            } else if (control != null) {
                resourceBundle = ResourceBundle.getBundle(baseName, control);
            } else {
                resourceBundle = ResourceBundle.getBundle(baseName, new UTF8Control());
            }
            return new ResourceBundleManagerBase(resourceBundle);
        }


    }

    public static class UTF8Control extends ResourceBundle.Control {
        @Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IOException {
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            ResourceBundle bundle = null;
            InputStream stream = null;
            if (reload) {
                URL url = loader.getResource(resourceName);
                if (url != null) {
                    URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            } else {
                stream = loader.getResourceAsStream(resourceName);
            }
            if (stream != null) {
                try {
                    // Only this line is changed to make it to read properties files as UTF-8.
                    bundle = new PropertyResourceBundle(new InputStreamReader(stream, StandardCharsets.UTF_8));
                } finally {
                    stream.close();
                }
            }
            return bundle;
        }
    }
}
