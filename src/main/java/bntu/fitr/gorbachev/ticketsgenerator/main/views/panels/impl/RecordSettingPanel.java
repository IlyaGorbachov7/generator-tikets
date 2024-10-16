package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.TicketGeneratorUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.WriterTicketProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.Localizer;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.serializer.SerializeListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.serializer.SerializeManager;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.serializer.Serializer;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.BasePanel;
import lombok.SneakyThrows;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

public class RecordSettingPanel extends BasePanel implements SerializeListener {
    /**
     * Data transfer object. This object is mapping GUI this panel.
     */
    private WriterTicketProperty property;

    private static Serializer serializer;

    @SneakyThrows
    public RecordSettingPanel(Window rootWindow) {
        super(rootWindow);
        serializer = Serializer.getSerializer(TicketGeneratorUtil.getPathSerializeDirectory());
        List<WriterTicketProperty> list = serializer.deserialize(WriterTicketProperty.class);
        if (list.isEmpty()) {
            property = new WriterTicketProperty();
        } else {
            property = list.get(0);
        }
        SerializeManager.addListener(this);

        lbQuantityTicketOnSinglePage = new JLabel(Localizer.get("panel.recordsetting.quantityTickets"));
        spinnerQuantityTicketOnSinglePage = new JSpinner(new SpinnerNumberModel(property.getQuantityOnSinglePage(), 1, 5, 1));

        lbFontSize = new JLabel(Localizer.get("panel.recordsetting.fontSize"));
        spinnerFontSize = new JSpinner(new SpinnerNumberModel(property.getSizeFont(), 1, 20, 1));

        btnOk = new JButton(Localizer.get("btn.ok"));

        initPanel();
        setConfigComponents();
        setComponentsListeners();
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
                property.setSizeFont((Integer) spinnerFontSize.getValue());
            }
        });
        btnOk.addActionListener(e -> {
            property.setQuantityOnSinglePage((Integer) spinnerQuantityTicketOnSinglePage.getValue());
            property.setSizeFont((Integer) spinnerFontSize.getValue());
            getRootFrame().setVisible(false);
        });

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
    public void setConfigComponents() {

    }

    private final JLabel lbQuantityTicketOnSinglePage;
    private final JSpinner spinnerQuantityTicketOnSinglePage;

    private final JLabel lbFontSize;
    private final JSpinner spinnerFontSize;

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

    public WriterTicketProperty getProperty() {
        return property;
    }

    public void setProperty(WriterTicketProperty property) {
        this.property = property;
    }

    @Override
    public void serialize() throws IOException {
        serializer.serialize(property);
    }
}
