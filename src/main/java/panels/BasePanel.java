package panels;

import javax.swing.*;
import java.awt.*;

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

    public Window getFrame() {
        return frame;
    }

}

