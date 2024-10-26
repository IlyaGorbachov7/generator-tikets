package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.basis.WriterTicketProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.Localizer;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.LocalizerListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.AppThemeManager;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.ColorManager;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.ThemeApp;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.ThemeChangerListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.BasePanel;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TicketPreviewPatternPanel extends BasePanel implements LocalizerListener, ThemeChangerListener {
    @Getter
    private JPanel rootPnl;
    private JLabel lblUniversity;
    private JLabel lblFaculty;
    private JLabel lblDepartment;
    private JLabel lblSpecialization;
    private JLabel lblExam;
    private JLabel lblSessionType;
    private JLabel lblQuestion3;
    private JLabel lblQuestion1;
    private JLabel lblQuestion2;
    private JPanel pnlInfoHeadDepAndTeacher;
    private JLabel lblHeadDep;
    private JLabel lblTeacher;
    private JPanel pnlApprovalAndProtocol;
    private JLabel lblApproval;
    private JLabel lblData;
    private JLabel lblProtocol;
    private JLabel lblDiscipline;

    private JLabel lblTicketPattern;
    private final Dimension size = new Dimension(0, 0);
    private final GridConstraints constraintsEmpty = new GridConstraints(1, 0, 1, 1,
            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED,
            GridConstraints.SIZEPOLICY_FIXED, size, size, size, 0, false);

    private final WriterTicketProperty ticketProperty;
    private final Map<Component, JLabel> orderComponents;


    public TicketPreviewPatternPanel(Window frame, WriterTicketProperty property) {
        super(frame);
        this.ticketProperty = property;
        orderComponents = new ConcurrentHashMap<>();
        initPanel();
    }

    @Override
    public void initPanel() {
        this.setLayout(new BorderLayout(10, 10));
        lblTicketPattern = new JLabel("Шаблон билета");
        this.add(lblTicketPattern, BorderLayout.NORTH);
        this.add(rootPnl, BorderLayout.CENTER);
        initOrderComponents();
        setConfigComponents();
        setComponentsListeners();
    }

    protected void initOrderComponents() {
        for (Component c : rootPnl.getComponents()) {
            JLabel label = new JLabel();
            label.setBackground(Color.red);
            orderComponents.put(c, label);
        }
    }

    @Override
    public void setConfigComponents() {
        rootPnl.setBorder(BorderFactory.createMatteBorder(80, 15, 80, 15,
                ColorThemeConfig.COLOR_BORDER_TICKET_PATTERN.getColor()));

        Font lblUniversityFont = this.$$$getFont$$$("Romantic", Font.BOLD, 18, lblTicketPattern.getFont());
        if (lblUniversityFont != null) lblTicketPattern.setFont(lblUniversityFont);
        lblTicketPattern.setHorizontalAlignment(SwingConstants.CENTER);
        lblTicketPattern.setVerticalAlignment(SwingConstants.CENTER);

//        onUpdateLocale(TicketGeneratorUtil.getLocalsConfiguration().getSelectedLocale());
    }

    @Override
    public void onUpdateLocale(Locale selectedLocale) {
        lblTicketPattern.setText(Localizer.get(""));
        lblUniversity.setText(Localizer.get(""));
        lblFaculty.setText(Localizer.get(""));
        lblDepartment.setText(Localizer.get(""));
        lblSpecialization.setText(Localizer.get(""));
        lblHeadDep.setText(Localizer.get(""));
        lblTeacher.setText(Localizer.get(""));
        lblSessionType.setText(Localizer.get(""));
        lblQuestion1.setText(Localizer.get(""));
        lblQuestion2.setText(Localizer.get(""));
        lblQuestion3.setText(Localizer.get(""));
        lblApproval.setText(Localizer.get(""));
        lblProtocol.setText(Localizer.get(""));
        lblDiscipline.setText(Localizer.get(""));
    }

    @Override
    public void setComponentsListeners() {
        AppThemeManager.addThemeChangerListener(this);
        rootPnl.addContainerListener(new ContainerListener() {
            @Override
            public void componentAdded(ContainerEvent e) {
                int hGrow = e.getChild().getHeight();
                if (orderComponents.containsKey(e.getChild())) {
                    MatteBorder border = (MatteBorder) rootPnl.getBorder();
                    Insets borderInsets = border.getBorderInsets();
                    borderInsets.top -= hGrow / 2;
                    borderInsets.bottom -= hGrow / 2;
                    rootPnl.setBorder(BorderFactory.createMatteBorder(borderInsets.top, borderInsets.left, borderInsets.bottom, borderInsets.right, border.getMatteColor()));
                }
            }

            @Override
            public void componentRemoved(ContainerEvent e) {
                int hGrow = e.getChild().getHeight();
                if (orderComponents.containsKey(e.getChild())) {
                    MatteBorder border = (MatteBorder) rootPnl.getBorder();
                    Insets borderInsets = border.getBorderInsets();
                    borderInsets.top += hGrow / 2;
                    borderInsets.bottom += hGrow / 2;
                    rootPnl.setBorder(BorderFactory.createMatteBorder(borderInsets.top, borderInsets.left, borderInsets.bottom, borderInsets.right, border.getMatteColor()));
                }
            }
        });

        WriterTicketProperty.HandlersOnProperties handlersOnTicketProperty = ticketProperty.getHandlersOnProperties();
        handlersOnTicketProperty.setOnIncludeUniversity((oldV, newV) -> {
            if (newV) {
                int index = rootPnl.getComponentZOrder(orderComponents.get(lblUniversity));
                rootPnl.remove(orderComponents.get(lblUniversity));
                rootPnl.add(lblUniversity, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW,
                                GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false),
                        index);
            } else {
                int index = rootPnl.getComponentZOrder(lblUniversity);
                rootPnl.remove(lblUniversity);
                rootPnl.add(orderComponents.get(lblUniversity), constraintsEmpty, index);
            }
//            rootPnl.updateUI();
            TicketPreviewPatternPanel.this.updateUI();
            this.repaint();
        });

        handlersOnTicketProperty.setOnIncludeFaculty((oldV, newV) -> {
            if (newV) {
                int index = rootPnl.getComponentZOrder(orderComponents.get(lblFaculty));
                rootPnl.remove(orderComponents.get(lblFaculty));
                rootPnl.add(lblFaculty, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false),
                        index);
            } else {
                int index = rootPnl.getComponentZOrder(lblFaculty);
                rootPnl.remove(lblFaculty);
                rootPnl.add(orderComponents.get(lblFaculty), constraintsEmpty, index);
            }
//            rootPnl.updateUI();
            TicketPreviewPatternPanel.this.updateUI();
            this.repaint();
        });

        handlersOnTicketProperty.setOnIncludeDepartment((oldV, newV) -> {
            if (newV) {
                int index = rootPnl.getComponentZOrder(orderComponents.get(lblDepartment));
                rootPnl.remove(orderComponents.get(lblDepartment));
                rootPnl.add(lblDepartment, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false),
                        index);
            } else {
                int index = rootPnl.getComponentZOrder(lblDepartment);
                rootPnl.remove(lblDepartment);
                rootPnl.add(orderComponents.get(lblDepartment), constraintsEmpty, index);
            }
//            rootPnl.updateUI();
            TicketPreviewPatternPanel.this.updateUI();
            this.repaint();
        });

        handlersOnTicketProperty.setOnIncludeSpecialization((oldV, newV) -> {
            if (newV) {
                int index = rootPnl.getComponentZOrder(orderComponents.get(lblSpecialization));
                rootPnl.remove(orderComponents.get(lblSpecialization));
                rootPnl.add(lblSpecialization, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false),
                        index);
            } else {
                int index = rootPnl.getComponentZOrder(lblSpecialization);
                rootPnl.remove(lblSpecialization);
                rootPnl.add(orderComponents.get(lblSpecialization), constraintsEmpty, index);
            }
//            rootPnl.updateUI();
            TicketPreviewPatternPanel.this.updateUI();
            this.repaint();
        });

        handlersOnTicketProperty.setOnIncludeDiscipline((oldV, newV) -> {
            if (newV) {
                int index = rootPnl.getComponentZOrder(orderComponents.get(lblDiscipline));
                rootPnl.remove(orderComponents.get(lblDiscipline));
                rootPnl.add(lblDiscipline, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false),
                        index);
            } else {
                int index = rootPnl.getComponentZOrder(lblDiscipline);
                rootPnl.remove(lblDiscipline);
                rootPnl.add(orderComponents.get(lblDiscipline), constraintsEmpty, index);
            }
//            rootPnl.updateUI();
            TicketPreviewPatternPanel.this.updateUI();
            this.repaint();
        });

        handlersOnTicketProperty.setOnIncludeHeadDepartment((oldV, newV) -> {
            if (newV) {
                lblHeadDep.setVisible(true);
                if (!ticketProperty.isIncludeTeacher()) {
                    int index = rootPnl.getComponentZOrder(orderComponents.get(pnlInfoHeadDepAndTeacher));
                    rootPnl.remove(orderComponents.get(pnlInfoHeadDepAndTeacher));
                    rootPnl.add(pnlInfoHeadDepAndTeacher, new GridConstraints(12, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false),
                            index);
//                    rootPnl.updateUI();
                    TicketPreviewPatternPanel.this.updateUI();
                    this.repaint();
                }
            } else {
                lblHeadDep.setVisible(false);
                if (!ticketProperty.isIncludeTeacher()) {
                    int index = rootPnl.getComponentZOrder(pnlInfoHeadDepAndTeacher);
                    rootPnl.remove(pnlInfoHeadDepAndTeacher);
                    rootPnl.add(orderComponents.get(pnlInfoHeadDepAndTeacher), constraintsEmpty, index);

//                    rootPnl.updateUI();
                    TicketPreviewPatternPanel.this.updateUI();
                    this.repaint();
                }
            }
        });

        handlersOnTicketProperty.setOnIncludeTeacher((oldV, newV) -> {
            if (newV) {
                lblTeacher.setVisible(true);
                if (!ticketProperty.isIncludeHeadDepartment()) {
                    int index = rootPnl.getComponentZOrder(orderComponents.get(pnlInfoHeadDepAndTeacher));
                    rootPnl.remove(orderComponents.get(pnlInfoHeadDepAndTeacher));
                    rootPnl.add(pnlInfoHeadDepAndTeacher, new GridConstraints(12, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false),
                            index);

//                    rootPnl.updateUI();
                    TicketPreviewPatternPanel.this.updateUI();
                    this.repaint();
                }
            } else {
                lblTeacher.setVisible(false);
                if (!ticketProperty.isIncludeHeadDepartment()) {
                    int index = rootPnl.getComponentZOrder(pnlInfoHeadDepAndTeacher);
                    rootPnl.remove(pnlInfoHeadDepAndTeacher);
                    rootPnl.add(orderComponents.get(pnlInfoHeadDepAndTeacher), constraintsEmpty, index);

//                    rootPnl.updateUI();
                    TicketPreviewPatternPanel.this.updateUI();
                    this.repaint();
                }
            }
        });

        handlersOnTicketProperty.setOnIncludeSessionType((oldV, newV) -> {
            if (newV) {
                int index = rootPnl.getComponentZOrder(orderComponents.get(lblSessionType));
                rootPnl.remove(orderComponents.get(lblSessionType));
                rootPnl.add(lblSessionType, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false),
                        index);
            } else {
                int index = rootPnl.getComponentZOrder(lblDiscipline);
                rootPnl.remove(lblSessionType);
                rootPnl.add(orderComponents.get(lblSessionType), constraintsEmpty, index);
            }
//            rootPnl.updateUI();
            TicketPreviewPatternPanel.this.updateUI();
            this.repaint();
        });

        handlersOnTicketProperty.setOnIncludeProtocol((oldV, newV) -> {
            if (newV) {
                int index = rootPnl.getComponentZOrder(orderComponents.get(pnlApprovalAndProtocol));
                rootPnl.remove(orderComponents.get(pnlApprovalAndProtocol));
                rootPnl.add(pnlApprovalAndProtocol, new GridConstraints(13, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false),
                        index);
            } else {
                int index = rootPnl.getComponentZOrder(pnlApprovalAndProtocol);
                rootPnl.remove(pnlApprovalAndProtocol);
                rootPnl.add(orderComponents.get(pnlApprovalAndProtocol), constraintsEmpty, index);
            }

//            rootPnl.updateUI();
            TicketPreviewPatternPanel.this.updateUI();
            this.repaint();

        });

        handlersOnTicketProperty.setOnExam((oldV, newV) -> {
            if (newV) {
                lblExam.setText("Экзаменационный билет №1");
            } else {
                lblExam.setText("Билет №1");
            }
        });
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        rootPnl = new JPanel();
        rootPnl.setLayout(new GridLayoutManager(14, 1, new Insets(10, 10, 10, 10), -1, -1));
        lblUniversity = new JLabel();
        Font lblUniversityFont = this.$$$getFont$$$("Romantic", Font.BOLD, -1, lblUniversity.getFont());
        if (lblUniversityFont != null) lblUniversity.setFont(lblUniversityFont);
        lblUniversity.setName("");
        lblUniversity.setText("БЕЛОРУССКИЙ НАЦИОНАЛЬНЫЙ ТЕХНИЧЕСКИЙ УНИВЕРСИТЕТ");
        rootPnl.add(lblUniversity, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblFaculty = new JLabel();
        Font lblFacultyFont = this.$$$getFont$$$("Romantic", Font.BOLD, -1, lblFaculty.getFont());
        if (lblFacultyFont != null) lblFaculty.setFont(lblFacultyFont);
        lblFaculty.setText("Факультет информационных технологий и радиоэлектронники");
        rootPnl.add(lblFaculty, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblDepartment = new JLabel();
        lblDepartment.setText("Кафедра программного обеспечения информационных систем и технологий");
        rootPnl.add(lblDepartment, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblSpecialization = new JLabel();
        Font lblSpecializationFont = this.$$$getFont$$$(null, -1, -1, lblSpecialization.getFont());
        if (lblSpecializationFont != null) lblSpecialization.setFont(lblSpecializationFont);
        lblSpecialization.setOpaque(false);
        lblSpecialization.setText("Инжинер-программист (в проектировании и производстве)");
        lblSpecialization.setVisible(true);
        rootPnl.add(lblSpecialization, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblExam = new JLabel();
        Font lblExamFont = this.$$$getFont$$$("Romantic", Font.BOLD, -1, lblExam.getFont());
        if (lblExamFont != null) lblExam.setFont(lblExamFont);
        lblExam.setText("ЭКЗАМЕНАЦИОННЫЙ БИЛЕТ №1");
        lblExam.setVisible(true);
        rootPnl.add(lblExam, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblSessionType = new JLabel();
        lblSessionType.setText("Зимняя экзаменационная сессия 2022/2023 учебного года");
        rootPnl.add(lblSessionType, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblQuestion3 = new JLabel();
        lblQuestion3.setText("3) Содержимое вопроса");
        rootPnl.add(lblQuestion3, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 3, false));
        final Spacer spacer1 = new Spacer();
        rootPnl.add(spacer1, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 2), new Dimension(-1, 2), new Dimension(-1, 2), 0, false));
        lblQuestion1 = new JLabel();
        lblQuestion1.setText("1) Содержимое вопроса");
        rootPnl.add(lblQuestion1, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 3, false));
        lblQuestion2 = new JLabel();
        lblQuestion2.setText("2) Содержимое вопроса");
        rootPnl.add(lblQuestion2, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 3, false));
        final Spacer spacer2 = new Spacer();
        rootPnl.add(spacer2, new GridConstraints(11, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 2), new Dimension(-1, 2), new Dimension(-1, 2), 0, false));
        pnlInfoHeadDepAndTeacher = new JPanel();
        pnlInfoHeadDepAndTeacher.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        rootPnl.add(pnlInfoHeadDepAndTeacher, new GridConstraints(12, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        pnlInfoHeadDepAndTeacher.add(spacer3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 2), new Dimension(-1, 2), new Dimension(-1, 2), 0, false));
        lblHeadDep = new JLabel();
        lblHeadDep.setText("Заведующий кафедры: Иванов Ю. Д.");
        pnlInfoHeadDepAndTeacher.add(lblHeadDep, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 2, false));
        lblTeacher = new JLabel();
        lblTeacher.setText("Экзаменатор: Петров А. И.");
        pnlInfoHeadDepAndTeacher.add(lblTeacher, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 2, false));
        pnlApprovalAndProtocol = new JPanel();
        pnlApprovalAndProtocol.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        rootPnl.add(pnlApprovalAndProtocol, new GridConstraints(13, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        lblApproval = new JLabel();
        lblApproval.setText("Утверждено на заседании кафедры ");
        pnlApprovalAndProtocol.add(lblApproval, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 2, false));
        final Spacer spacer4 = new Spacer();
        pnlApprovalAndProtocol.add(spacer4, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        lblData = new JLabel();
        lblData.setText("24.10.2022,");
        pnlApprovalAndProtocol.add(lblData, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblProtocol = new JLabel();
        lblProtocol.setText("Протокол № 2.5");
        pnlApprovalAndProtocol.add(lblProtocol, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblDiscipline = new JLabel();
        lblDiscipline.setText("Дисциплина«Программирование на Java»");
        rootPnl.add(lblDiscipline, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPnl;
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont1$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    @Override
    public Component getComponent() {
        return null;
    }

    @Override
    public void updateComponent() {
        Font font = new Font("Segoe UI", Font.BOLD, 18);
        lblTicketPattern.setFont(font);
        font = new Font("Segoe UI", Font.BOLD, 12);
        lblUniversity.setFont(font);
        lblFaculty.setFont(font);
        lblExam.setFont(font);
        MatteBorder border = (MatteBorder) rootPnl.getBorder();
        Insets insets = border.getBorderInsets();
        rootPnl.setBorder(BorderFactory.createMatteBorder(insets.top, insets.left, insets.bottom, insets.right,
                ColorThemeConfig.COLOR_BORDER_TICKET_PATTERN.getColor()));
    }

    public enum ColorThemeConfig implements ColorManager {
        COLOR_BORDER_TICKET_PATTERN(() -> AppThemeManager.getCurrentTheme() == ThemeApp.LIGHT ?
                new Color(211, 211, 211, 255) :
                new Color(103, 103, 103));

        final ColorManager manager;

        ColorThemeConfig(ColorManager colorManager) {
            manager = colorManager;
        }

        @Override
        public Color getColor() {
            return manager.getColor();
        }
    }
}
