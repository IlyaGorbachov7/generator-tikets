package subtest1.subsubtest1;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.Serializer;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.thememanag.AppThemeManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Test11Serializer {

    @Test
    void test11() throws IOException {
        System.out.println(Serializer.deserialize(AppThemeManager.class.getClass()).size());
    }

    @Test
    void testSer() throws IOException {
        AppThemeManager.ThemeAppWrapper wrapper = new AppThemeManager.ThemeAppWrapper(AppThemeManager.ThemeApp.LIGHT);
        Serializer.serialize(wrapper);
    }
}
