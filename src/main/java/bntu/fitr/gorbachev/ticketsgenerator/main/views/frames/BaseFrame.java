package bntu.fitr.gorbachev.ticketsgenerator.main.views.frames;

import bntu.fitr.gorbachev.ticketsgenerator.main.TicketGeneratorUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.AppThemeManager;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.ThemeChangerListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.DialogFunc;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.PanelType;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public abstract class BaseFrame extends JFrame implements DialogFunc, ThemeChangerListener {
    private PanelType panelType;

	{
		AppThemeManager.addThemeChangerListener(this);
		// Установка иконки через ImageIO
		TicketGeneratorUtil.getConfig().getAppIcon().ifPresent(classpathIcon -> {
			try {
				Image icon = ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource(classpathIcon)));
				this.setIconImage(icon);
			} catch (Exception e) {
				log.warn("Loading icon was failed: {}", classpathIcon);
			}
		});
	}

    public BaseFrame() throws HeadlessException {
    }

    public BaseFrame(String title) throws HeadlessException {
        super(title);
    }

    public BaseFrame(PanelType panelType) throws HeadlessException {
        this.panelType = panelType;
    }

    public BaseFrame(String title, PanelType panelType) throws HeadlessException {
        super(title);
        this.panelType = panelType;
    }

    public PanelType getPanelType() {
        return panelType;
    }

    public void setPanelType(PanelType panelType) {
        this.panelType = panelType;
    }

    @Override
    public abstract void initDialog();

    /**
     * This update whole frame and all items inside. So you don't need to add handlerListener
     * for root panel. If also add handler listener inside root any panel then this updating
     * gui will be twice, that may try any exception.
     * <p>
     * <b>Therefore, it is enough to add handler only here</b>
     */
    @Override
    public Component getComponent() {
        return this;
    }
}
