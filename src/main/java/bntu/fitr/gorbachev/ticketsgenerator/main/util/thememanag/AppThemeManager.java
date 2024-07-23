package bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag;

import bntu.fitr.gorbachev.ticketsgenerator.main.Main;
import bntu.fitr.gorbachev.ticketsgenerator.main.TicketGeneratorUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.exep.NotAccessForReadToFileException;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.serializer.SerializeListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.serializer.SerializeManager;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.serializer.Serializer;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Здесь будет запись данных в базу данных
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class AppThemeManager {

    private static ThemeAppWrapper saveObj;
    @Getter
    private static ThemeApp currentTheme;

    @Getter
    private static volatile boolean running;


    private static Serializer serializer;
    private static final Collection<ThemeChangerListener> handler = Collections.synchronizedCollection(new ArrayList<>());

    public static void run() throws IOException {
        if (!running) {
            synchronized (AppThemeManager.class) {
                if (!running) {
                    running = true;
                    serializer = Serializer.getSerializer(TicketGeneratorUtil.getFileSerializeDirectory().toPath());
                    List<ThemeAppWrapper> objs = serializer.deserialize(ThemeAppWrapper.class);
                    if (objs.isEmpty()) {
                        log.warn("AppThemeManager: deserialize object: don't found");
                        currentTheme =  ThemeApp.LIGHT;
                        saveObj = new ThemeAppWrapper(currentTheme);
                        SerializeManager.addListener(saveObj);
                        updateTheme();
                    } else {
                        saveObj = objs.get(0);
                        SerializeManager.addListener(saveObj);
                        currentTheme = saveObj.currentTheme;
                        updateTheme();
                    }
                }
            }
        }
    }

    public static void swapTheme() {
        synchronized (AppThemeManager.class) {
            checkRunning();
            currentTheme = (currentTheme == ThemeApp.LIGHT) ? ThemeApp.NIGHT : ThemeApp.LIGHT;
            if (currentTheme == ThemeApp.LIGHT) {
                setLightTheme();
            } else {
                setDarkTheme();
            }
        }
    }

    public static void updateTheme() {
        synchronized (AppThemeManager.class) {
            checkRunning();
            if (currentTheme == ThemeApp.LIGHT) {
                setLightTheme();
            } else {
                setDarkTheme();
            }
        }
    }

    public static void updateComponentTreeUI(Component c) {
        updateComponentTreeUI0(c);
        c.invalidate();
        c.validate();
        c.repaint();
    }


    private static void fire() {
        handler.parallelStream().forEach(ThemeChangerListener::updateComponent);
    }

    public static void addThemeChangerListener(ThemeChangerListener listener) {
        handler.add(listener);
    }

    public static void removeChangerListener(ThemeChangerListener listener) {
        handler.remove(listener);
    }

    private static void setDarkTheme() {
        try {
            FlatDarkLaf.setup();
            fire();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setLightTheme() {
        try {
            FlatLightLaf.setup();
            fire();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateComponentTreeUI0(Component c) {
        if (c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            try {
                jc.updateUI();
                JPopupMenu jpm = jc.getComponentPopupMenu();
                if (jpm != null) {
                    updateComponentTreeUI(jpm);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Component[] children = null;
        if (c instanceof JMenu) {
            children = ((JMenu) c).getMenuComponents();
        } else if (c instanceof Container) {
            children = ((Container) c).getComponents();
        }
        if (children != null) {
            for (Component child : children) {
                updateComponentTreeUI0(child);
            }
        }
    }

    private static void checkRunning() {
        if (!running)
            throw new RuntimeException("ThemeAppManager has not been launched yet. Invoke method : ThemeAppManager.run()");
    }

    public static Serializable serialize() {
        return new ThemeAppWrapper(currentTheme);
    }

    public enum ThemeApp {
        LIGHT,
        NIGHT
    }

    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class ThemeAppWrapper implements Serializable, SerializeListener {
        ThemeApp currentTheme;

        {
            currentTheme = ThemeApp.LIGHT;
        }

        @Override
        public void serialize() throws IOException {
            serializer.serialize(new ThemeAppWrapper(AppThemeManager.currentTheme));
        }
    }
}
