package propmanag;

import bntu.fitr.gorbachev.ticketsgenerator.main.TicketGeneratorUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.Localizer;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.LocalsConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

public class LocaleConfigurationTest {

    static LocalsConfiguration configuration;

    @BeforeAll
    public static void init() {
        configuration = TicketGeneratorUtil.getLocalsConfiguration();
        configuration.setSelectedLocale(new Locale("ru"));
    }

    @Test
    void test() {
        String value = Localizer.get("panel.main.input.validator.symbols.access");
        System.out.println(value);
    }

    @Test
    void test2() {
        String value = Localizer.get("panel.main.input.validator.input.incorrect", "panel.main.input.validator.example");
        System.out.println(value);
    }

    @Test
    void test4() {
        String value  = Localizer.get("locale-test.value", "locale-test.advice", "locale-test.responce");
        System.out.println(value);
    }

    @Test
    void test6(){
        String value = Localizer.getWithValues("locale-test.value", "Хуй в польто", "Fack you");
        System.out.println(value);
    }

    @Test
    void test8() {
        String value = Localizer.getCombined("\n", Arrays.asList("dialog.title.input", "panel.message.title.msg", "panel.message.generation.success"));
        System.out.println(value);
    }

    @Test
    void test10(){
        String value = Localizer.getCombined(".\n",
                Map.entry("panel.main.input.validator.input.incorrect", new String[]{"panel.main.input.validator.example"}),
                Map.entry("locale-test.advice", new String[]{}),
                Map.entry("locale-test.value", new String[]{"locale-test.advice", "locale-test.responce"}));
        System.out.println(value);

    }
}
