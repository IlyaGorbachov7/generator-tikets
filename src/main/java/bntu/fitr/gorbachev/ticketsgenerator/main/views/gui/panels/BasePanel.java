package bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.ChangeFieldModelEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.InitViewEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.PanelFunc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * This class present base panel with basic functionality and
 * interact with {@link JPanel}
 *
 * @author SecuRiTy
 * @version 27.05.2022
 */
public abstract class BasePanel extends JPanel implements PanelFunc {
    protected Window frame;

    public BasePanel(Window frame) {
        this.frame = frame;
    }

    @Override
    public abstract void initPanel();

    @Override
    public abstract void setComponentsListeners();

    @Override
    public abstract void setConfigComponents();

    @Override
    public abstract void changeStateViewElems(ChangeFieldModelEvent event);

    @Override
    public void actionViewElems(ActionEvent event) {
    }

    @Override
    public abstract void actionInitViewElems(InitViewEvent event);

    @Override
    public Window getRootFrame() {
        return frame;
    }
}

