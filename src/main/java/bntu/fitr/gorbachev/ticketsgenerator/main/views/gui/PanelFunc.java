package bntu.fitr.gorbachev.ticketsgenerator.main.views.gui;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.ChangeFieldModelEvent;

import java.awt.*;
import java.awt.event.ActionEvent;

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

    void changeStateViewElems(ChangeFieldModelEvent event);

    void actionViewElems(ActionEvent event);

    void actionInitViewElems(InitViewEvent event);
}
