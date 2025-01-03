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
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static bntu.fitr.gorbachev.ticketsgenerator.main.util.FilesUtil.ARCHIVE_SEPARATOR;
import static bntu.fitr.gorbachev.ticketsgenerator.main.util.FilesUtil.PROPERTIES_EXCEPTION;

@Log4j2
public class LocalsConfiguration implements SerializeListener {
    public static final String DIR_LOCALES = "lang";
    private static final String LOCALE_SEPARATOR = "_";

    private final Locale defaultLocale = TicketGeneratorUtil.getDefaultLocale();
    @Getter
    private Set<Locale> supportedLocales;
    @Getter
    private Locale selectedLocale;
    @Getter
    private ReadableProperties localeProperties;

    private List<LocalizerListener> handlers = new ArrayList<>();

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
        Locale.setDefault(selectedLocale);
        updateLocaleProperties();
        log.info("Selected locale: {}", selectedLocale);
        runUpdate(selectedLocale);
    }

    public void setSelectedLocaleSerialize(Locale locale) throws IOException {
        setSelectedLocale(locale);
        serialize();
    }

    private void defineLocale() throws LocalizerException {
        Set<Locale> supportedLocales = getSupportedLocale();
        if (supportedLocales.isEmpty()) {
            log.error("Supported languages are not defined in the application. Check the correspondence between the values of the configuration property in the configuration file and the file names in the directory: {}/", DIR_LOCALES);
            throw new LocalizerException(String.format("Supported languages are not defined in the application. Check the correspondence between the values of the configuration property in the configuration file and the file names in the directory: %s/", DIR_LOCALES));
        }
        log.info("Found supported locales: {}", supportedLocales);
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
        Locale.setDefault(selectedLocale);

    }

    private Set<Locale> getSupportedLocale() throws LocalizerException {
        if (Objects.isNull(supportedLocales)) {
            Set<Locale> definedInConfig = getLocalesToUsedFromConfig();
            if (definedInConfig.isEmpty()) {
                log.error("Don't specified configuration property for defining to used locales");
                throw new LocalizerException("Don't specified configuration property for defining to used locales");
            }
            log.info("Specified locales to used in application: {}", definedInConfig);

            supportedLocales = definedInConfig.stream().filter(locSpecifiedFromConfig -> {
                String simpleFileName = toString(locSpecifiedFromConfig) + PROPERTIES_EXCEPTION; // for exam: ru.properties
                String source = String.join(ARCHIVE_SEPARATOR, DIR_LOCALES, simpleFileName); // for exam: lang/ru.properties
                boolean isExist = Objects.nonNull(ClassLoader.getSystemResourceAsStream(source)); // check if ru/en/any.properties exist in lang/
                if (!isExist) {
                    log.error("Specified values of locals in config file don't match with physically exists files of locale." +
                              " Was specified {}, but file not fonded {}",
                            locSpecifiedFromConfig, source);
                    throw new RuntimeException(new LocalizerException(String.format(
                            "Specified values of locals in config file don't match with physically exists files of locale." +
                            " Was specified %s, but file not fonded %s",
                            locSpecifiedFromConfig, source
                    )));
                }
                return isExist; // is always will be true
            }).collect(Collectors.toSet());
        }
        return supportedLocales;
    }

    /**
     * This method return specified locales in file of configuration : {@code application.properties} which will be included
     * in project to used
     *
     * @return locales specified in file configuration for using
     */
    public Set<Locale> getLocalesToUsedFromConfig() {
        return TicketGeneratorUtil.getConfig().getLocaleToUse().map(supportedLocales -> Stream.of(supportedLocales)
                        .filter(locale -> Objects.nonNull(isLocale(locale))).map(this::isLocale).collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
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
    public Locale isLocaleFile(String simpleFileName) {
        String lng_city = simpleFileName.substring(0, simpleFileName.indexOf(PROPERTIES_EXCEPTION));
        return isLocale(lng_city);
    }

    public Locale isLocale(String lng_city) {
        if (lng_city.length() > 5) return null;
        if (lng_city.contains("_")) {
            String[] args = lng_city.split(LOCALE_SEPARATOR);
            if (args.length > 2) {
                return null;
            }
            return new Locale(args[0], args[1]);
        }
        if (lng_city.length() > 2) return null;
        return new Locale(lng_city);
    }

    public String toString(Locale locale) {
        String separator = LOCALE_SEPARATOR;

        if (locale.getLanguage().isEmpty() || locale.getCountry().isEmpty()) {
            separator = "";
        }
        return String.join(separator, locale.getLanguage(), locale.getCountry());
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

    protected void runUpdate(Locale locale) {
        TicketGeneratorUtil.handlerExceptionUIAlert(() -> {
            handlers.parallelStream().forEach(l -> {
                l.onUpdateLocale(locale);
            });
        }).run();
    }

    public void addListener(LocalizerListener l) {
        handlers.add(l);
    }

    public void removeListener(LocalizerListener l) {
        handlers.remove(l);
    }

    @Override
    public void serialize() throws IOException {
        LocData data = new LocData();
        data.setSelectedLocale(selectedLocale);
        serializer.serialize(data);
    }

    /**
     * Цель создания этого метода была в том, чтобы не вмешиваясь в код проекта добавлять файл locale (новый язык) просто в
     * директорию.
     * <p>
     * <b>Теперь этот метод не используется, а причина проста</b>
     * <p>
     * - Реализация {@link #XgetSupportedLocaleX()} подразумевает поиск файлов locale-properties в lang директории
     * Если проект запускается из jar или из IDEA то все отрабатывает.
     * <b>НОООО</b> {@code если проект собрать в exe файл то ничего не работает, потому что exe - это исполняемый файл,
     * а не архивный файл и по нему нельзя пробежаться посмотреть какие файлы есть}
     *
     * @apiNote {@link #getSupportedLocales()}  Мне пришлось указать supported locales в application.properties, а причина проста.
     */
    @Deprecated
    private Set<Locale> XgetSupportedLocaleX() throws NotAccessToFileException, LocalizerException {
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
                        if (pathToFileOrDir.startsWith(DIR_LOCALES) && pathToFileOrDir.endsWith(".properties")) {
                            String simpleName = pathToFileOrDir.substring(pathToFileOrDir.lastIndexOf(ARCHIVE_SEPARATOR) + 1);
                            Locale locale = isLocaleFile(simpleName);
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
                        Locale locale = isLocaleFile(namePropertieFile);
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
}
