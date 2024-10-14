package locale;

import bntu.fitr.gorbachev.ticketsgenerator.main.TicketGeneratorUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.FilesUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.Localizer;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.LocalsConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

import static bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.LocalsConfiguration.DIR_LOCALES;

@Slf4j
public class LocaleTest {

    @Test
    void test() {
        System.out.println(Locale.getDefault());
        System.out.println(Arrays.toString(Locale.getAvailableLocales()));
        System.out.println(Locale.LanguageRange.MAX_WEIGHT);
        System.out.println(Arrays.toString(Locale.getISOCountries()));
    }

    @Test
    void test1() {
        System.getProperties().forEach((key, value) -> {
            System.out.println("key = " + key + " value = " + value);
        });
    }
    @Test
    void test2(){
        Locale defLoc = Locale.getDefault();
        System.out.println(defLoc);
        System.out.println("Country : "+defLoc.getCountry());
        System.out.println("Display Country: "+ defLoc.getDisplayCountry());
        System.out.println("languange: "+ defLoc.getLanguage());
        System.out.println("Display lang: "+ defLoc.getDisplayLanguage());
        System.out.println(defLoc.getScript());
        System.out.println(defLoc.getVariant());
        System.out.println(defLoc.getDisplayVariant());
        System.out.println(defLoc.toLanguageTag());
    }

    @Test
    void test3(){
        Locale loc = new Locale("ru");
        System.out.println(loc);
        System.out.println(loc.getLanguage());
        System.out.println(loc.getCountry());
        System.out.println(loc.getISO3Language());
        System.out.println(loc.getISO3Country());
    }

    @Test
    void test5() throws IOException, URISyntaxException {
        // 1
        System.out.println(LocalsConfiguration.class.getResource(DIR_LOCALES).getFile());
        // 2
        System.out.println(Paths.get(LocalsConfiguration.class.getResource(DIR_LOCALES).toURI()));
        // 3
        System.out.println(Path.of(LocalsConfiguration.class.getResource(DIR_LOCALES).toURI()));

        Path rootDirLocales = Path.of(Objects.requireNonNull(LocalsConfiguration.class.getResource(DIR_LOCALES)).toURI());
        System.out.println(rootDirLocales);
        Files.find(rootDirLocales, 1, (pathFile, attr) -> {
            System.out.println("a[a[a ::: " + pathFile);
            return true;
        });
    }

    @Test
    void test6(){
        String className = Localizer.class.getName().replace('.', '/');
        System.out.println("/" + className + ".class");
        String classJar = Localizer.class.getResource("/" + className + ".class").toString();
        System.out.println(classJar);
    }

    @Test
    void test7(){
        System.out.println( TicketGeneratorUtil.class.getResource("/resources/lang"));

        System.out.println(TicketGeneratorUtil.class.getResource(TicketGeneratorUtil.class.getName().replace(".","/")));

        System.out.println(TicketGeneratorUtil.class.getResource(
                "/"+ TicketGeneratorUtil.class.getName().replace(".","/") + ".class"));
    }

    @Test
    void test8() {
        String pathOfJar = URLDecoder.decode(FilesUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath(),
                StandardCharsets.UTF_8);
        System.out.println(pathOfJar);
        System.out.println(FilesUtil.class.getProtectionDomain().getCodeSource().getLocation());

    }

    @Test
    void test9() throws URISyntaxException, IOException {
        Path rootDirLocales = Path.of(Objects.requireNonNull(LocalsConfiguration.class.getResource(DIR_LOCALES)).toURI());
        System.out.println("Default dir locales file: "+ rootDirLocales);
        Files.find(rootDirLocales, 1, (pathFile, attr) -> {
            System.out.println("a[a[a ::: " + pathFile);
            return true;
        });
    }

}
