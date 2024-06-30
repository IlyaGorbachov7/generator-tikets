package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.thememanag;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Здесь будет запись данных в базу данных
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppThemeManager {

    @Getter
    private static ThemeApp currentTheme;

    @Getter
    private static volatile boolean running;

    private static final Collection<ThemeChangerListener> handler = Collections.synchronizedCollection(new ArrayList<>());

    public static void run() {
        if (!running) {
            synchronized (AppThemeManager.class) {
                if (!running) {
                    System.out.println("Обращаемся к базе данных, берем от туда необходимые инфо о теме. если нет то создаем default nему");
                    running = true;
                    FlatLightLaf.setup();
                    currentTheme = ThemeApp.LIGHT;
                }
            }
        }
    }

    public static void swapTheme() {
        synchronized (AppThemeManager.class) {
            checkRunning();
            System.out.println("Вносим изминение в базе данных, что пользователь выбрал темную/свлетлую тему. Перезагружаем приложение полностю, но не завершаем программу, показываем уже нужнут тему");
            currentTheme = (currentTheme == ThemeApp.LIGHT) ? ThemeApp.NIGHT : ThemeApp.LIGHT;
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

    public enum ThemeApp {
        LIGHT,
        NIGHT
    }
}
