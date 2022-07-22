package frames.impl;


import frames.DialogFunc;
import panels.PanelFactory;
import panels.PanelType;

import javax.swing.*;
import java.awt.*;

/**
 * This class is represented {@link JFrame}
 */
public class LaunchFrame extends JFrame implements DialogFunc {
    public static final Toolkit toolkit = Toolkit.getDefaultToolkit();

    /**
     * This constructor created this Frame
     */
    public LaunchFrame(PanelType type) {
        Dimension sizeScreen = toolkit.getScreenSize();
        Dimension sizeFrame = new Dimension(sizeScreen.width / 2 - 100,
                sizeScreen.height / 2);
        this.setLayout(new BorderLayout());

        JPanel splashScreenPanel = PanelFactory.getInstance().createPanel(this, type);

        this.add(splashScreenPanel, BorderLayout.CENTER);

        this.setBounds((sizeScreen.width - sizeFrame.width) / 2,
                (sizeScreen.height - sizeFrame.height) / 2,
                sizeFrame.width, sizeFrame.height);
        this.setMinimumSize(sizeFrame);
        this.setResizable(false);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void initDialog() {

    }
}
