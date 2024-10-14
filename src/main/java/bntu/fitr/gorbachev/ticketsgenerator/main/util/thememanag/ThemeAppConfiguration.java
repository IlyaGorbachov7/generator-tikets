package bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag;

import bntu.fitr.gorbachev.ticketsgenerator.main.TicketGeneratorUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.serializer.SerializeListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.serializer.SerializeManager;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.serializer.Serializer;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.List;

@Log4j2
public class ThemeAppConfiguration implements SerializeListener {
    @Getter
    private ThemeAppWrapper currentThemeWrapper;

    private final Serializer serializer;

    public ThemeAppConfiguration() throws IOException {
        serializer = Serializer.getSerializer(TicketGeneratorUtil.getFileSerializeDirectory().toPath());
        SerializeManager.addListener(this);
        List<ThemeAppWrapper> objs = serializer.deserialize(ThemeAppWrapper.class);
        if (objs.isEmpty()) {
            log.warn("AppThemeManager: deserialize object: don't found");
            ThemeApp currentTheme = TicketGeneratorUtil.getThemeAppDefault();
            currentThemeWrapper = new ThemeAppWrapper(currentTheme);
        } else {
            currentThemeWrapper = objs.get(0);
        }
    }

    @Override
    public void serialize() throws IOException {
        serializer.serialize(currentThemeWrapper);
    }
}
