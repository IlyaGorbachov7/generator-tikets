package bntu.fitr.gorbachev.ticketsgenerator.main.panels;

import java.awt.*;

/**
 * This interface describes base functional {@link javax.swing.JPanel}
 */
public interface PanelFunc {
    /**
     * The method initialization panel
     */
    void initPanel();

    /**
     * This method is space for setting all components listener
     */
    void setComponentsListeners();

    /**
     * This method is space for setting components config
     */
    void setConfigComponents();

    Window getRootFrame();
}
