package bntu.fitr.gorbachev.ticketsgenerator.main.util.loc;

import bntu.fitr.gorbachev.ticketsgenerator.main.TicketGeneratorUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.FilesUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.impl.PropertiesManagerBase;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.exep.NotAccessToFileException;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.ReadableProperties;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.serializer.SerializeListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.serializer.SerializeManager;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.serializer.Serializer;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public class LocalsConfiguration implements SerializeListener {
    public static final String DIR_LOCALES = "lang";
    public static final String PROPERTIES_EXCEPTION = ".properties";

    private final Locale defaultLocale = TicketGeneratorUtil.getDefaultLocale();
    @Getter
    private Set<Locale> supportedLocales;
    @Getter
    private Locale selectedLocale;
    @Getter
    private ReadableProperties localeProperties;


    private Serializer serializer;

    public LocalsConfiguration() throws LocalizerException {
        SerializeManager.addListener(this);
        List<LocData> data = null;
        try {
            serializer = Serializer.getSerializer(TicketGeneratorUtil.getPathSerializeDirectory());
            data = serializer.deserialize(LocData.class);
            if (!data.isEmpty()) {
                selectedLocale = data.get(0).getSelectedLocale();
            }
            defineLocale();
            log.info("Selected locale application: {}", selectedLocale); // Here selectedLocale is defined after invoke method: defineLocale();
            defineLocaleProperties();
        } catch (IOException e) {
            throw new LocalizerException(e);
        }
    }

    public void setSelectedLocale(Locale locale) {
        if (!supportedLocales.contains(locale)) {
            log.warn("Try was set selected locale: {}. Transmitted locale don't supported.", locale);
            return;
        }
        selectedLocale = locale;
        updateLocaleProperties();
    }

    public void setSelectedLocaleSerialize(Locale locale) throws IOException {
        setSelectedLocale(locale);
        serialize();
    }

    private void defineLocale() throws LocalizerException {
        try {
            Set<Locale> supportedLocales = getSupportedLocale();
            if (!supportedLocales.contains(defaultLocale)) { // this snippet is very important
                log.error("Specified defaultLocale: {} don't supported. Supported locales: {}", defaultLocale, supportedLocales);
                throw new LocalizerException(String.format("Specified defaultLocale: %s don't supported. Supported locales: %s.", defaultLocale, supportedLocales));
            }

            if (Objects.nonNull(selectedLocale)) {
                if (!supportedLocales.contains(selectedLocale)) {
                    log.warn("Selected locale: {} not founded from supported locales: {}", selectedLocale, supportedLocales);
                    selectedLocale = null;
                } else {
                    return;
                }
            }
            selectedLocale = defaultLocale;
        } catch (NotAccessToFileException e) {
            throw new LocalizerException(e);
        }

    }

    private Set<Locale> getSupportedLocale() throws NotAccessToFileException, LocalizerException {
        if (Objects.isNull(supportedLocales)) {
            Stream.Builder<Locale> builderList = Stream.builder();
            if (FilesUtil.isRunningFromJar()) {
                String pathJarFile = LocalsConfiguration.class.getProtectionDomain()
                        .getCodeSource().getLocation().getPath();
                try (JarFile jarFile = new JarFile(pathJarFile)) {
                    var entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entity = entries.nextElement();
                        String pathToFileOrDir = entity.getName();
                        if (pathToFileOrDir.startsWith(DIR_LOCALES.replaceFirst(TicketGeneratorUtil.getFileSeparator(), "")) && pathToFileOrDir.endsWith(".properties")) {
                            String simpleName = pathToFileOrDir.substring(pathToFileOrDir.lastIndexOf(TicketGeneratorUtil.getFileSeparator()) + 1);
                            Locale locale = isLocale(simpleName);
                            if (Objects.nonNull(locale)) {
                                builderList.accept(locale);
                                log.debug("Find supported-locale file {}", simpleName);
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    Path rootDirLocales = Path.of(Objects.requireNonNull(LocalsConfiguration.class.getClassLoader().getResource(DIR_LOCALES)).toURI());
                    log.info("Default dir locales file: {}", rootDirLocales);
                    Files.find(rootDirLocales, 1, (pathFile, attr) -> {
                        if (pathFile.toString().endsWith(".properties")) {
                            return true;
                        }
                        return false;
                    }).forEach(propertiesPath -> {
                        String namePropertieFile = propertiesPath.toString().substring(propertiesPath.toString().lastIndexOf(TicketGeneratorUtil.getFileSeparator()) + 1);
                        Locale locale = isLocale(namePropertieFile);
                        if (Objects.nonNull(locale)) {
                            builderList.accept(locale);
                            log.debug("Find supported-locale file {}", namePropertieFile);
                        }
                    });
                } catch (Exception e) {
                    throw new NotAccessToFileException("Directory locale undefined", e, Path.of(DIR_LOCALES));
                }
            }
            supportedLocales = builderList.build().collect(Collectors.toSet());
            if (supportedLocales.isEmpty()) throw new LocalizerException("Don't founded nothing locales file");
        }
        return supportedLocales;
    }

    /**
     * <b>simpleName</b> может быть любой. Даже если файл имеет такое название
     * <i>xyu.properties</i> то в таком случаи локаль будет с языком <b>xyu</b>
     * <p>
     * Если будет файл с названием <b>vtf_city</b> то локаль будет с языком <b>vtf</b> и страной <b>city</b>
     * <p>
     * То есть я хочу донести тот факт, что языком и страной может быть любая строка. Но я конечно же буду назвать файлы
     * привычными именами:
     * en.properties, ru.properties и так далее. Потому что пользователь явно с какой-то из этих локацией
     */
    private Locale isLocale(String simpleName) {
        String name = simpleName.substring(0, simpleName.indexOf(PROPERTIES_EXCEPTION));
        if (name.length() > 5) return null;
        if (name.contains("_")) {
            String[] args = name.split("_");
            if (args.length > 2) {
                return null;
            }
            return new Locale(args[0], args[1]);
        }
        if (name.length() > 2) return null;
        return new Locale(name);
    }

    public LocaleResolver getLocaleResolver() {
        return (LocaleResolver) localeProperties.getResolverRegex();
    }

    private void defineLocaleProperties() {
        try {
            localeProperties = PropertiesManagerBase.builder().setResolverRegex(LocaleResolver.builder().buildNullable())
                    .build(FilesUtil.resolveSourceLocationAsInputStream(String.format(
                            Locale.ROOT, "%s/%s%s", DIR_LOCALES, selectedLocale.toString(), PROPERTIES_EXCEPTION)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateLocaleProperties() {
        try {
            if (localeProperties == null) {
                throw new NullPointerException("localeProperties == null, You should define locale");
            } else {
                // It's done this way because/ There are objects that directly depend on localeProperties
                // So when locale will be updated. Whe don't have to update localeProperties
                // Because localeProperties used in resolverRegex and resolverLocale then used this instance
                // So I update only properties which contains key=value E
                localeProperties.setProperties(PropertiesManagerBase.builder().setResolverRegex(LocaleResolver.builder()
                        .buildNullable()).build(FilesUtil.resolveSourceLocationAsInputStream(String.format(Locale.ROOT,
                        "%s/%s%s", DIR_LOCALES, selectedLocale.toString(), PROPERTIES_EXCEPTION))).getProperties());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void serialize() throws IOException {
        LocData data = new LocData();
        data.setSelectedLocale(selectedLocale);
        serializer.serialize(data);
    }
}
