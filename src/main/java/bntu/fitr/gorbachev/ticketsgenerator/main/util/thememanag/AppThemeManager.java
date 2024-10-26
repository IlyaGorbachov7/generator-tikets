package bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag;

import bntu.fitr.gorbachev.ticketsgenerator.main.TicketGeneratorUtil;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Здесь будет запись данных в базу данных
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public class AppThemeManager {
    private static final ThemeAppConfiguration themeAppConfiguration;
    private static ThemeAppWrapper currentThemeWrapper;
    private static final Collection<ThemeChangerListener> handler = Collections.synchronizedCollection(new ArrayList<>());

    static {
        themeAppConfiguration = TicketGeneratorUtil.getThemeAppConfiguration();
        currentThemeWrapper = themeAppConfiguration.getCurrentThemeWrapper();
    }

    public static ThemeApp getCurrentTheme() {
        return currentThemeWrapper.getCurrentTheme();
    }

    public static void swapTheme() {
        synchronized (AppThemeManager.class) {
            currentThemeWrapper.setCurrentTheme((currentThemeWrapper.getCurrentTheme() == ThemeApp.LIGHT)
                    ? ThemeApp.NIGHT : ThemeApp.LIGHT);
            if (currentThemeWrapper.getCurrentTheme() == ThemeApp.LIGHT) {
                setLightTheme();
            } else {
                setDarkTheme();
            }
        }
    }

    public static void updateTheme() {
        synchronized (AppThemeManager.class) {
            if (currentThemeWrapper.getCurrentTheme() == ThemeApp.LIGHT) {
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
        handler.parallelStream().forEach((h) -> {
            try {
                h.updateComponent();
            } catch (Exception e) {
                log.warn("Updating component is failed: ", e);
            }
        });
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
}
