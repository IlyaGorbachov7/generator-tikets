package bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.impl;


import bntu.fitr.gorbachev.ticketsgenerator.main.TicketGeneratorUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.Localizer;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.LocalizerListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.BaseDialog;
import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.MyAnnotationCallback;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Locale;

/**
 * The class represents a dialog box object "Viewer file"
 *
 * @author Gorbachev I. D.
 * @version 03.05.2022
 */
public class FileViewer extends BaseDialog implements LocalizerListener {
    private SwingController control;
    private File file;

    /**
     * @param frame class object {@link JFrame}
     */
    public FileViewer(Window frame) {
        super(frame, Localizer.get("frame.title.preview"));
        this.setModal(true);
    }

    /**
     * The method initializes the panel
     */
    @Override
    public void initDialog() {
        TicketGeneratorUtil.getLocalsConfiguration().addListener(this);
        this.control = new SwingController();
        this.setBounds((int) getFrame().getBounds().getX(),
                (int) getFrame().getBounds().getY(), 900, 700);

        SwingViewBuilder factory = new SwingViewBuilder(control);
        JPanel pnlViewFile = factory.buildViewerPanel();

        ComponentKeyBinding.install(control, pnlViewFile);
        control.getDocumentViewController().setAnnotationCallback(
                new MyAnnotationCallback(control.getDocumentViewController()));

        this.add(pnlViewFile);
    }

    /**
     * @return current view file
     */
    public File getFile() {
        return file;
    }

    /**
     * This method set file for view
     *
     * @param file file for view
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * The method open specify file
     *
     * @throws Exception in case if specify file not exist
     */
    public void openDocument() throws Exception {
        control.openDocument(file.getPath());
    }

    @Override
    public void onUpdateLocale(Locale selectedLocale) {
        this.setTitle(Localizer.get("frame.title.preview"));
    }
}
