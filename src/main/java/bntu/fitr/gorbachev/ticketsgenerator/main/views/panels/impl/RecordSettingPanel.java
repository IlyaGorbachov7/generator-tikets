package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.TicketGeneratorUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.WriterTicketProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.Localizer;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.LocalizerListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.serializer.SerializeListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.serializer.SerializeManager;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.serializer.Serializer;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.BasePanel;
import lombok.SneakyThrows;
import org.apache.commons.math3.util.Pair;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class RecordSettingPanel extends BasePanel implements SerializeListener, LocalizerListener {
    /**
     * Data transfer object. This object is mapping GUI this panel.
     */
    private WriterTicketProperty property;

    private static Serializer serializer;

    @SneakyThrows
    public RecordSettingPanel(Window rootWindow) {
        super(rootWindow);
        TicketGeneratorUtil.getLocalsConfiguration().addListener(this);
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

        lblUniversity = new JLabel(Localizer.get("panel.recordsetting.include-in-ticket", "panel.main.university"));
        chkUniversity = new JCheckBox("", property.isIncludeUniversity());

        lblFaculty = new JLabel(Localizer.get("panel.recordsetting.include-in-ticket", "panel.main.faculty"));
        chkFaculty = new JCheckBox("", property.isIncludeFaculty());

        lblDepartment = new JLabel(Localizer.get("panel.recordsetting.include-in-ticket", "panel.main.department"));
        chkDepartment = new JCheckBox("", property.isIncludeDepartment());

        lblSpecialization = new JLabel(Localizer.get("panel.recordsetting.include-in-ticket", "panel.main.specialization"));
        chkSpecialization = new JCheckBox("", property.isIncludeSpecialization());

        lblDiscipline = new JLabel(Localizer.get("panel.recordsetting.include-in-ticket", "panel.main.discipline"));
        chkDiscipline = new JCheckBox("", property.isIncludeDiscipline());

        lblSessionType = new JLabel(Localizer.get("panel.recordsetting.include-in-ticket", "panel.main.sessionType"));
        chkSessionType = new JCheckBox("", property.isIncludeDiscipline());

        lblExam = new JLabel(Localizer.get("panel.recordsetting.include-in-ticket", "panel.recordsetting.exam"));
        chkExam = new JCheckBox("", property.isExam());

        lblHeadDepartment = new JLabel(Localizer.get("panel.recordsetting.include-in-ticket", "panel.main.departmentLeader"));
        chkHeadDepartment = new JCheckBox("", property.isIncludeHeadDepartment());

        lblTeacher = new JLabel(Localizer.get("panel.recordsetting.include-in-ticket", "panel.main.examiner"));
        chkTeacher = new JCheckBox("", property.isIncludeTeacher());

        lblProtocol = new JLabel(Localizer.getJoinValue(Localizer.get("panel.recordsetting.include-in-ticket",
                "panel.main.dataApproval"), Localizer.get("panel.main.protocol")));
        chkProtocol = new JCheckBox("", property.isIncludeProtocol());

        btnOk = new JButton(Localizer.get("btn.ok"));
        pnlTicketPreview = new TicketPreviewPatternPanel(rootWindow, property); // initialization of pnlTicketPreview must be performed AFTER all as you see now
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

        chkUniversity.addActionListener(e -> property.setIncludeUniversity(chkUniversity.isSelected()));
        chkFaculty.addActionListener(e -> property.setIncludeFaculty(chkFaculty.isSelected()));
        chkDepartment.addActionListener(e -> property.setIncludeDepartment(chkDepartment.isSelected()));
        chkSpecialization.addActionListener(e -> property.setIncludeSpecialization(chkSpecialization.isSelected()));
        chkDiscipline.addActionListener(e -> property.setIncludeDiscipline(chkDiscipline.isSelected()));
        chkSessionType.addActionListener(e -> property.setIncludeSessionType(chkSessionType.isSelected()));
        chkExam.addActionListener(e -> property.setExam(chkExam.isSelected()));
        chkHeadDepartment.addActionListener(e -> property.setIncludeHeadDepartment(chkHeadDepartment.isSelected()));
        chkTeacher.addActionListener(e -> property.setIncludeTeacher(chkTeacher.isSelected()));
        chkProtocol.addActionListener(e -> property.setIncludeProtocol(chkProtocol.isSelected()));
    }

    @Override
    public void setConfigComponents() {

    }

    private final JLabel lbQuantityTicketOnSinglePage;
    private final JSpinner spinnerQuantityTicketOnSinglePage;

    private final JLabel lbFontSize;
    private final JSpinner spinnerFontSize;

    private final JLabel lblUniversity;
    private final JCheckBox chkUniversity;

    private final JLabel lblFaculty;
    private final JCheckBox chkFaculty;

    private final JLabel lblDepartment;
    private final JCheckBox chkDepartment;

    private final JLabel lblSpecialization;
    private final JCheckBox chkSpecialization;

    private final JLabel lblDiscipline;
    private final JCheckBox chkDiscipline;

    private final JLabel lblSessionType;
    private final JCheckBox chkSessionType;

    private final JLabel lblExam;
    private final JCheckBox chkExam;

    private final JLabel lblHeadDepartment;
    private final JCheckBox chkHeadDepartment;

    private final JLabel lblTeacher;
    private final JCheckBox chkTeacher;

    private final JLabel lblProtocol;
    private final JCheckBox chkProtocol;

    private final TicketPreviewPatternPanel pnlTicketPreview;

    private JPanel createPanel() {
        JPanel mainPnl = new JPanel(new BorderLayout());

        JPanel pnlRow = new JPanel();
        pnlRow.setLayout(new BoxLayout(pnlRow, BoxLayout.Y_AXIS));
        pnlRow.setBorder(new LineBorder(Color.BLACK));

        Stream.of(
                Pair.create(lbQuantityTicketOnSinglePage, spinnerQuantityTicketOnSinglePage),
                Pair.create(lbFontSize, spinnerFontSize),
                Pair.create(lblUniversity, chkUniversity),
                Pair.create(lblFaculty, chkFaculty),
                Pair.create(lblDepartment, chkDepartment),
                Pair.create(lblSpecialization, chkSpecialization),
                Pair.create(lblDiscipline, chkDiscipline),
                Pair.create(lblSessionType, chkSessionType),
                Pair.create(lblExam, chkExam),
                Pair.create(lblHeadDepartment, chkHeadDepartment),
                Pair.create(lblTeacher, chkTeacher),
                Pair.create(lblProtocol, chkProtocol)
        ).forEach(pair -> {
            JLabel l = pair.getKey();
            JComponent c = pair.getValue();
            JPanel pnlColl = new JPanel();
            pnlColl.setLayout(new BoxLayout(pnlColl, BoxLayout.X_AXIS));

            JPanel pnlLblAndSpinner = new JPanel(new BorderLayout());
            pnlLblAndSpinner.add(l);
            pnlColl.add(pnlLblAndSpinner);
            pnlLblAndSpinner = new JPanel(new BorderLayout());
            pnlLblAndSpinner.add(c, BorderLayout.EAST);
            pnlLblAndSpinner.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            pnlColl.add(pnlLblAndSpinner);
            pnlColl.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));
            pnlRow.add(pnlColl);
        });
        pnlRow.setPreferredSize(new Dimension(400, 300));
        mainPnl.add(pnlRow);
//        pnlTicketPreview.setMinimumSize(new Dimension(400, 300));
//        pnlTicketPreview.getRootPnl().setMinimumSize(new Dimension(400, 300));
        mainPnl.add(pnlTicketPreview,BorderLayout.EAST);
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

    @Override
    public void onUpdateLocale(Locale selectedLocale) {
        lbQuantityTicketOnSinglePage.setText(Localizer.get("panel.recordsetting.quantityTickets"));
        lbFontSize.setText(Localizer.get("panel.recordsetting.fontSize"));
        lblUniversity.setText(Localizer.get("panel.recordsetting.include-in-ticket", "panel.main.university"));
        lblFaculty.setText(Localizer.get("panel.recordsetting.include-in-ticket", "panel.main.faculty"));
        lblDepartment.setText(Localizer.get("panel.recordsetting.include-in-ticket", "panel.main.department"));
        lblSpecialization.setText(Localizer.get("panel.recordsetting.include-in-ticket", "panel.main.specialization"));
        lblDiscipline.setText(Localizer.get("panel.recordsetting.include-in-ticket", "panel.main.discipline"));
        lblSessionType.setText(Localizer.get("panel.recordsetting.include-in-ticket", "panel.main.sessionType"));
        lblExam.setText(Localizer.get("panel.recordsetting.include-in-ticket", "panel.recordsetting.exam"));
        lblHeadDepartment.setText(Localizer.get("panel.recordsetting.include-in-ticket", "panel.main.departmentLeader"));
        lblTeacher.setText(Localizer.get("panel.recordsetting.include-in-ticket", "panel.main.examiner"));
        lblProtocol.setText(Localizer.getJoinValue(Localizer.get("panel.recordsetting.include-in-ticket",
                "panel.main.dataApproval"), Localizer.get("panel.main.protocol")));
        btnOk.setText(Localizer.get("btn.ok"));
        pnlTicketPreview.onUpdateLocale(selectedLocale);
    }
}
