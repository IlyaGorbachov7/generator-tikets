package bntu.fitr.gorbachev.ticketsgenerator.main.util.loc;

import bntu.fitr.gorbachev.ticketsgenerator.main.TicketGeneratorUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.FilesUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.exep.NotAccessToFileException;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.serializer.SerializeListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.serializer.Serializer;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

@Log4j2
public class LocalsConfiguration implements SerializeListener {
    public static final String DIR_LOCALES = "/resources/lang";

    private final Locale systemLocale = Locale.getDefault();
    private Locale selectedLocale = systemLocale;

    private Serializer serializer = Serializer.getSerializer(TicketGeneratorUtil.getPathSerializeDirectory());

    public LocalsConfiguration() throws IOException {
        List<LocData> data = serializer.deserialize(LocData.class);
        if (data.isEmpty()) {
            defineLocale();
        } else {
            selectedLocale = data.get(0).getSelectedLocale();
        }
        getSupportedLocale();
    }

    private void defineLocale() {


    }

    private List<Locale> getSupportedLocale() throws NotAccessToFileException {
        if (FilesUtil.isRunningFromJar()) {
            String pathJarFile = LocalsConfiguration.class.getProtectionDomain()
                    .getCodeSource().getLocation().getPath();
            Stream.Builder<Locale> builderList = Stream.builder();
            try (JarFile jarFile = new JarFile(pathJarFile)) {
                var entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entity = entries.nextElement();
                    String pathToFileOrDir = entity.getName();
                    if (pathToFileOrDir.startsWith(DIR_LOCALES.replaceFirst("/", "")) && pathToFileOrDir.endsWith(".properties")) {
                        String simpleName = pathToFileOrDir.substring(pathToFileOrDir.lastIndexOf("/") + 1);
                        Locale locale = isLocale(simpleName);
                        if (Objects.nonNull(locale)) {
                            builderList.accept(locale);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                Path rootDirLocales = Path.of(Objects.requireNonNull(LocalsConfiguration.class.getResource(DIR_LOCALES)).toURI());
                log.info("Default dir locales file: {}", rootDirLocales);
                Files.find(rootDirLocales, 1, (pathFile, attr) -> {
                    System.out.println("a[a[a ::: " + pathFile);
                    return true;
                });
            } catch (Exception e) {
                throw new NotAccessToFileException("Directory locale undefined", e, Path.of(DIR_LOCALES));
            }
        }

        return null;
    }

    private Locale isLocale(String simpleName) {
        log.debug("Find file Simple name: {}", simpleName);
        return null;
    }


    @Override
    public void serialize() throws IOException {
        LocData data = new LocData();
        data.setSelectedLocale(selectedLocale);
        serializer.serialize(data);
    }
}
