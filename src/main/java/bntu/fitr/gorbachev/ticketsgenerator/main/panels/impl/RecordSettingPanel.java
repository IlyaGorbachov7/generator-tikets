package bntu.fitr.gorbachev.ticketsgenerator.main.panels.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.WriterTicketProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.panels.BasePanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RecordSettingPanel extends BasePanel {
    /**
     * Data transfer object. This object is mapping GUI this panel.
     */
    private WriterTicketProperty property;

    public RecordSettingPanel(Window rootWindow) {
        super(rootWindow);
        initPanel();
        setConfigComponents();
        setComponentsListeners();
        property = new WriterTicketProperty();
    }

    {
        lbQuantityTicketOnSinglePage = new JLabel("Количество билетов на странице");
        spinnerQuantityTicketOnSinglePage = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        btnOk = new JButton("Ok");
    }

    private final JButton btnOk;

    @Override
    public void initPanel() {
        this.setBorder(new TitledBorder(""));
        this.setLayout(new BorderLayout());
        this.add(createPanel(), BorderLayout.CENTER);
        this.add(btnOk, BorderLayout.SOUTH);
    }

    @Override
    public void setComponentsListeners() {
        getRootFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                property.setQuantityOnSinglePage((Integer) spinnerQuantityTicketOnSinglePage.getValue());
            }
        });
        btnOk.addActionListener(e -> {
            property.setQuantityOnSinglePage((Integer) spinnerQuantityTicketOnSinglePage.getValue());
            getRootFrame().setVisible(false);
        });

        JFormattedTextField formattedTextField = ((JSpinner.NumberEditor) spinnerQuantityTicketOnSinglePage
                .getEditor()).getTextField();
        DefaultFormatter formatter = (DefaultFormatter) formattedTextField.getFormatter();
        formatter.setCommitsOnValidEdit(true);
    }

    @Override
    public void setConfigComponents() {

    }

    private final JLabel lbQuantityTicketOnSinglePage;
    private final JSpinner spinnerQuantityTicketOnSinglePage;

    private JPanel createPanel() {
        JPanel mainPnl = new JPanel(new BorderLayout());

        JPanel pnlRow = new JPanel();
        pnlRow.setLayout(new BoxLayout(pnlRow, BoxLayout.Y_AXIS));
        pnlRow.setBorder(new LineBorder(Color.BLACK));

        JPanel pnlColl = new JPanel();
        pnlColl.setLayout(new BoxLayout(pnlColl, BoxLayout.X_AXIS));

        JPanel pnlLbl = new JPanel(new BorderLayout());
        pnlLbl.add(lbQuantityTicketOnSinglePage);
        pnlColl.add(pnlLbl);

        pnlLbl = new JPanel(new BorderLayout());
        pnlLbl.add(spinnerQuantityTicketOnSinglePage);
        pnlColl.add(pnlLbl);

        pnlRow.add(pnlColl);

        mainPnl.add(pnlRow);
        return mainPnl;
    }

    public WriterTicketProperty getProperty() {
        return property;
    }

    public void setProperty(WriterTicketProperty property) {
        this.property = property;
    }

}
