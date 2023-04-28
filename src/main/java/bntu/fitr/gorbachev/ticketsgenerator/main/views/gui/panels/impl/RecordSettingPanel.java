package bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.models.WriterTicketProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.ChangeFieldModelEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.controller.impl.RecordSettingController;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.InitViewEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.BasePanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.model.impl.RecordSettingModel;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static bntu.fitr.gorbachev.ticketsgenerator.main.views.model.impl.perconst.RecordSettingFieldNameConst.FONT_SIZE;
import static bntu.fitr.gorbachev.ticketsgenerator.main.views.model.impl.perconst.RecordSettingFieldNameConst.QUANTITY_TICKET_ON_SINGLEPAGE;

@Getter
public class RecordSettingPanel extends BasePanel {
    /**
     * Data transfer object. This object is mapping GUI this panel.
     */
    private WriterTicketProperty property;
    private final RecordSettingController controller;

    private final JButton btnOk;

    private final JLabel lbQuantityTicketOnSinglePage;
    private final JSpinner spinnerQuantityTicketOnSinglePage;
    private final int minVQuantityTicketSinglePage = 1;
    private final int maxVQuantityTicketOnSinglePage = 5;
    private final int stepQuantityTicketOnSignlePage = 1;
    private int valueQuantityTicketOnSinglePage = 1;

    private final JLabel lbFontSize;
    private final JSpinner spinnerFontSize;
    private final int minVFontSize = 1;
    private final int maxVFontSize = 20;
    private final int stepFontSize = 1;
    private int valueFontSize = 14;

    {
        lbQuantityTicketOnSinglePage = new JLabel("Количество билетов на странице");
        spinnerQuantityTicketOnSinglePage = new JSpinner(new SpinnerNumberModel(valueQuantityTicketOnSinglePage,
                minVQuantityTicketSinglePage, maxVQuantityTicketOnSinglePage, stepQuantityTicketOnSignlePage));

        lbFontSize = new JLabel("Размер шрифта");
        spinnerFontSize = new JSpinner(new SpinnerNumberModel(valueFontSize, minVFontSize, maxVFontSize, stepFontSize));

        btnOk = new JButton("Ok");
    }

    public RecordSettingPanel(Window rootWindow) {
        super(rootWindow);
        initPanel();
        setConfigComponents();
        setComponentsListeners();
        property = new WriterTicketProperty();
        controller = new RecordSettingController(this, new RecordSettingModel());
        controller.actionInitViewByModel();
    }

    @Override
    public void initPanel() {
        this.setBorder(new TitledBorder(""));
        this.setLayout(new BorderLayout());
        this.add(createPanel(), BorderLayout.CENTER);
        this.add(btnOk, BorderLayout.SOUTH);
    }

    private JPanel createPanel() {
        JPanel mainPnl = new JPanel(new BorderLayout());

        JPanel pnlRow = new JPanel();
        pnlRow.setLayout(new BoxLayout(pnlRow, BoxLayout.Y_AXIS));
        pnlRow.setBorder(new LineBorder(Color.BLACK));

        //------------------------------------------------------------------------------------
        JPanel pnlColl = new JPanel();
        pnlColl.setLayout(new BoxLayout(pnlColl, BoxLayout.X_AXIS));

        JPanel pnlLblAndSpinner = new JPanel(new BorderLayout());
        pnlLblAndSpinner.add(lbQuantityTicketOnSinglePage);
        pnlColl.add(pnlLblAndSpinner);
        pnlLblAndSpinner = new JPanel(new BorderLayout());
        pnlLblAndSpinner.add(spinnerQuantityTicketOnSinglePage);
        pnlColl.add(pnlLblAndSpinner);

        pnlRow.add(pnlColl);
        //------------------------------------------------------------------------------------

        pnlColl = new JPanel();
        pnlColl.setLayout(new BoxLayout(pnlColl, BoxLayout.X_AXIS));

        pnlLblAndSpinner = new JPanel(new BorderLayout());
        pnlLblAndSpinner.add(lbFontSize);
        pnlColl.add(pnlLblAndSpinner);
        pnlLblAndSpinner = new JPanel(new BorderLayout());
        pnlLblAndSpinner.add(spinnerFontSize);
        pnlColl.add(pnlLblAndSpinner);

        pnlRow.add(pnlColl);
        //------------------------------------------------------------------------------------

        mainPnl.add(pnlRow);
        return mainPnl;
    }

    @Override
    public void setConfigComponents() {
        JFormattedTextField formattedTextField = ((JSpinner.NumberEditor) spinnerQuantityTicketOnSinglePage
                .getEditor()).getTextField();
        DefaultFormatter formatter = (DefaultFormatter) formattedTextField.getFormatter();
        formatter.setCommitsOnValidEdit(true);

        formattedTextField = ((JSpinner.NumberEditor) spinnerFontSize
                .getEditor()).getTextField();
        formatter = (DefaultFormatter) formattedTextField.getFormatter();
        formatter.setCommitsOnValidEdit(true);
    }

    @Override
    public void setComponentsListeners() {
        getRootFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.actionWindowClosing((Integer) spinnerQuantityTicketOnSinglePage.getValue(),
                        (Integer) spinnerFontSize.getValue());
            }
        });
        btnOk.addActionListener(e -> {
            controller.actionOkBtn((Integer) spinnerQuantityTicketOnSinglePage.getValue(),
                    (Integer) spinnerFontSize.getValue());
            getRootFrame().setVisible(false);
        });
    }

    @Override
    public void primaryInitViewElems(InitViewEvent event) {
        event.getFields().forEach(this::setNewValueFieldByName);
    }

    @Override
    public void changeStateViewElems(ChangeFieldModelEvent event) {
        setNewValueFieldByName(event.getFieldName(), event.getNewValue());
    }

    private void setNewValueFieldByName(String fieldName, Object newValue) {
        switch (fieldName) {
            case QUANTITY_TICKET_ON_SINGLEPAGE -> {
                valueQuantityTicketOnSinglePage = (int) newValue;
                property.setQuantityOnSinglePage((Integer) newValue);
            }
            case FONT_SIZE -> {
                valueFontSize = (int) newValue;
                property.setSizeFont((Integer) newValue);
            }
        }
    }

    public WriterTicketProperty getProperty() {
        return property;
    }

    public void setProperty(WriterTicketProperty property) {
        this.property = property;
    }

}
