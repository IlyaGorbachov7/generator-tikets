package panels.impl;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import entity.Ticket;
import entity.TicketGenerator;
import exceptions.NumberQuestionsRequireException;
import frames.FrameDialogFactory;
import frames.impl.AboutAuthor;
import frames.impl.AboutProgram;
import frames.impl.FileViewer;
import panels.BasePanel;
import panels.PanelType;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class represent main window panel
 *
 * @author Gorbachev I. D.
 * @version 18.04.2022
 */
public class MainWindowPanel extends BasePanel {
    private final JMenuBar menuBar;
    private final JMenuItem loadItem;
    private final JMenuItem saveItem;
    private final JMenuItem exitItem;
    private final JMenuItem aboutAuthorItem;
    private final JMenuItem aboutProgramItem;

    private final JFileChooser chooserUpLoad;
    private final JFileChooser chooserSave;

    private final Window frameRoot;
    private final AboutAuthor aboutAuthorDialog;
    private final AboutProgram aboutProgramDialog;
    private final FileViewer viewFileDialog;

    {
        menuBar = new JMenuBar();
        loadItem = new JMenuItem("Загрузить",
                new ImageIcon(Objects.requireNonNull(
                        MainWindowPanel.class.getResource("/resources/openItemIcon1.png"))
                ));
        saveItem = new JMenuItem("Сохранить",
                new ImageIcon(Objects.requireNonNull(
                        MainWindowPanel.class.getResource("/resources/saveItemIcon1.png"))
                ));
        exitItem = new JMenuItem("Выйти",
                new ImageIcon(Objects.requireNonNull(
                        MainWindowPanel.class.getResource("/resources/exitIcon1.png"))
                ));
        aboutAuthorItem = new JMenuItem("О авторе",
                new ImageIcon(Objects.requireNonNull(
                        MainWindowPanel.class.getResource("/resources/aboutAuthorIcon1.png"))
                ));
        aboutProgramItem = new JMenuItem("О программе",
                new ImageIcon(Objects.requireNonNull(
                        MainWindowPanel.class.getResource("/resources/aboutProgramIcon1.png"))
                ));
        chooserUpLoad = new JFileChooser();
        chooserSave = new JFileChooser();

        //**********************

        lbInstitute = new JLabel("Учреждение образования");
        lbFaculty = new JLabel("Факультет");
        lbDepartment = new JLabel("Кафедра");
        lbSpecialization = new JLabel("Специальность");
        lbDiscipline = new JLabel("Дисциплина");
        lbTeacher = new JLabel("Преподаватель");
        lbHeadDepartment = new JLabel("Заведующий кафедрой");
        lbTypeSession = new JLabel("Вид сессии");
        lbDateDecision = new JLabel("Дата утверждения : ");
        lbProtocol = new JLabel("Протокол №");

        tfInstitute = new JTextField(30);
        tfFaculty = new JTextField(30);
        tfDepartment = new JTextField(30);
        tfSpecialization = new JTextField(30);
        tfDiscipline = new JTextField(30);
        tfTeacher = new JTextField(30);
        tfHeadDepartment = new JTextField(30);
        tfProtocol = new JTextField(5);
        boxTypeSession = new JComboBox<>(
                new Ticket.SessionType[]{Ticket.SessionType.WINTER, Ticket.SessionType.SUMMER
                });
        datePicDecision = new DatePicker();
        //**********************

        modelListFilesRsc = new DefaultListModel<>();
        btnAdd = new JButton("Загрузить файл вопросов", new ImageIcon(
                Objects.requireNonNull(
                        MainWindowPanel.class.getResource("/resources/openItemIcon1.png"))
        ));
        btnRemove = new JButton("Исключить файл", new ImageIcon(
                Objects.requireNonNull(MainWindowPanel.class.getResource("/resources/removeItemIcon.png"))
        ));
        btnGenerate = new JButton("Сгенерировать билеты", new ImageIcon(
                Objects.requireNonNull(MainWindowPanel.class.getResource("/resources/generateIcon.png"))
        ));
        btnViewFile = new JButton("Предварительный просмотр", new ImageIcon(
                Objects.requireNonNull(MainWindowPanel.class.getResource("/resources/previewIcon.png"))
        ));
        btnSave = new JButton("Сохранить билеты", new ImageIcon(
                Objects.requireNonNull(MainWindowPanel.class.getResource("/resources/saveItemIcon1.png"))
        ));
        lblCountItems = new JLabel("Загружено файлов : ");
        lblListSize = new JLabel("" + "0");
        lbQuantityTickets = new JLabel("Количество билетов");
        tfQuantityTickets = new JTextField("10", 2);
        lbQuantityQuestionTickets = new JLabel("Количество вопросов в билете");
        tfQuantityQuestionTickets = new JTextField("3", 2);
    }


    /**
     * The constructor creates a panel
     *
     * @param frame frame contains created this  panel
     */
    public MainWindowPanel(Window frame) {
        super(frame);
        frameRoot = getFrame();
        aboutAuthorDialog = (AboutAuthor) FrameDialogFactory.getInstance()
                .createJDialog(frame, PanelType.ABOUT_AUTHOR);
        aboutProgramDialog = (AboutProgram) FrameDialogFactory.getInstance()
                .createJDialog(frame, PanelType.ABOUT_PROGRAM);
        viewFileDialog = (FileViewer) FrameDialogFactory.getInstance()
                .createJDialog(frame, PanelType.FILE_VIEWER);

        // initialization menu bar
        JMenu fileMenu = new JMenu("Файл");
        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu infoMenu = new JMenu("Информация");
        infoMenu.add(aboutProgramItem);
        infoMenu.add(aboutAuthorItem);

        menuBar.add(fileMenu);
        menuBar.add(infoMenu);
        if (frame instanceof JFrame window) {
            window.setJMenuBar(menuBar);
        } else if (frame instanceof JDialog window) {
            window.setJMenuBar(menuBar);
        }

        this.initPanel();

        this.setComponentsListeners();
        this.setVisible(true);
    }

    private JPanel pnlData;
    private JPanel pnlGenerate;

    /**
     * The method initialize view main panel
     */
    @Override
    public void initPanel() {
        this.setLayout(new GridLayout(1, 2, 10, 10));

        // разбил на потоки, чтобы было быстро инициализировалось
        Thread thread1 = new Thread(() -> {
            pnlData = this.createDataInputPanel();
            System.out.println("000");
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            pnlGenerate = this.createPanelMainActionPanel();
            System.out.println("0000");
        });
        thread2.start();


        System.out.println("0");
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException ignored) {
        }
        this.setConfigComponents();
        this.add(pnlData);
        this.add(pnlGenerate);
    }

    /**
     * The method sets configuration all components panels.
     * This method invoked once panel generation.
     */
    @Override
    public void setConfigComponents() {
        // init MenuBar
        saveItem.setEnabled(false);

        // createDataInputPanel
        tfInstitute.setFont(new Font("Serif", Font.BOLD, 17));
        tfFaculty.setFont(new Font("Serif", Font.PLAIN, 17));
        tfDepartment.setFont(new Font("Serif", Font.PLAIN, 17));
        tfSpecialization.setFont(new Font("Serif", Font.PLAIN, 17));
        tfDiscipline.setFont(new Font("Serif", Font.PLAIN, 17));
        tfTeacher.setFont(new Font("Serif", Font.PLAIN, 17));
        tfHeadDepartment.setFont(new Font("Serif", Font.PLAIN, 17));
        tfProtocol.setFont(new Font("Serif", Font.PLAIN, 17));

        tfTeacher.setToolTipText("Фамилия Имя Отчество (Фамилия И. О.)");
        tfHeadDepartment.setToolTipText("Фамилия Имя Отчество (Фамилия И. О.)");

        DatePickerSettings datePickerSettings = new DatePickerSettings(new Locale("ru"));
        datePickerSettings.setFormatForDatesCommonEra("dd.MM.uuuu");
        datePicDecision.setSettings(datePickerSettings);
        datePicDecision.getComponentDateTextField().setFont(
                new Font("Serif", Font.PLAIN, 17));
        datePicDecision.getComponentDateTextField().setEnabled(false);
        datePicDecision.setDateToToday();
        datePicDecision.setFocusable(false);
        boxTypeSession.setFocusable(false);

        // createPanelMainActionPanel
        btnAdd.setFocusable(false);

        btnRemove.setFocusable(false);
        btnRemove.setEnabled(false);
        btnSave.setFocusable(false);
        btnSave.setEnabled(false);

        btnViewFile.setFocusable(false);
        btnViewFile.setEnabled(false);

        btnGenerate.setFocusable(false);
        btnGenerate.setEnabled(false);

        chooserUpLoad.setMultiSelectionEnabled(true);
        chooserUpLoad.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".docx");
            }

            @Override
            public String getDescription() {
                return ".docx";
            }
        });

        chooserSave.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".docx");
            }

            @Override
            public String getDescription() {
                return ".docx";
            }
        });
    }

    /**
     * The method sets necessary all components listeners
     */
    public void setComponentsListeners() {
        ActionListener handler = new ActionEventHandler();
        loadItem.addActionListener(handler);
        saveItem.addActionListener(handler);
        exitItem.addActionListener(handler);
        aboutAuthorItem.addActionListener(handler);
        aboutProgramItem.addActionListener(handler);

        btnAdd.addActionListener(handler);
        btnRemove.addActionListener(handler);
        btnGenerate.addActionListener(handler);
        btnViewFile.addActionListener(handler);
        btnSave.addActionListener(handler);

        FocusAdapter tfFocusListener = new FocusEventHandler();
        tfInstitute.addFocusListener(tfFocusListener);
        tfFaculty.addFocusListener(tfFocusListener);
        tfDepartment.addFocusListener(tfFocusListener);
        tfSpecialization.addFocusListener(tfFocusListener);
        tfDiscipline.addFocusListener(tfFocusListener);
        tfTeacher.addFocusListener(tfFocusListener);
        tfHeadDepartment.addFocusListener(tfFocusListener);
        tfProtocol.addFocusListener(tfFocusListener);
        tfQuantityTickets.addFocusListener(tfFocusListener);
        tfQuantityQuestionTickets.addFocusListener(tfFocusListener);

        modelListFilesRsc.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                btnGenerate.setEnabled(true);
                saveItem.setEnabled(false);
                btnSave.setEnabled(false);
                btnViewFile.setEnabled(false);
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                if (modelListFilesRsc.isEmpty()) {
                    btnGenerate.setEnabled(false);
                }
                saveItem.setEnabled(false);
                btnSave.setEnabled(false);
                btnViewFile.setEnabled(false);
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
            }
        });

        jList.addListSelectionListener(e -> {
            if (!jList.getSelectedValuesList().isEmpty()) {
                btnRemove.setEnabled(true);
                jList.setFocusable(true); // возвращаем фокус
                jList.grabFocus();// ура ! Я нашел как вернуть захватить фокус
            } else {
                btnRemove.setEnabled(false);
                jList.setFocusable(false);
            }
        });

        frameRoot.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                jList.clearSelection();
            }
        });

        frameRoot.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (executionThread != null) {
                    executionThread.interrupt();
                }
            }

        });

    }

    /**
     * This method return message obout valid data.
     * If message is empty then data is valid, else contains note to valid data
     *
     * @return if true then data is valid, else contains note to valid data
     */
    private boolean checkValidData() {
        boolean bolInst = TextPatterns.COMMON_PATTERN.matches(tfInstitute.getText());
        boolean bolFac = TextPatterns.COMMON_PATTERN.matches(tfInstitute.getText());
        boolean bolDep = TextPatterns.COMMON_PATTERN.matches(tfDepartment.getText());
        boolean bolSpec = TextPatterns.COMMON_PATTERN.matches(tfSpecialization.getText());
        boolean bolDist = TextPatterns.COMMON_PATTERN.matches(tfDiscipline.getText());
        if (!bolInst || !bolFac || !bolDep || !bolSpec || !bolDist) return false;

        boolean bolTeach = TextPatterns.PERSON_NAME_PATTERN.matches(tfTeacher.getText());
        boolean bolHeadDest = TextPatterns.PERSON_NAME_PATTERN.matches(tfHeadDepartment.getText());
        if (!bolTeach || !bolHeadDest) return false;

        boolean bolProtocol = TextPatterns.PROTOCOL_PATTERN.matches(tfProtocol.getText());
        if (!bolProtocol) return false;

        boolean bolQuaTick = TextPatterns.NUMBER_PATTERN.matches(tfQuantityTickets.getText());
        boolean bolQuaQuestTick = TextPatterns.NUMBER_PATTERN.matches(tfQuantityQuestionTickets.getText());

        return bolQuaTick && bolQuaQuestTick;
    }

    private final JLabel lbInstitute;
    private final JLabel lbFaculty;
    private final JLabel lbDepartment;
    private final JLabel lbSpecialization;
    private final JLabel lbDiscipline;
    private final JLabel lbTeacher;
    private final JLabel lbHeadDepartment;
    private final JLabel lbTypeSession;
    private final JLabel lbDateDecision;
    private final JLabel lbProtocol;

    private final JTextField tfInstitute;
    private final JTextField tfFaculty;
    private final JTextField tfDepartment;
    private final JTextField tfSpecialization;
    private final JTextField tfDiscipline;
    private final JTextField tfTeacher;
    private final JTextField tfHeadDepartment;
    private final JTextField tfProtocol;
    private final JComboBox<Ticket.SessionType> boxTypeSession;
    private final DatePicker datePicDecision;

    /**
     * This method created data input panel
     *
     * @return created panel
     */
    private JPanel createDataInputPanel() {
        new Thread(() -> {

        });
        JPanel panelLEFT = new JPanel(new GridBagLayout());
        panelLEFT.setBorder(new TitledBorder("Заполните область ввода данных"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(5, 5, 5, 5);
        panelLEFT.add(lbInstitute, gbc);

        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 1;
        gbc1.fill = GridBagConstraints.BOTH;
        gbc1.weightx = 1;
        gbc1.weighty = 0.5;
        gbc1.insets = new Insets(5, 5, 5, 5);
        panelLEFT.add(tfInstitute, gbc1);

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.gridy = 1;
        gbc2.fill = GridBagConstraints.BOTH;
        gbc2.weightx = 0;
        gbc2.weighty = 0.5;
        gbc2.insets = new Insets(5, 5, 5, 5);
        panelLEFT.add(lbFaculty, gbc2);

        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.gridx = 1;
        gbc3.gridy = 1;
        gbc3.fill = GridBagConstraints.BOTH;
        gbc3.weightx = 1;
        gbc3.weighty = 0.5;
        gbc3.insets = new Insets(5, 5, 5, 5);
        panelLEFT.add(tfFaculty, gbc3);

        GridBagConstraints gbc4 = new GridBagConstraints();
        gbc4.gridx = 0;
        gbc4.gridy = 2;
        gbc4.fill = GridBagConstraints.BOTH;
        gbc4.weightx = 0;
        gbc4.weighty = 0.5;
        gbc4.insets = new Insets(5, 5, 5, 5);
        panelLEFT.add(lbDepartment, gbc4);

        GridBagConstraints gbc5 = new GridBagConstraints();
        gbc5.gridx = 1;
        gbc5.gridy = 2;
        gbc5.fill = GridBagConstraints.BOTH;
        gbc5.weightx = 1;
        gbc5.weighty = 0.5;
        gbc5.insets = new Insets(5, 5, 5, 5);
        panelLEFT.add(tfDepartment, gbc5);

        GridBagConstraints gbc6 = new GridBagConstraints();
        gbc6.gridx = 0;
        gbc6.gridy = 3;
        gbc6.fill = GridBagConstraints.BOTH;
        gbc6.weightx = 0;
        gbc6.weighty = 0.5;
        gbc6.insets = new Insets(5, 5, 5, 5);
        panelLEFT.add(lbSpecialization, gbc6);

        GridBagConstraints gbc7 = new GridBagConstraints();
        gbc7.gridx = 1;
        gbc7.gridy = 3;
        gbc7.fill = GridBagConstraints.BOTH;
        gbc7.weightx = 1;
        gbc7.weighty = 0.5;
        gbc7.insets = new Insets(5, 5, 5, 5);
        panelLEFT.add(tfSpecialization, gbc7);

        GridBagConstraints gbc8 = new GridBagConstraints();
        gbc8.gridx = 0;
        gbc8.gridy = 4;
        gbc8.fill = GridBagConstraints.BOTH;
        gbc8.weightx = 0;
        gbc8.weighty = 0.5;
        gbc8.insets = new Insets(5, 5, 5, 5);
        panelLEFT.add(lbDiscipline, gbc8);

        GridBagConstraints gbc9 = new GridBagConstraints();
        gbc9.gridx = 1;
        gbc9.gridy = 4;
        gbc9.fill = GridBagConstraints.BOTH;
        gbc9.weightx = 1;
        gbc9.weighty = 0.5;
        gbc9.insets = new Insets(5, 5, 5, 5);
        panelLEFT.add(tfDiscipline, gbc9);
        GridBagConstraints gbc10 = new GridBagConstraints();
        gbc10.gridx = 0;
        gbc10.gridy = 5;
        gbc10.fill = GridBagConstraints.BOTH;
        gbc10.weightx = 0;
        gbc10.weighty = 0.5;
        gbc10.insets = new Insets(5, 5, 5, 5);
        panelLEFT.add(lbTeacher, gbc10);

        GridBagConstraints gbc11 = new GridBagConstraints();
        gbc11.gridx = 1;
        gbc11.gridy = 5;
        gbc11.fill = GridBagConstraints.BOTH;
        gbc11.anchor = GridBagConstraints.WEST;
        gbc11.weighty = 1;
        gbc11.weightx = 0.5;
        gbc11.insets = new Insets(5, 5, 5, 5);
        panelLEFT.add(tfTeacher, gbc11);

        GridBagConstraints gbc12 = new GridBagConstraints();
        gbc12.gridx = 0;
        gbc12.gridy = 6;
        gbc12.fill = GridBagConstraints.BOTH;
        gbc12.anchor = GridBagConstraints.WEST;
        gbc12.weightx = 0;
        gbc12.weighty = 0.5;
        gbc12.insets = new Insets(5, 5, 5, 5);
        panelLEFT.add(lbHeadDepartment, gbc12);

        GridBagConstraints gbc13 = new GridBagConstraints();
        gbc13.gridx = 1;
        gbc13.gridy = 6;
        gbc13.fill = GridBagConstraints.BOTH;
        gbc13.anchor = GridBagConstraints.WEST;
        gbc13.weightx = 1;
        gbc13.weighty = 0.5;
        gbc13.insets = new Insets(5, 5, 5, 5);
        panelLEFT.add(tfHeadDepartment, gbc13);


        GridBagConstraints gbc14 = new GridBagConstraints();
        gbc14.gridx = 0;
        gbc14.gridy = 7;
        gbc14.fill = GridBagConstraints.BOTH;
        gbc14.anchor = GridBagConstraints.WEST;
        gbc14.weightx = 0;
        gbc14.weighty = 0.5;
        gbc14.insets = new Insets(5, 5, 5, 5);
        panelLEFT.add(lbTypeSession, gbc14);

        GridBagConstraints gbc15 = new GridBagConstraints();
        gbc15.gridx = 1;
        gbc15.gridy = 7;
        gbc15.fill = GridBagConstraints.VERTICAL;
        gbc15.anchor = GridBagConstraints.WEST;
        gbc15.weightx = 1;
        gbc15.weighty = 0.5;
        gbc15.insets = new Insets(5, 5, 5, 5);
        panelLEFT.add(boxTypeSession, gbc15);

        GridBagConstraints gbc16 = new GridBagConstraints();
        gbc16.gridx = 0;
        gbc16.gridy = 8;
        gbc16.fill = GridBagConstraints.BOTH;
        gbc16.anchor = GridBagConstraints.WEST;
        gbc16.weightx = 0;
        gbc16.weighty = 0.5;
        gbc16.insets = new Insets(5, 5, 5, 5);
        panelLEFT.add(lbDateDecision, gbc16);

        GridBagConstraints gbc17 = new GridBagConstraints();
        gbc17.gridx = 1;
        gbc17.gridy = 8;
        gbc17.fill = GridBagConstraints.VERTICAL;
        gbc17.anchor = GridBagConstraints.WEST;
        gbc17.weightx = 0;
        gbc17.weighty = 0.5;
        gbc17.insets = new Insets(5, 5, 5, 5);
        panelLEFT.add(datePicDecision, gbc17);

        GridBagConstraints gbc18 = new GridBagConstraints();
        gbc18.gridx = 0;
        gbc18.gridy = 9;
        gbc18.fill = GridBagConstraints.VERTICAL;
        gbc18.anchor = GridBagConstraints.WEST;
        gbc18.weightx = 0;
        gbc18.weighty = 0.5;
        gbc18.insets = new Insets(5, 5, 5, 5);
        panelLEFT.add(lbProtocol, gbc18);

        GridBagConstraints gbc19 = new GridBagConstraints();
        gbc19.gridx = 1;
        gbc19.gridy = 9;
        gbc19.fill = GridBagConstraints.HORIZONTAL;
        gbc19.anchor = GridBagConstraints.WEST;
        gbc19.weightx = 0;
        gbc19.weighty = 0.5;
        gbc19.insets = new Insets(5, 5, 5, 5);
        panelLEFT.add(tfProtocol, gbc19);

        // added empty space
        gbc.weightx = 0;
        gbc.weighty = 4; // very necessarily
        gbc.gridx = 0;
        gbc.gridy = ++gbc19.gridy;
        JPanel pTmp = new JPanel();
        panelLEFT.add(pTmp, gbc);
        return panelLEFT;
    }

    private final DefaultListModel<File> modelListFilesRsc;
    private final JList<File> jList = new JList<>(modelListFilesRsc);
    private final JButton btnAdd;
    private final JButton btnRemove;
    private final JButton btnGenerate;
    private final JButton btnViewFile;
    private final JButton btnSave;
    private final JLabel lblCountItems;
    private final JLabel lblListSize;
    private final JLabel lbQuantityTickets;
    private final JTextField tfQuantityTickets;
    private final JLabel lbQuantityQuestionTickets;
    private final JTextField tfQuantityQuestionTickets;

    /**
     * The method creates a panel containing a list of downloaded files
     * and also needed buttons for manage
     *
     * @return created panel
     */
    private JPanel createPanelMainActionPanel() {
        JPanel pnlRes = new JPanel();
        pnlRes.setLayout(new BoxLayout(pnlRes, BoxLayout.Y_AXIS));
        pnlRes.setBorder(new TitledBorder("Область ресурсов"));

        jList.setVisibleRowCount(10);
        jList.setSelectionBackground(Color.gray); // by default
        jList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scrollPane = new JScrollPane(jList);

        JPanel pLabel = new JPanel();
        pLabel.add(lblCountItems);
        pLabel.add(lblListSize);
        pnlRes.add(pLabel);

        pnlRes.add(scrollPane);

        JPanel pBtn = new JPanel(new GridLayout(1, 2, 5, 0));
        pBtn.add(btnAdd);
        pBtn.add(btnRemove);
        pnlRes.add(pBtn);


        JPanel pnlBtnGenerate = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        pnlBtnGenerate.add(lbQuantityTickets, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        pnlBtnGenerate.add(tfQuantityTickets, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        pnlBtnGenerate.add(lbQuantityQuestionTickets, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        pnlBtnGenerate.add(tfQuantityQuestionTickets, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.ipady = 25;
        gbc.fill = GridBagConstraints.BOTH;
        pnlBtnGenerate.add(btnGenerate, gbc);

        gbc.gridy = 3;
        pnlBtnGenerate.add(btnViewFile, gbc);
        gbc.gridy = 4;
        pnlBtnGenerate.add(btnSave, gbc);

        pnlRes.add(pnlBtnGenerate);

        return pnlRes;
    }

    private TicketGenerator ticketGenerator;
    private TicketsGenerationExecutionThread executionThread;

    /**
     * The class is a descendant of the ticket generation execution
     *
     * @author Gorbachev I. D.
     * @version 19.04.2022
     */
    private final class TicketsGenerationExecutionThread extends Thread {

        /**
         * Flow execution  method
         */
        @Override
        public void run() {
            this.setEnabledComponents(false, false);
            String val = tfQuantityTickets.getText();
            int quantityTickets = Integer.parseInt(val);
            val = tfQuantityQuestionTickets.getText();
            int quantityQuestionInTicket = Integer.parseInt(val);

            File[] filesRes = this.toArrayFiles(modelListFilesRsc.toArray());
            Ticket tempTicket = new Ticket(
                    tfInstitute.getText(),
                    tfFaculty.getText(),
                    tfDepartment.getText(), tfSpecialization.getText(),
                    tfDiscipline.getText(), tfTeacher.getText(),
                    tfHeadDepartment.getText(),
                    (Ticket.SessionType) boxTypeSession.getSelectedItem(),
                    datePicDecision.getText(), tfProtocol.getText());

            ticketGenerator = new TicketGenerator(filesRes, tempTicket);

            try {
                ticketGenerator.startGenerate(quantityTickets, quantityQuestionInTicket, true);
            } catch (NumberQuestionsRequireException e) {
                int selected = JOptionPane.showInternalConfirmDialog(null,
                        e.getMessage() +
                        "\n\nDo you want to do a repeat of the questions ?",
                        "Message", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (selected == JOptionPane.OK_OPTION) {
                    try {
                        ticketGenerator.startGenerate(quantityTickets, quantityQuestionInTicket, false);
                    } catch (NumberQuestionsRequireException | ExecutionException e1) {
                        JOptionPane.showInternalMessageDialog(null,
                                e1.getMessage(),
                                "Warning", JOptionPane.ERROR_MESSAGE);

                        this.setEnabledComponents(true, false);
                    } catch (InterruptedException e2) {
                        System.out.println(Thread.currentThread() + " is interrupted: Reason " +
                                           ": interrupted generate tickets by close program during ticket generation");
                    }

                } else {
                    this.setEnabledComponents(true, false);
                }

            } catch (ExecutionException | IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(),
                        "Warning !", JOptionPane.ERROR_MESSAGE);

                this.setEnabledComponents(true, false);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread() + " is interrupted: Reason : interrupted generate tickets by" +
                                   "close program during ticket generation");
            }

            // if docx file is generated, then ...
            // save generate file docx inside project for further conversion to pdf file
            if (ticketGenerator.getDocxDec() != null) {
                InputStream inputStream = null;
                OutputStream outputStream = null;
                IConverter converter = null;
                try {
                    try {
                        File tmpFileDocx = File.createTempFile("tmpDocx", ".docx");
                        tmpFileDocx.deleteOnExit();
                        File tmpFilePdf = File.createTempFile("tmpPdf", ".pdf");
                        tmpFilePdf.deleteOnExit();
                        viewFileDialog.setFile(tmpFilePdf);

                        ticketGenerator.writeOutputFile(tmpFileDocx);

                        // convert saved file to pdf file
                        inputStream = new FileInputStream(tmpFileDocx);
                        outputStream = new FileOutputStream(tmpFilePdf);
                        converter = LocalConverter.builder().build();
                        converter.convert(inputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
                        System.out.println("convert docx => pdf is : success");
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }
                        if (converter != null) {
                            converter.shutDown();
                        }
                    }
                } catch (Exception e) {
                    // ConverterException in case if program has been close
                    // for a reason: InterruptedException inside method .execute();
                    System.out.println("complete");
                    return;
                }
                // after generating and conversion enable all components
                this.setEnabledComponents(true, true);
                JOptionPane.showInternalMessageDialog(
                        null, "Генерация прошла успешно !", "",
                        JOptionPane.INFORMATION_MESSAGE, new ImageIcon(
                                Objects.requireNonNull(
                                        MainWindowPanel.class.getResource("/resources/successIcon.png"))
                        ));
            }
        }

        /**
         * Convert array objects class {@link Object} to array objects class {@link File}
         *
         * @param objects array objects class {@link Object}
         * @return array objects class {@link File}
         */
        private File[] toArrayFiles(Object[] objects) {
            File[] files = new File[objects.length];
            int i = 0;
            for (Object obj : objects) {
                files[i++] = (File) obj;
            }
            return files;
        }


        /**
         * The method is helper.
         * This method was created for convenient set the on/off mode components.
         * <p>
         * The method used in {@link TicketsGenerationExecutionThread#run()}
         *
         * @param b  is boolean value, sets enabled {@link #btnGenerate},
         *           {@link #btnAdd}, {@link #loadItem}, {@link #jList}
         * @param b1 is boolean value, sets enabled {@link #btnSave},
         *           {@link #saveItem}, {@link #btnViewFile}
         */
        private void setEnabledComponents(boolean b, boolean b1) {
            btnGenerate.setEnabled(b);
            btnAdd.setEnabled(b);
            loadItem.setEnabled(b);

            jList.clearSelection();
            jList.setEnabled(b);


            btnSave.setEnabled(b1);
            btnViewFile.setEnabled(b1);
            saveItem.setEnabled(b1);
        }
    }

    /**
     * The class handles events  {@link FocusEvent} received from components
     *
     * @author Gorbachev I. D.
     * @version 18.04.2022
     */
    private final class FocusEventHandler extends FocusAdapter {

        /**
         * Handler focus event
         *
         * @param e class object FocusEvent
         */
        @Override
        public void focusLost(FocusEvent e) {
            if (e.getSource() instanceof JTextField textField) {
                String msg = "";
                if ((textField == tfTeacher || textField == tfHeadDepartment) &&
                    !TextPatterns.PERSON_NAME_PATTERN.matches(textField.getText())) {
                    msg = "Пример:\n" +
                          " Фамилия Имя Отчество (или Фамилия И. О.)";
                } else if (textField == tfProtocol &&
                           !TextPatterns.PROTOCOL_PATTERN.matches(textField.getText())) {
                    msg = "Пример:\n" +
                          " 4.1 (или 1.23.1)";
                } else if ((textField == tfQuantityTickets || textField == tfQuantityQuestionTickets) &&
                           !TextPatterns.NUMBER_PATTERN.matches(textField.getText())) {
                    msg = "Допустимы символы:\n" +
                          " 0-9";
                } else if (textField != tfTeacher && textField != tfHeadDepartment &&
                           textField != tfProtocol &&
                           textField != tfQuantityQuestionTickets && textField != tfQuantityTickets &&
                           !TextPatterns.COMMON_PATTERN.matches(textField.getText())) {
                    msg = "Допустимы символы:\n" +
                          " A-Я,А-Z, а-я, a-z, 0-9, - , «, », \"";
                } else { // кидаем ошибку
                    textField.setForeground(Color.BLACK);
                }

                if (!msg.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Неверный ввод !\n" +
                                                        msg);
                    textField.setForeground(Color.RED);
                    textField.grabFocus();
                }
            }
        }
    }

    /**
     * The class handles events  {@link ActionEvent} received from components
     *
     * @author Gorbachev I. D.
     * @version 18.04.2022
     */
    private final class ActionEventHandler implements ActionListener {

        /**
         * Handler action event
         *
         * @param e class object ActionEvent
         */
        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == loadItem || e.getSource() == btnAdd) {
                int res = chooserUpLoad.showDialog(frameRoot, "Загрузить");
                if (res == JFileChooser.APPROVE_OPTION) {
                    File[] files = chooserUpLoad.getSelectedFiles();
                    for (var file : files) {
                        if (!modelListFilesRsc.contains(file)) {
                            modelListFilesRsc.addElement(file);
                        }
                    }
                    lblListSize.setText("" + modelListFilesRsc.size());
                }

            } else if (e.getSource() == saveItem || e.getSource() == btnSave) {
                if (ticketGenerator != null) {
                    new Thread(() -> {
                        int res = chooserSave.showSaveDialog(frameRoot);
                        if (res == JFileChooser.APPROVE_OPTION) {
                            File saveFile = chooserSave.getSelectedFile();
                            if (!saveFile.getName().toLowerCase().endsWith(".docx")) {
                                saveFile = new File(saveFile.getParentFile(),
                                        saveFile.getName() + ".docx");
                            }
                            try {
                                ticketGenerator.writeOutputFile(saveFile);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }).start();
                }

            } else if (e.getSource() == exitItem) {
                // вызываем события закрытия окна, чтобы вызвался слушатель и он закроет все потоки безопасно
                frameRoot.dispatchEvent(new WindowEvent(frameRoot,
                        WindowEvent.WINDOW_CLOSING));

            } else if (e.getSource() == aboutAuthorItem) {
                aboutAuthorDialog.setVisible(true);

            } else if (e.getSource() == aboutProgramItem) {
                aboutProgramDialog.setVisible(true);

            } else if (e.getSource() == btnRemove) {
                File[] selectedElements = jList.getSelectedValuesList().toArray(new File[0]);
                if (selectedElements.length > 0) {
                    for (File element : selectedElements) {
                        modelListFilesRsc.removeElement(element);
                    }
                    lblListSize.setText("" + modelListFilesRsc.size());
                }

            } else if (e.getSource() == btnGenerate) {
                if (checkValidData()) {
                    executionThread = new TicketsGenerationExecutionThread();
                    executionThread.start();
                } else {
                    /*knowingly grab focus of inactive text filed,
                    with the goal generate throw exception*/
                    datePicDecision.getComponentDateTextField().grabFocus();
                }
            } else if (e.getSource() == btnViewFile) {
                new Thread(() -> {
                    try {
                        viewFileDialog.openDocument();
                    } catch (Exception ex) {
                        JOptionPane.showConfirmDialog(frameRoot,
                                "No such file");
                    }
                    viewFileDialog.setVisible(true);
                }).start();
            }
        }
    }

    /**
     * This enum contains constants define text patterns
     * for correctly input data
     *
     * @author Gorbachev I. D.
     * @version 06.05.2022
     */
    private enum TextPatterns {
        COMMON_PATTERN("[\\wА-Яа-я\\-\"«»\\sЁё&&[^_]]*"),
        PERSON_NAME_PATTERN("(^([А-ЯЁ][а-яё]+)\\s+(([А-ЯЁ][а-яё]+)|" +
                            "([А-ЯЁ]\\.?))\\s+(([А-ЯЁ][а-яё]+)|([А-ЯЁ]\\.?))\\s*)?"),
        PROTOCOL_PATTERN("((^[0-9]+)((\\.)([0-9]+))*)?\\s*"),
        NUMBER_PATTERN("[0-9]+");

        private final Pattern pattern;

        /**
         * This private constructor with parameters
         *
         * @param regex regex expression
         */
        TextPatterns(String regex) {
            this.pattern = Pattern.compile(regex);
        }

        /**
         * This method return template
         *
         * @return is class object {@link Pattern} represent template string
         */
        public Pattern getPattern() {
            return pattern;
        }

        /**
         * This method return class object {@link Matcher}
         *
         * @param input matchers string
         * @return is class object {@link Matcher} represent result matches
         */
        public Matcher getMatcher(String input) {
            return pattern.matcher(input);
        }

        /**
         * This method matches input string with pattern
         *
         * @param input match string
         * @return true if input string match pattern else false
         */
        public boolean matches(String input) {
            return getMatcher(input).matches();
        }

        /**
         * Object represent string
         *
         * @return string
         */
        @Override
        public String toString() {
            return pattern.pattern();
        }
    }
}