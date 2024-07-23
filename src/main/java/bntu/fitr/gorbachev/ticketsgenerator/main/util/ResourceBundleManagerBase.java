package bntu.fitr.gorbachev.ticketsgenerator.main.util;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.impl.ResourceBundleManager;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

@Slf4j
public class ResourceBundleManagerBase extends ResourceBundleManager {

    protected ResourceBundleManagerBase(ResourceBundle resourceBundle) {
        super(resourceBundle);
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
