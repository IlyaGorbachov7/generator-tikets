package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.TicketGeneratorUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.AbstractTicketGenerator;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.exceptions.FindsChapterWithoutSection;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.exceptions.FindsNonMatchingLevel;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.exceptions.GenerationConditionException;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.exceptions.NumberQuestionsRequireException;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.GenerationPropertyImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.TicketGeneratorImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.sender.MessageRetriever;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.sender.SenderMessage;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.sender.SenderMsgFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.sender.SenderStopSleepException;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.threads.tools.constants.TextPatterns;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.poolcon.ConnectionPoolException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.poolcon.PoolConnection;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.*;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.displn.DisciplineDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.headdep.HeadDepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl.SpecializationDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.tchr.TeacherDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityDTO;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.factory.impl.ServiceFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.Localizer;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.LocalizerListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.serializer.SerializeManager;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.AppThemeManager;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.ThemeApp;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.ThemeChangerListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.PanelFunc;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.combobox.MyJCompoBox;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.combobox.abservers.RelatedComponentEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.BaseDialog;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.FrameDialogFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.FrameType;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.impl.*;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.BasePanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.PanelType;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.FileNames;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.GenerationMode;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.InputSearchFieldsData;
import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ValueRange;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.impl.LaunchFrame.toolkit;

/**
 * The class represent main window panel
 *
 * @author Gorbachev I. D.
 * @version 18.04.2022
 */
@Log4j2
public class MainWindowPanel extends BasePanel implements ThemeChangerListener, LocalizerListener {
    private final JMenuBar menuBar;
    private final JMenuItem loadItem;
    private final JMenuItem saveItem;
    private final JMenuItem exitItem;
    private final JMenuItem aboutAuthorItem;
    private final JMenuItem aboutProgramItem;
    private final JMenuItem recordSettingItem;
    private final JMenuItem databaseSettingItem;
    private final JMenuItem tglAppTheme;

    private final JFileChooser chooserUpLoad;
    private final JFileChooser chooserSave;

    private final Window frameRoot;
    private AboutAuthor aboutAuthorDialog;
    private AboutProgram aboutProgramDialog;
    private FileViewer viewFileDialog;
    private RecordSetting recordSettingDialog;
    private InputParametersDialog dataBaseDialog;

    private final UniversityService universityService = ServiceFactoryImpl.getInstance().universityService();
    private final FacultyService facultyService = ServiceFactoryImpl.getInstance().facultyService();
    private final DepartmentService departmentService = ServiceFactoryImpl.getInstance().departmentService();
    private final SpecializationService specializationService = ServiceFactoryImpl.getInstance().specializationService();
    private final DisciplineService disciplineService = ServiceFactoryImpl.getInstance().disciplineService();
    private final HeadDepartmentService headDepartmentService = ServiceFactoryImpl.getInstance().headDepartmentService();
    private final TeacherService teacherService = ServiceFactoryImpl.getInstance().teacherService();

    private final InputSearchFieldsData inputSearchFieldsData;

    private final UUID NO_FUND_ID = UUID.randomUUID();

    // TODO: add toggle dark or lite mode window
    {
        menuBar = new JMenuBar();
        loadItem = new JMenuItem(Localizer.get("panel.load"),
                new ImageIcon(Objects.requireNonNull(FileNames.getResource(FileNames.openItemIcon))
                ));
        saveItem = new JMenuItem(Localizer.get("panel.save"),
                new ImageIcon(Objects.requireNonNull(FileNames.getResource(FileNames.saveItemIcon))
                ));
        exitItem = new JMenuItem(Localizer.get("panel.leave"),
                new ImageIcon(Objects.requireNonNull(FileNames.getResource(FileNames.exitItemIcon))
                ));
        aboutAuthorItem = new JMenuItem(Localizer.get("panel.main.aboutAuthor"),
                new ImageIcon(Objects.requireNonNull(FileNames.getResource(FileNames.aboutAuthorItemIcon))
                ));
        aboutProgramItem = new JMenuItem(Localizer.get("panel.main.aboutProgram"),
                new ImageIcon(Objects.requireNonNull(FileNames.getResource(FileNames.aboutProgramItemIcon))
                ));
        recordSettingItem = new JMenuItem(Localizer.get("panel.main.recordTickets"),
                new ImageIcon(Objects.requireNonNull(FileNames.getResource(FileNames.recordSettingIcon))));
        databaseSettingItem = new JMenuItem(Localizer.get("panel.main.inputSetting"),
                new ImageIcon(Objects.requireNonNull(FileNames.getResource(FileNames.databaseSettingIcon))));
        tglAppTheme = new JMenuItem(Localizer.get("panel.main.themeApp"));

        chooserUpLoad = new JFileChooser();
        chooserUpLoad.setLocale(TicketGeneratorUtil.getLocalsConfiguration().getSelectedLocale());
        chooserSave = new JFileChooser();
        chooserSave.setLocale(TicketGeneratorUtil.getLocalsConfiguration().getSelectedLocale());
        //**********************

        lbInstitute = new JLabel(Localizer.get("panel.main.university"));
        lbFaculty = new JLabel(Localizer.get("panel.main.faculty"));
        lbDepartment = new JLabel(Localizer.get("panel.main.department"));
        lbSpecialization = new JLabel(Localizer.get("panel.main.specialization"));
        lbDiscipline = new JLabel(Localizer.get("panel.main.discipline"));
        lbTeacher = new JLabel(Localizer.get("panel.main.examiner"));
        lbHeadDepartment = new JLabel(Localizer.get("panel.main.departmentLeader"));
        lbTypeSession = new JLabel(Localizer.get("panel.main.sessionType"));
        lbDateDecision = new JLabel(Localizer.get("panel.main.dataApproval"));
        lbProtocol = new JLabel(Localizer.get("panel.main.protocol"));

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
        btnAdd = new JButton(Localizer.get("panel.main.file.load"), new ImageIcon(
                Objects.requireNonNull(FileNames.getResource(FileNames.openItemIcon))
        ));
        btnRemove = new JButton(Localizer.get("panel.main.file.exclude"), new ImageIcon(
                Objects.requireNonNull(FileNames.getResource(FileNames.removeItemIcon))
        ));
        jBoxModes = new JComboBox<>(new GenerationMode[]{
                GenerationMode.MODE_3, GenerationMode.MODE_1, GenerationMode.MODE_2});

        lbReadQuestRandom = new JLabel(Localizer.get("panel.main.file.question.read"));
        btnGroupReadWay = new ButtonGroup();
        rdiBtnSequence = new JRadioButton(Localizer.get("panel.main.file.question.sequence"), true);
        rdiBtnRandom = new JRadioButton(Localizer.get("panel.main.file.question.random"), false);

        lbWriteQuestRandom = new JLabel(Localizer.get("panel.main.file.question.write"));
        btnGroupWriteWay = new ButtonGroup();
        rdiBtnWriteSequence = new JRadioButton(Localizer.get("panel.main.file.question.sequence"), true);
        rdiBtnWriteRandom = new JRadioButton(Localizer.get("panel.main.file.question.random"), false);


        lbGenerationMode = new JLabel(Localizer.get("panel.main.generation.mode"));

        btnGenerate = new JButton(Localizer.get("panel.main.ticket.generation"), new ImageIcon(
                Objects.requireNonNull(FileNames.getResource(FileNames.generateIcon))
        ));
        btnViewFile = new JButton(Localizer.get("panel.main.ticket.preview"), new ImageIcon(
                Objects.requireNonNull(FileNames.getResource(FileNames.previewIcon))
        ));
        btnSave = new JButton(Localizer.get("panel.main.ticket.save"), new ImageIcon(
                Objects.requireNonNull(FileNames.getResource(FileNames.saveItemIcon))
        ));
        lblCountItems = new JLabel(Localizer.get("panel.main.file.load.quantity"));
        lblListSize = new JLabel("" + "0");
        lbQuantityTickets = new JLabel(Localizer.get("panel.main.ticket.quantity"));
        tfQuantityTickets = new JTextField("10", 2);
        lbQuantityQuestionTickets = new JLabel(Localizer.get("panel.main.ticket.quantity.question"));
        tfQuantityQuestionTickets = new JTextField("3", 2);

        spinnerQuantityTickets = new JSpinner();
        spinnerQuantityQuestionTickets = new JSpinner();

        loadingDialog = new LoadingDialog();

        inputSearchFieldsData = InputSearchFieldsData.builder().build();

        cbInstitute = MyJCompoBox.builder().mapperViewElem((obj) -> ((UniversityDTO) obj).getName())
                .supplierListElem((textField) -> ServiceFactoryImpl.getInstance().universityService()
                        .getByLikeName(textField))
                .build();

        cbFaculty = MyJCompoBox.builder().mapperViewElem(obj -> ((FacultyDto) obj).getName())
                .supplierListElem(textField -> facultyService.getByLikeNameAndUniversityId(textField, inputSearchFieldsData.getUniversityDto().getId()))
                .build();

        cbDepartment = MyJCompoBox.builder().mapperViewElem(obj -> ((DepartmentDto) obj).getName())
                .supplierListElem(text -> departmentService.getByLikeNameAndFacultyId(text, inputSearchFieldsData.getFacultyDto().getId()))
                .build();

        cbSpecialization = MyJCompoBox.builder().mapperViewElem(obj -> ((SpecializationDto) obj).getName())
                .supplierListElem((text) -> specializationService.getByLikeNameAndDepartmentId(text, inputSearchFieldsData.getDepartmentDto().getId()))
                .build();

        cbDiscipline = MyJCompoBox.builder().mapperViewElem(obj -> ((DisciplineDto) obj).getName())
                .supplierListElem(text -> disciplineService.getByLikeNameAndSpecializationId(text, inputSearchFieldsData.getSpecializationDto().getId()))
                .build();

        cbHeadDepartment = MyJCompoBox.builder().mapperViewElem(obj -> ((HeadDepartmentDto) obj).getName())
                .supplierListElem(text -> headDepartmentService.getByLikeNameAndDepartmentId(text, inputSearchFieldsData.getDepartmentDto().getId()))
                .build();

        cbTeacher = MyJCompoBox.builder().mapperViewElem(obj -> ((TeacherDto) obj).getName())
                .supplierListElem(text -> teacherService.getByLikeNameAndFacultyId(text, inputSearchFieldsData.getFacultyDto().getId()))
                .build();
    }

    @Override
    public void onUpdateLocale(Locale selectedLocale) {
        loadItem.setText(Localizer.get("panel.load"));
        saveItem.setText(Localizer.get("panel.save"));
        exitItem.setText(Localizer.get("panel.leave"));
        aboutAuthorItem.setText(Localizer.get("panel.main.aboutAuthor"));
        aboutProgramItem.setText(Localizer.get("panel.main.aboutProgram"));
        recordSettingItem.setText(Localizer.get("panel.main.recordTickets"));
        databaseSettingItem.setText(Localizer.get("panel.main.inputSetting"));
        tglAppTheme.setText(Localizer.get("panel.main.themeApp"));

        lbInstitute.setText(Localizer.get("panel.main.university"));
        lbFaculty.setText(Localizer.get("panel.main.faculty"));
        lbDepartment.setText(Localizer.get("panel.main.department"));
        lbSpecialization.setText(Localizer.get("panel.main.specialization"));
        lbDiscipline.setText(Localizer.get("panel.main.discipline"));
        lbTeacher.setText(Localizer.get("panel.main.examiner"));
        lbHeadDepartment.setText(Localizer.get("panel.main.departmentLeader"));
        lbTypeSession.setText(Localizer.get("panel.main.sessionType"));
        lbDateDecision.setText(Localizer.get("panel.main.dataApproval"));
        lbProtocol.setText(Localizer.get("panel.main.protocol"));

        boxTypeSession.updateUI();
        jBoxModes.updateUI();

        btnAdd.setText(Localizer.get("panel.main.file.load"));
        btnRemove.setText(Localizer.get("panel.main.file.exclude"));
        lbReadQuestRandom.setText(Localizer.get("panel.main.file.question.read"));
        rdiBtnSequence.setText(Localizer.get("panel.main.file.question.sequence"));
        rdiBtnRandom.setText(Localizer.get("panel.main.file.question.random"));
        lbWriteQuestRandom.setText(Localizer.get("panel.main.file.question.write"));
        rdiBtnWriteSequence.setText(Localizer.get("panel.main.file.question.sequence"));
        rdiBtnWriteRandom.setText(Localizer.get("panel.main.file.question.random"));

        lbGenerationMode.setText(Localizer.get("panel.main.generation.mode"));
        btnGenerate.setText(Localizer.get("panel.main.ticket.generation"));
        btnViewFile.setText(Localizer.get("panel.main.ticket.preview"));
        btnSave.setText(Localizer.get("panel.main.ticket.save"));
        lblCountItems.setText(Localizer.get("panel.main.file.load.quantity"));
        lbQuantityTickets.setText(Localizer.get("panel.main.ticket.quantity"));
        lbQuantityQuestionTickets.setText(Localizer.get("panel.main.ticket.quantity.question"));
        tfTeacher.setToolTipText(Localizer.get("panel.main.firstlastname"));
        tfHeadDepartment.setToolTipText(Localizer.get("panel.main.firstlastname"));



    }

    /**
     * The constructor creates a panel
     *
     * @param frame frame contains created this  panel
     */
    public MainWindowPanel(Window frame) {
        super(frame);
        frameRoot = getRootFrame();
        SwingUtilities.invokeLater(() -> {
            viewFileDialog = (FileViewer) FrameDialogFactory.getInstance()
                    .createJDialog(frame, FrameType.FILE_VIEWER, PanelType.FILE_VIEWER);
            recordSettingDialog = (Objects.isNull(recordSettingDialog)) ? (RecordSetting) FrameDialogFactory.getInstance()
                    .createJDialog(frame, FrameType.RECORD_SETTING, PanelType.RECORD_SETTING) : recordSettingDialog;
        });

        // initialization menu bar
        JMenu fileMenu = new JMenu(Localizer.get("panel.main.file"));
        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu infoMenu = new JMenu(Localizer.get("panel.main.information"));
        infoMenu.add(aboutProgramItem);
        infoMenu.add(aboutAuthorItem);

        JMenu settingMenu = new JMenu(Localizer.get("panel.main.setting"));
        settingMenu.add(recordSettingItem);
        settingMenu.add(databaseSettingItem);
        settingMenu.addSeparator();
        settingMenu.add(tglAppTheme);

        menuBar.add(fileMenu);
        menuBar.add(infoMenu);
        menuBar.add(settingMenu);
        if (frame instanceof JFrame window) {
            window.setJMenuBar(menuBar);
        } else if (frame instanceof JDialog window) {
            window.setJMenuBar(menuBar);
        }
        TicketGeneratorUtil.getLocalsConfiguration().addListener(new LocalizerListener() {
            @Override
            public void onUpdateLocale(Locale selectedLocale) {
                fileMenu.setText(Localizer.get("panel.main.file"));
                infoMenu.setText(Localizer.get("panel.main.information"));
                settingMenu.setText(Localizer.get("panel.main.setting"));
            }
        });

        this.initPanel();

        this.setComponentsListeners();
    }

    private JPanel pnlData;
    private JPanel pnlGenerate;

    /**
     * The method initialize view main panel
     */
    @Override
    public void initPanel() {
        this.setLayout(new BorderLayout());

        CompletableFuture.runAsync(() -> {
            pnlData = this.createDataInputPanel();
        });
        CompletableFuture.runAsync(() -> {
            pnlGenerate = this.createPanelMainActionPanel();
            pnlGenerate.setPreferredSize(new Dimension(430, pnlGenerate.getHeight()));
        });

        this.setConfigComponents();
        this.add(pnlData, BorderLayout.CENTER);
        this.add(pnlGenerate, BorderLayout.EAST);
    }

    /**
     * The method sets configuration all components bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.
     * This method invoked once panel generation.
     */
    @Override
    public void setConfigComponents() {
        // init MenuBar
        saveItem.setEnabled(false);

        tglAppTheme.setIcon(((AppThemeManager.getCurrentTheme() == ThemeApp.LIGHT))
                ? new ImageIcon(Objects.requireNonNull(FileNames.getResource(FileNames.nightModeApp)))
                : new ImageIcon(Objects.requireNonNull(FileNames.getResource(FileNames.lightModeApp))));
        tglAppTheme.setFocusable(false);

        // createDataInputPanel
        tfInstitute.setFont(new Font("Serif", Font.BOLD, 17));
        tfFaculty.setFont(new Font("Serif", Font.PLAIN, 17));
        tfDepartment.setFont(new Font("Serif", Font.PLAIN, 17));
        tfSpecialization.setFont(new Font("Serif", Font.PLAIN, 17));
        tfDiscipline.setFont(new Font("Serif", Font.PLAIN, 17));
        tfTeacher.setFont(new Font("Serif", Font.PLAIN, 17));
        tfHeadDepartment.setFont(new Font("Serif", Font.PLAIN, 17));
        tfProtocol.setFont(new Font("Serif", Font.PLAIN, 17));

        cbInstitute.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, universityService.count() > 0);
        cbFaculty.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
        cbDepartment.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
        cbSpecialization.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
        cbDiscipline.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
        cbHeadDepartment.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
        cbTeacher.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);

        tfTeacher.setToolTipText(Localizer.get("panel.main.firstlastname"));
        tfHeadDepartment.setToolTipText(Localizer.get("panel.main.firstlastname"));

        DatePickerSettings datePickerSettings = new DatePickerSettings(TicketGeneratorUtil.getLocalsConfiguration().getSelectedLocale());
        datePickerSettings.setFormatForDatesCommonEra("dd.MM.uuuu");
        datePicDecision.setSettings(datePickerSettings);
        datePicDecision.getComponentDateTextField().setFont(
                new Font("Serif", Font.PLAIN, 17));
        datePicDecision.getComponentDateTextField().setEnabled(false);
        datePicDecision.setDateToToday();
        datePicDecision.setFocusable(false);
        customizeUIDatePicker();
        // range with between September and December
        setTimeYearOnBoxTypeSession(datePicDecision.getDate());
        boxTypeSession.setFocusable(false);


        // createPanelMainActionPanel
        btnAdd.setFocusable(false);

        btnGroupReadWay.add(rdiBtnSequence);
        btnGroupReadWay.add(rdiBtnRandom);
        btnGroupWriteWay.add(rdiBtnWriteRandom);
        btnGroupWriteWay.add(rdiBtnWriteSequence);

        rdiBtnRandom.setVerticalAlignment(SwingConstants.CENTER);

        btnRemove.setFocusable(false);
        btnRemove.setEnabled(false);
        btnSave.setFocusable(false);
        btnSave.setEnabled(false);

        jBoxModes.setFocusable(false);

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

        // spinner number quantity tickets
        SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(10, 1, 1000, 1);
        spinnerQuantityTickets.setModel(spinnerNumberModel);
        JSpinner.NumberEditor numberEditor = (JSpinner.NumberEditor) spinnerQuantityTickets.getEditor();
        JFormattedTextField textField = numberEditor.getTextField();
        textField.setColumns(3);
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setFont(new Font("Serif", Font.PLAIN, 12));


        // spinner number quantity question in ticket
        spinnerNumberModel = new SpinnerNumberModel(3, 1, 50, 1);
        spinnerQuantityQuestionTickets.setModel(spinnerNumberModel);
        numberEditor = (JSpinner.NumberEditor) spinnerQuantityQuestionTickets.getEditor();
        textField = numberEditor.getTextField();
        textField.setColumns(3);
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setFont(new Font("Serif", Font.PLAIN, 12));
    }

    private void customizeUIDatePicker() {
        var settings = datePicDecision.getSettings();
        // цвет недели
        settings.setColorBackgroundWeekdayLabels(UIManager.getColor("Button.background"), false);
        // кнопка спаво снизу
        settings.setColor(DatePickerSettings.DateArea.BackgroundClearLabel, UIManager.getColor("Button.background"));
        // посередине 2е лейбы
        settings.setColor(DatePickerSettings.DateArea.BackgroundMonthAndYearMenuLabels, UIManager.getColor("Button.background"));
        // это кнопки навигации
        settings.setColor(DatePickerSettings.DateArea.BackgroundMonthAndYearNavigationButtons, UIManager.getColor("Button.background"));
        // кнопка лева снизу
        settings.setColor(DatePickerSettings.DateArea.BackgroundTodayLabel, UIManager.getColor("Button.background"));
        settings.setColor(DatePickerSettings.DateArea.BackgroundTopLeftLabelAboveWeekNumbers, UIManager.getColor("Button.disabledBackground"));

        // задает для всех типо касание мыши типо папал на облость то отобрази более темным
        settings.setColor(DatePickerSettings.DateArea.BackgroundCalendarPanelLabelsOnHover, UIManager.getColor("Button.disabledBackground"));
        // фон отображаю темным
        settings.setColor(DatePickerSettings.DateArea.BackgroundOverallCalendarPanel, UIManager.getColor("Button.disabledBackground"));
        // цвет выбраной данты
        settings.setColor(DatePickerSettings.DateArea.CalendarBackgroundSelectedDate, UIManager.getColor("Component.focusColor"));
        // цвет календаря
        settings.setColor(DatePickerSettings.DateArea.CalendarBackgroundNormalDates, UIManager.getColor("Button.background"));
        // это то что отображается уже даны на textFiled
        settings.setColor(DatePickerSettings.DateArea.TextFieldBackgroundValidDate, UIManager.getColor("Button.background"));

    }

    private void setTimeYearOnBoxTypeSession(LocalDate newDate) {
        var range = ValueRange.of(Month.SEPTEMBER.getValue(), Month.DECEMBER.getValue());
        boxTypeSession.setSelectedIndex((range.isValidIntValue(newDate.getMonthValue())) ? 0 : 1);
    }

    /**
     * The method sets necessary all components listeners
     */
    public void setComponentsListeners() {
        AppThemeManager.addThemeChangerListener(this);
        ActionListener handler = new ActionEventHandler();
        loadItem.addActionListener(handler);
        saveItem.addActionListener(handler);
        exitItem.addActionListener(handler);
        aboutAuthorItem.addActionListener(handler);
        aboutProgramItem.addActionListener(handler);
        recordSettingItem.addActionListener(handler);
        databaseSettingItem.addActionListener(handler);
        btnAdd.addActionListener(handler);
        btnRemove.addActionListener(handler);
        btnGenerate.addActionListener(handler);
        btnViewFile.addActionListener(handler);
        btnSave.addActionListener(handler);
        rdiBtnRandom.addActionListener(handler);
        rdiBtnSequence.addActionListener(handler);
        rdiBtnWriteRandom.addActionListener(handler);
        rdiBtnWriteSequence.addActionListener(handler);
        tglAppTheme.addActionListener(handler);

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

        //  Search Text Field Listeners -------------------------
        cbInstitute.addRelatedComponentListener(relatedComponentEvent -> {
            MyJCompoBox instituteComboBox = (MyJCompoBox) relatedComponentEvent.getSource();
            if (instituteComboBox.getSelectedItem() instanceof UniversityDTO) {
                inputSearchFieldsData.setUniversityDto((UniversityDTO) instituteComboBox.getSelectedItem());
                if (facultyService.countByLikeNameAndUniversity(cbFaculty.getEditorTextField().getText(),
                        inputSearchFieldsData.getUniversityDto().getId()) > 0) {
                    cbFaculty.setEnableElements(MyJCompoBox.Element.ALL, true);
                } else {
                    cbFaculty.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
                }
            } else {
                String text = instituteComboBox.getFieldText();
                universityService.getByName(text).ifPresentOrElse((elm) -> {
                    inputSearchFieldsData.setUniversityDto(elm);
                    if (facultyService.countByLikeNameAndUniversity(cbFaculty.getFieldText(),
                            inputSearchFieldsData.getUniversityDto().getId()) > 0) {
                        cbFaculty.setEnableElements(MyJCompoBox.Element.ALL, true);
                    } else {
                        cbFaculty.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
                    }
                }, () -> {
                    inputSearchFieldsData.setUniversityDto(UniversityDTO.builder().id(NO_FUND_ID).build());
                    inputSearchFieldsData.setFacultyDto(FacultyDto.builder().id(NO_FUND_ID).build());
                    cbFaculty.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
                    cbFaculty.setSelectedItem(null);

                    cbDepartment.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
                    cbDepartment.setSelectedItem(null);
                    inputSearchFieldsData.setDepartmentDto(DepartmentDto.builder().id(NO_FUND_ID).build());

                    cbSpecialization.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
                    cbSpecialization.setSelectedItem(null);
                    inputSearchFieldsData.setSpecializationDto(SpecializationDto.builder().id(NO_FUND_ID).build());

                    cbDiscipline.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
                    cbDiscipline.setSelectedItem(null);
                    inputSearchFieldsData.setDisciplineDto(DisciplineDto.builder().id(NO_FUND_ID).build());

                    cbHeadDepartment.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
                    cbHeadDepartment.setSelectedItem(null);
                    inputSearchFieldsData.setHeadDepartmentDto(HeadDepartmentDto.builder().id(NO_FUND_ID).build());

                    cbTeacher.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
                    cbTeacher.setSelectedItem(null);
                    inputSearchFieldsData.setTeacherDto(TeacherDto.builder().id(NO_FUND_ID).build());
                });
            }
            cbFaculty.updateDropDownList();
            cbFaculty.fireRelatedComponentListener(new RelatedComponentEvent(cbFaculty));
        });

        cbFaculty.addRelatedComponentListener(relatedComponentEvent -> {
            MyJCompoBox facultyComboBox = (MyJCompoBox) relatedComponentEvent.getSource();
            if (facultyComboBox.getSelectedItem() instanceof FacultyDto) {
                inputSearchFieldsData.setFacultyDto((FacultyDto) facultyComboBox.getSelectedItem());
                if (departmentService.countByLikeNameAndFacultyId(cbDepartment.getEditorTextField().getText(),
                        inputSearchFieldsData.getFacultyDto().getId()) > 0) {
                    cbDepartment.setEnableElements(MyJCompoBox.Element.ALL, true);
                } else {
                    cbDepartment.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
                }
                if (teacherService.countByLikeNameAndFacultyId(cbTeacher.getEditorTextField().getText(),
                        inputSearchFieldsData.getFacultyDto().getId()) > 0) {
                    cbTeacher.setEnableElements(MyJCompoBox.Element.ALL, true);
                } else {
                    cbTeacher.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
                }
            } else {
                String text = facultyComboBox.getEditorTextField().getText();
                if (facultyService.countByLikeNameAndUniversity(text, inputSearchFieldsData.getUniversityDto().getId()) == 0) {
                    text = "";
                }
                facultyService.getByName(text).ifPresentOrElse((elm) -> {
                    inputSearchFieldsData.setFacultyDto(elm);
                    if (departmentService.countByLikeNameAndFacultyId(cbDepartment.getFieldText(),
                            inputSearchFieldsData.getFacultyDto().getId()) > 0) {
                        cbDepartment.setEnableElements(MyJCompoBox.Element.ALL, true);
                    } else {
                        cbDepartment.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
                    }
                    if (teacherService.countByLikeNameAndFacultyId(cbTeacher.getFieldText(),
                            inputSearchFieldsData.getFacultyDto().getId()) > 0) {
                        cbTeacher.setEnableElements(MyJCompoBox.Element.ALL, true);
                    } else {
                        cbTeacher.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
                    }
                }, () -> {
                    inputSearchFieldsData.setFacultyDto(FacultyDto.builder().id(NO_FUND_ID).build());
                    inputSearchFieldsData.setDepartmentDto(DepartmentDto.builder().id(NO_FUND_ID).build());
                    inputSearchFieldsData.setTeacherDto(TeacherDto.builder().id(NO_FUND_ID).build());
                    cbDepartment.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
                    cbTeacher.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);

                    cbSpecialization.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
                    cbSpecialization.setSelectedItem(null);
                    inputSearchFieldsData.setSpecializationDto(SpecializationDto.builder().id(NO_FUND_ID).build());

                    cbDiscipline.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
                    cbDiscipline.setSelectedItem(null);
                    inputSearchFieldsData.setDisciplineDto(DisciplineDto.builder().id(NO_FUND_ID).build());

                    cbHeadDepartment.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
                    cbHeadDepartment.setSelectedItem(null);
                    inputSearchFieldsData.setHeadDepartmentDto(HeadDepartmentDto.builder().id(NO_FUND_ID).build());
                });
            }
            cbDepartment.updateDropDownList();
            cbTeacher.updateDropDownList();
            cbDepartment.fireRelatedComponentListener(new RelatedComponentEvent(cbDepartment));
            cbTeacher.fireRelatedComponentListener(new RelatedComponentEvent(cbTeacher));
        });
        cbDepartment.addRelatedComponentListener(relatedComponentEvent -> {
            MyJCompoBox departmentComboBox = (MyJCompoBox) relatedComponentEvent.getSource();
            if (departmentComboBox.getSelectedItem() instanceof DepartmentDto) {
                inputSearchFieldsData.setDepartmentDto((DepartmentDto) departmentComboBox.getSelectedItem());
                if (specializationService.countByLikeNameAndDepartmentId(cbSpecialization.getFieldText(),
                        inputSearchFieldsData.getDepartmentDto().getId()) > 0) {
                    cbSpecialization.setEnableElements(MyJCompoBox.Element.ALL, true);
                } else {
                    cbSpecialization.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
                }
                if (headDepartmentService.countByLikeNameAndDepartmentId(cbHeadDepartment.getFieldText(),
                        inputSearchFieldsData.getDepartmentDto().getId()) > 0) {
                    cbHeadDepartment.setEnableElements(MyJCompoBox.Element.ALL, true);
                } else {
                    cbHeadDepartment.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
                }
            } else {
                String text = departmentComboBox.getFieldText();
                if (departmentService.countByLikeNameAndFacultyId(text, inputSearchFieldsData.getFacultyDto().getId()) == 0) {
                    text = "";
                }
                departmentService.getByName(text).ifPresentOrElse((elm) -> {
                    inputSearchFieldsData.setDepartmentDto(elm);
                    if (specializationService.countByLikeNameAndDepartmentId(cbSpecialization.getFieldText(),
                            inputSearchFieldsData.getDepartmentDto().getId()) > 0) {
                        cbSpecialization.setEnableElements(MyJCompoBox.Element.ALL, true);
                    } else {
                        cbSpecialization.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
                    }
                    if (headDepartmentService.countByLikeNameAndDepartmentId(cbHeadDepartment.getFieldText(),
                            inputSearchFieldsData.getDepartmentDto().getId()) > 0) {
                        cbHeadDepartment.setEnableElements(MyJCompoBox.Element.ALL, true);
                    } else {
                        cbHeadDepartment.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
                    }
                }, () -> {
                    inputSearchFieldsData.setDepartmentDto(DepartmentDto.builder().id(NO_FUND_ID).build());
                    inputSearchFieldsData.setSpecializationDto(SpecializationDto.builder().id(NO_FUND_ID).build());
                    inputSearchFieldsData.setHeadDepartmentDto(HeadDepartmentDto.builder().id(NO_FUND_ID).build());
                    cbSpecialization.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
                    cbHeadDepartment.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);

                    cbDiscipline.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
                    cbDiscipline.setSelectedIndex(-1);
                    inputSearchFieldsData.setDisciplineDto(DisciplineDto.builder().id(NO_FUND_ID).build());
                    cbHeadDepartment.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
                    cbHeadDepartment.setSelectedIndex(-1);
                    inputSearchFieldsData.setHeadDepartmentDto(HeadDepartmentDto.builder().id(NO_FUND_ID).build());
                });
            }
            cbSpecialization.updateDropDownList();
            cbHeadDepartment.updateDropDownList();
            cbSpecialization.fireRelatedComponentListener(new RelatedComponentEvent(cbSpecialization));
            cbHeadDepartment.fireRelatedComponentListener(new RelatedComponentEvent(cbHeadDepartment));
        });
        cbSpecialization.addRelatedComponentListener(relatedComponentEvent -> {
            MyJCompoBox specComboBox = (MyJCompoBox) relatedComponentEvent.getSource();
            if (specComboBox.getSelectedItem() instanceof SpecializationDto) {
                inputSearchFieldsData.setSpecializationDto((SpecializationDto) specComboBox.getSelectedItem());
                if (disciplineService.countByLikeNameAndSpecializationId(cbDiscipline.getFieldText(),
                        inputSearchFieldsData.getSpecializationDto().getId()) > 0) {
                    cbDiscipline.setEnableElements(MyJCompoBox.Element.ALL, true);
                } else {
                    cbDiscipline.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
                }
            } else {
                String text = specComboBox.getEditorTextField().getText();
                if (specializationService.countByLikeNameAndDepartmentId(text, inputSearchFieldsData.getDepartmentDto().getId()) == 0) {
                    text = "";
                }
                specializationService.getByName(text).ifPresentOrElse((elm) -> {
                    inputSearchFieldsData.setSpecializationDto(elm);
                    if (disciplineService.countByLikeNameAndSpecializationId(cbDiscipline.getFieldText(),
                            inputSearchFieldsData.getSpecializationDto().getId()) > 0) {
                        cbDiscipline.setEnableElements(MyJCompoBox.Element.ALL, true);
                    } else {
                        cbDiscipline.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
                    }
                }, () -> {
                    inputSearchFieldsData.setSpecializationDto(SpecializationDto.builder().id(NO_FUND_ID).build());
                    inputSearchFieldsData.setDisciplineDto(DisciplineDto.builder().id(NO_FUND_ID).build());
                    cbDiscipline.setEnableElements(MyJCompoBox.Element.ARROW_BUTTON, false);
                });
            }
            cbDiscipline.updateDropDownList();
            cbDiscipline.fireRelatedComponentListener(new RelatedComponentEvent(cbDepartment));
        });
        cbDiscipline.addRelatedComponentListener((relatedComponentEvent -> {
            MyJCompoBox disciplineComboBox = (MyJCompoBox) relatedComponentEvent.getSource();
            if (disciplineComboBox.getSelectedItem() instanceof DisciplineDto) {
                inputSearchFieldsData.setDisciplineDto((DisciplineDto) disciplineComboBox.getSelectedItem());
            } else {
                String text = disciplineComboBox.getEditorTextField().getText();
                disciplineService.getByName(text).ifPresentOrElse(inputSearchFieldsData::setDisciplineDto,
                        () -> inputSearchFieldsData.setDisciplineDto(DisciplineDto.builder().id(NO_FUND_ID).build()));
            }
        }));

        cbHeadDepartment.addRelatedComponentListener(relatedComponentEvent -> {
            MyJCompoBox headDepComboBox = (MyJCompoBox) relatedComponentEvent.getSource();
            if (headDepComboBox.getSelectedItem() instanceof HeadDepartmentDto) {
                inputSearchFieldsData.setHeadDepartmentDto((HeadDepartmentDto) headDepComboBox.getSelectedItem());
            } else {
                String text = headDepComboBox.getEditorTextField().getText();
                headDepartmentService.getByName(text).ifPresentOrElse(inputSearchFieldsData::setHeadDepartmentDto,
                        () -> inputSearchFieldsData.setHeadDepartmentDto(HeadDepartmentDto.builder().id(NO_FUND_ID)
                                .build()));
            }
        });
        cbTeacher.addRelatedComponentListener(relatedComponentEvent -> {
            MyJCompoBox teacherCompoBox = (MyJCompoBox) relatedComponentEvent.getSource();
            if (teacherCompoBox.getSelectedItem() instanceof TeacherDto) {
                inputSearchFieldsData.setTeacherDto((TeacherDto) teacherCompoBox.getSelectedItem());
            } else {
                String text = teacherCompoBox.getEditorTextField().getText();
                teacherService.getByName(text).ifPresentOrElse(inputSearchFieldsData::setTeacherDto,
                        () -> inputSearchFieldsData.setTeacherDto(TeacherDto.builder().id(NO_FUND_ID)
                                .build()));
            }
        });
        // -----------------------------------

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
                log.trace("Frame application is closing");
                CompletableFuture<Boolean> task = CompletableFuture.supplyAsync(() -> {
                    try {
                        SerializeManager.runSerialize();
                        return true;
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                getRootFrame().setVisible(false);
                try {
                    if (executionThread != null) {
                        executionThread.interrupt();
                    }
                    PoolConnection.Builder.build().destroy();
                    task.get();

                } catch (ConnectionPoolException | ExecutionException | InterruptedException ex) {
                    throw new RuntimeException(ex);
                } finally {
                    log.info("Application is closed");
                }
            }

        });

        //https://stackoverflow.com/questions/3949382/jspinner-value-change-events
        JFormattedTextField formTextField = ((JSpinner.NumberEditor) spinnerQuantityTickets.getEditor())
                .getTextField();
        DefaultFormatter form = (DefaultFormatter) formTextField.getFormatter();
        form.setCommitsOnValidEdit(true);
        spinnerQuantityTickets.addChangeListener(e -> {
        });

        formTextField = ((JSpinner.NumberEditor) spinnerQuantityQuestionTickets.getEditor()).getTextField();
        form = (DefaultFormatter) formTextField.getFormatter();
        form.setCommitsOnValidEdit(true);
        spinnerQuantityQuestionTickets.addChangeListener(e -> {
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

        boolean bolTeach = TextPatterns.PERSON_NAME_PATTERN_V1.matches(tfTeacher.getText());
        boolean bolHeadDest = TextPatterns.PERSON_NAME_PATTERN_V1.matches(tfHeadDepartment.getText());
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


    private final MyJCompoBox cbInstitute;

    private final MyJCompoBox cbFaculty;

    private final MyJCompoBox cbDepartment;

    private final MyJCompoBox cbSpecialization;

    private final MyJCompoBox cbDiscipline;

    private final MyJCompoBox cbHeadDepartment;

    private final MyJCompoBox cbTeacher;

    /**
     * This method created data input panel
     *
     * @return created panel
     */
    private JPanel createDataInputPanel() {
        JPanel panelLEFT = new JPanel(new GridBagLayout());
        panelLEFT.setBorder(new TitledBorder(Localizer.get("panel.main.space.datainput")));

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
//        panelLEFT.add(tfInstitute, gbc1);
        panelLEFT.add(cbInstitute, gbc1);

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
//        panelLEFT.add(tfFaculty, gbc3);
        panelLEFT.add(cbFaculty, gbc3);

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
//        panelLEFT.add(tfDepartment, gbc5);
        panelLEFT.add(cbDepartment, gbc5);

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
//        panelLEFT.add(tfSpecialization, gbc7);
        panelLEFT.add(cbSpecialization, gbc7);

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
//        panelLEFT.add(tfDiscipline, gbc9);
        panelLEFT.add(cbDiscipline, gbc9);

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
//        panelLEFT.add(tfTeacher, gbc11);
        panelLEFT.add(cbTeacher, gbc11);

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
//        panelLEFT.add(tfHeadDepartment, gbc13);
        panelLEFT.add(cbHeadDepartment, gbc13);


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
    private final JComboBox<GenerationMode> jBoxModes;
    private final JLabel lbGenerationMode;
    private final JButton btnGenerate;
    private final JButton btnViewFile;
    private final JButton btnSave;
    private final JLabel lblCountItems;
    private final JLabel lblListSize;
    private final JLabel lbQuantityTickets;
    private final JTextField tfQuantityTickets;
    private final JSpinner spinnerQuantityTickets;
    private final JLabel lbQuantityQuestionTickets;
    private final JTextField tfQuantityQuestionTickets;
    private final JSpinner spinnerQuantityQuestionTickets;
    private final JLabel lbReadQuestRandom;
    private final ButtonGroup btnGroupReadWay;
    private final JRadioButton rdiBtnRandom;
    private final JRadioButton rdiBtnSequence;
    private final JLabel lbWriteQuestRandom;
    private final ButtonGroup btnGroupWriteWay;
    private final JRadioButton rdiBtnWriteRandom;
    private final JRadioButton rdiBtnWriteSequence;

    /**
     * The method creates a panel containing a list of downloaded files
     * and also needed buttons for manage
     *
     * @return created panel
     */
    private JPanel createPanelMainActionPanel() {
        JPanel pnlRes = new JPanel();
        pnlRes.setLayout(new BoxLayout(pnlRes, BoxLayout.Y_AXIS));
        pnlRes.setBorder(new TitledBorder(Localizer.get("panel.main.space.resources")));
        TicketGeneratorUtil.getLocalsConfiguration().addListener(new LocalizerListener() {
            @Override
            public void onUpdateLocale(Locale selectedLocale) {
                pnlRes.setBorder(new TitledBorder(Localizer.get("panel.main.space.resources")));
            }
        });
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


        JPanel pnlBtnGenerate = new JPanel();
        pnlBtnGenerate.setLayout(new BoxLayout(pnlBtnGenerate, BoxLayout.Y_AXIS));

        JPanel panelRow = new JPanel(new GridLayout(1, 2));
        panelRow.add(lbReadQuestRandom);
        JPanel pnlGroupBtn = new JPanel(new GridLayout(1, 2));
        pnlGroupBtn.add(rdiBtnSequence);
        pnlGroupBtn.add(rdiBtnRandom);
        panelRow.add(pnlGroupBtn);
        pnlBtnGenerate.add(panelRow);

        panelRow = new JPanel(new GridLayout(1, 2));
        panelRow.add(lbWriteQuestRandom);
        pnlGroupBtn = new JPanel(new GridLayout(1, 2));
        pnlGroupBtn.add(rdiBtnWriteSequence);
        pnlGroupBtn.add(rdiBtnWriteRandom);
        panelRow.add(pnlGroupBtn);
        pnlBtnGenerate.add(panelRow);


        panelRow = new JPanel(new GridLayout(1, 2));
        panelRow.add(lbGenerationMode);
        panelRow.add(jBoxModes);
        pnlBtnGenerate.add(panelRow);

        panelRow = new JPanel(new GridLayout(1, 2, 0, 10));
        panelRow.add(lbQuantityTickets);
        JPanel pSpinner = new JPanel();
        pSpinner.setLayout(new BorderLayout());
        pSpinner.add(spinnerQuantityTickets, BorderLayout.LINE_START);
        panelRow.add(pSpinner);
        pnlBtnGenerate.add(panelRow);


        panelRow = new JPanel(new GridLayout(1, 2, 0, 10));
        panelRow.add(lbQuantityQuestionTickets);
        pSpinner = new JPanel();
        pSpinner.setLayout(new BorderLayout());
        pSpinner.add(spinnerQuantityQuestionTickets, BorderLayout.LINE_START);
        panelRow.add(pSpinner);
        pnlBtnGenerate.add(panelRow);

        panelRow = new JPanel(new GridLayout(3, 1));
        panelRow.add(btnGenerate);
        panelRow.add(btnViewFile);
        panelRow.add(btnSave);
        pnlBtnGenerate.add(panelRow);

        pnlRes.add(pnlBtnGenerate);

        return pnlRes;
    }
    private AbstractTicketGenerator<Question2, Ticket<Question2>> ticketGenerator;
    private final LoadingDialog loadingDialog;
    private TicketsGenerationExecutionThread executionThread;
    private File tmpFileDocx;
    private boolean isRandomRead;
    private boolean isRandomWrite;

    @Override
    public Component getComponent() {
        return null;
    }

    @Override
    public void updateComponent() {
        customizeUIDatePicker();
        Arrays.stream(chooserUpLoad.getComponents())
                .forEach(AppThemeManager::updateComponentTreeUI);
        Arrays.stream(chooserSave.getComponents())
                .forEach(AppThemeManager::updateComponentTreeUI);
    }


    /**
     * The class is a descendant of the ticket generation execution
     *
     * @author Gorbachev I. D.
     * @version 19.04.2022
     */
    private final class TicketsGenerationExecutionThread extends Thread {
        private volatile boolean singleOfInterrupted = false;

        @Override
        public void interrupt() {
            super.interrupt();
            singleOfInterrupted = true;
        }

        @Override
        public boolean isInterrupted() {
            return singleOfInterrupted;
        }

        /**
         * Flow execution  method
         */
        @Override
        public void run() {
            this.setEnabledComponents(false, false);
            int quantityTickets = (int) spinnerQuantityTickets.getValue();
            int quantityQuestionInTicket = (int) spinnerQuantityQuestionTickets.getValue();


            File[] filesRes = this.toArrayFiles(modelListFilesRsc.toArray());
            Ticket<Question2> tempTicket = Ticket.of(
                    tfInstitute.getText(),
                    tfFaculty.getText(),
                    tfDepartment.getText(), tfSpecialization.getText(),
                    tfDiscipline.getText(), tfTeacher.getText(),
                    tfHeadDepartment.getText(),
                    (Ticket.SessionType) boxTypeSession.getSelectedItem(),
                    datePicDecision.getText(), tfProtocol.getText(), quantityQuestionInTicket);
            SenderMessage registerSenderMsg = SenderMsgFactory.getInstance().getNewSenderMsg();
            // registrations sender msg
            registerSenderMsg.add(loadingDialog);
            ticketGenerator = TicketGeneratorImpl.builder().senderMsg(registerSenderMsg).build(false, filesRes, tempTicket);

            var generateWay = ((GenerationMode)
                    Objects.requireNonNull(jBoxModes.getSelectedItem())).getGenerateWay();
            var property = new GenerationPropertyImpl(quantityTickets, quantityQuestionInTicket,
                    true,
                    generateWay,
                    isRandomRead, isRandomWrite);
            property.setWriterTicketProperty(recordSettingDialog.getWriterTicketProperty());

            boolean repeat;
            do {
                try {
                    registerSenderMsg.sendMsg(Localizer.get("panel.main.message.registrator.start"));
                    loadingDialog.showDialog();
                    log.debug("**try-start...: thread: {}, isInterrupted: {} **", getName(), isInterrupted());
                    if (isInterrupted()) throw new InterruptedException();
                    ticketGenerator.startGenerate(property);
                    repeat = false;
                } catch (ExecutionException | GenerationConditionException ex) {
                    loadingDialog.closeDialog();
                    if (ex.getClass() == FindsNonMatchingLevel.class || ex.getClass() == NumberQuestionsRequireException.class) {

                        int selected = JOptionPane.showInternalConfirmDialog(null,
                                ex.getMessage() + "\n" +
                                Localizer.get("panel.message.generation.continue"),
                                Localizer.get("panel.message.title.msg"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (selected == JOptionPane.OK_OPTION) {
                            repeat = true;
                            if (ex.getClass() == FindsNonMatchingLevel.class) {
                                property.setFlagContinGenWithDepriveLev(true);
                            } else {
                                property.setUnique(false);
                            }
                        } else {
                            repeat = false;
                            this.setEnabledComponents(true, false);
                        }

                    } else if (ex.getClass() == FindsChapterWithoutSection.class) {
                        int selected = JOptionPane.showInternalConfirmDialog(null,
                                ex.getMessage() + "\n" +
                                Localizer.get("panel.message.generation.continue"),
                                Localizer.get("panel.message.title.msg"), JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                        if (selected == JOptionPane.OK_OPTION) {
                            repeat = true;
//                            loadingDialog.showDialog();
                            property.setFlagContinGenWithChapterWithoutSection(true);
                        } else {
                            repeat = false;
                            this.setEnabledComponents(true, false);
                        }
                    } else {
                        if (ex.getCause() instanceof IOException && ex.getCause().getMessage().contains("Zip bomb detected! The file would exceed the max.")) {
                            JOptionPane.showMessageDialog(null, Localizer.get("panel.message.file.big"),
                                    Localizer.get("panel.message.title.warn"), JOptionPane.ERROR_MESSAGE);
                        } else {
                            // general condition exception and execution exception
                            JOptionPane.showMessageDialog(null,
                                    (ex.getCause() != null) ? ex.getCause().getMessage() // handle InterruptedException
                                            : ex.getMessage(),// handle ConditionGenerationException
                                    Localizer.get("panel.message.title.warn"), JOptionPane.ERROR_MESSAGE);
                            log.warn("", ex);
                        }
                        this.setEnabledComponents(true, false);
                        repeat = false; // necessary, because need set value false, if earlier repeat = true
                        loadingDialog.closeDialog();
                    }

                } catch (InterruptedException e) {
                    loadingDialog.closeDialog();
                    log.error(Thread.currentThread() + " is interrupted: Reason : interrupted generate tickets by" +
                              "close program during ticket generation: interrupted is successful");
                    this.setEnabledComponents(true, false);
                    repeat = false; // necessary, because need set value false, if earlier repeat = true
                } catch(SenderStopSleepException senderException) {
                    // Если поучилось так, что генерацию остановил SenderMessage когда он спал.
                    log.warn("Generator ticket was stopped by reason stopping SenderMessage");
                    this.setEnabledComponents(true, false);
                    repeat = false; // necessary, because need set value false, if earlier repeat = true
                } catch (Exception allExceptions) {
                    loadingDialog.closeDialog();
                    // general condition exception and execution exception
                    JOptionPane.showMessageDialog(null,
                            (allExceptions.getCause() != null) ? allExceptions.getCause().getMessage() // handle InterruptedException
                                    : allExceptions.getMessage(),// handle ConditionGenerationException
                            Localizer.get("panel.message.title.warn"), JOptionPane.ERROR_MESSAGE);
                    log.warn("", allExceptions);
                    this.setEnabledComponents(true, false);
                    repeat = false; // necessary, because need set value false, if earlier repeat = true
                }catch (StackOverflowError stackOverflowError) {
                    log.warn("", stackOverflowError);
                    this.setEnabledComponents(true, false);
                    repeat = false; // necessary, because need set value false, if earlier repeat = true
                }
            } while (repeat);

            // if docx file is generated, then ...
            // save generate file docx inside project for further conversion to pdf file
            if (ticketGenerator.getDocxDec() != null) {
                InputStream inputStream = null;
                OutputStream outputStream = null;
                IConverter converter;
                try {
                    try {
                        registerSenderMsg.sendMsg(Localizer.get("panel.main.message.registrator.file.tmp"));
                        log.info("create temp file .docx .pdf");
                        tmpFileDocx = File.createTempFile("tmpDocx", ".docx");
                        tmpFileDocx.deleteOnExit();
                        File tmpFilePdf = File.createTempFile("tmpPdf", ".pdf");
                        tmpFilePdf.deleteOnExit();

                        registerSenderMsg.sendMsg(Localizer.get("panel.main.message.registrator.file.write"));
                        log.info("write output data to tmp .docx file");
                        ticketGenerator.writeOutputFile(tmpFileDocx);

                        /*Since if the user wants 100,000 tickets,the memory will fill up by 3GB.
                          At the end of the generation, we will write the generator*/
                        ticketGenerator = null;
                        Runtime.getRuntime().gc();

                        // convert saved file to pdf file
                        inputStream = new FileInputStream(tmpFileDocx);
                        outputStream = new FileOutputStream(tmpFilePdf);
                        converter = LocalConverter.builder().build();

                        registerSenderMsg.sendMsg(Localizer.get("panel.main.message.registrator.file.convert.start"));
                        log.info("convert docx => pdf");
                        converter.convert(inputStream).as(DocumentType.DOCX).to(outputStream)
                                .as(DocumentType.PDF).execute();
                        registerSenderMsg.sendMsg(Localizer.get("panel.main.message.registrator.file.convert"));
                        log.info("convert docx => pdf is : success");
                        // if convert docx is success, then load file for open viewFilePanel
                        viewFileDialog.setFile(tmpFilePdf);
                        try {
                            viewFileDialog.openDocument();
                        } catch (Exception ex) {
                            JOptionPane.showConfirmDialog(frameRoot,
                                    Localizer.get("panel.message.file.not-founded"));
                            this.setEnabledComponents(true, false);
                        }
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    }
                }catch(SenderStopSleepException ex){
                    log.warn("Generator ticket was stopped by reason stopping SenderMessage");
                } catch (Throwable e) {
                    loadingDialog.closeDialog();
                    this.setEnabledComponents(true, false);
                    log.error("CONVERTOR Xyeta 5min wait that close program");
                    log.error(e);
                    if(e instanceof InterruptedException){
                        return;
                    }
                    if (((e.getCause() != null) && (e.getCause() instanceof InterruptedException))) {
                        return;
                    }
                    if(e instanceof IllegalStateException ){
                        // Ошибка возникает потому что e.getCause == MicrosoftWordBridge
                        //ERROR com.documents4j.conversion.msoffice.MicrosoftWordBridge - Thread responsible for running script was interrupted: C:\Users\SecuRiTy\AppData\Local\Temp\tmp11017103057597779222\word_start905584576.vbs
                        return;
                    }
                    JOptionPane.showMessageDialog(null, //panel.message.generation.error
                            Localizer.get("panel.message.generation.error"),
                            Localizer.get("panel.message.title.error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // after generating and conversion enable all components
                loadingDialog.closeDialog();
                this.setEnabledComponents(true, true);
                JOptionPane.showInternalMessageDialog(
                        null, Localizer.get("panel.message.generation.success"), "",
                        JOptionPane.INFORMATION_MESSAGE, new ImageIcon(
                                Objects.requireNonNull(FileNames.getResource(FileNames.successIcon))
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
     * This class is JDialog object/
     *
     * @see BaseDialog
     */
    private final class LoadingDialog extends BaseDialog implements PanelFunc, MessageRetriever {

        public LoadingDialog() {
            super(MainWindowPanel.this.getRootFrame());
        }

        @Override
        public void initDialog() {
            Dimension sizeScreen = toolkit.getScreenSize();
            Dimension sizeFrame = new Dimension(250, 100);
            this.setBounds((sizeScreen.width - sizeFrame.width) / 2,
                    (sizeScreen.height - sizeFrame.height) / 2 - 20,
                    sizeFrame.width, sizeFrame.height);
            this.setMinimumSize(sizeFrame);
            // remove close option
            this.setUndecorated(true);
            this.pack();
            this.validate();
            initPanel();
        }

        private JButton btnCancel;
        private JLabel lblMsg;
        private JLabel labelGen;

        @Override
        public void initPanel() {
            // init fields
            this.setLayout(new BorderLayout());
            btnCancel = new JButton(Localizer.get("btn.cancel"));
            lblMsg = new JLabel(Localizer.get("panel.main.message.registrator.launch"));
            labelGen = new JLabel(Localizer.get("panel.message.generation"));
            JScrollPane scrollPane = new JScrollPane(lblMsg);


            JPanel pnlMain = new JPanel();
            pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));

            JPanel pnlRow = new JPanel();
            pnlRow.setLayout(new BoxLayout(pnlRow, BoxLayout.X_AXIS));
            pnlRow.add(new JLabel(initImageIcon()));
            pnlRow.add(labelGen);
            pnlMain.add(pnlRow);
            pnlMain.add(scrollPane);

            this.add(pnlMain, BorderLayout.CENTER);
            this.add(btnCancel, BorderLayout.SOUTH);
            setConfigComponents();
            setComponentsListeners();
        }

        private ImageIcon initImageIcon() {
            ImageIcon icon = new ImageIcon(FileNames.getResource(FileNames.spinnerLoaderIcon));
            return new ImageIcon(icon.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
        }

        @Override
        public void setComponentsListeners() {
            this.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentShown(ComponentEvent e) {
                    log.debug("Loading dialog is showed");
                }

                @Override
                public void componentHidden(ComponentEvent e) {
                    log.debug("Loading dialog is hidden");
                }
            });
            btnCancel.addActionListener(e -> {
                log.debug("@@cancel generate ticket: " + executionThread.getName());
                executionThread.interrupt();
                closeDialog();
            });

        }

        @Override
        public void setConfigComponents() {
            this.setModal(true);
            this.setResizable(false);
            lblMsg.setFont(new Font("Serif", Font.PLAIN, 13));
            lblMsg.setMinimumSize(new Dimension(lblMsg.getWidth(),
                    12));
        }

        @Override
        public Window getRootFrame() {
            return MainWindowPanel.this.getRootFrame();
        }

        public void closeDialog() {
            this.setModal(false);
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }

        private volatile boolean isRun;

        public void showDialog() {
            isRun = false;
            TicketsGenerationExecutionThread ticketGenerationExeThread = (TicketsGenerationExecutionThread) Thread.currentThread();
            var thread = new Thread(() -> {
                isRun = true;
                this.setModal(true);
                this.setVisible(true);
//                currThread.interrupt();
            });
            thread.start();

            try {
                while (!isRun) {
                    log.debug("WAIT SHOW IDALOG: {}, isInterrupted: {}", ticketGenerationExeThread.getName(), ticketGenerationExeThread.isInterrupted());
                    Thread.sleep(100);
                }
            } catch (InterruptedException ignored) {
                log.debug("EXXXEEEECCCUTTTOR thread wake up :): ticketGenerationExeThread.isInterrupt = {}", ticketGenerationExeThread.isInterrupted());
                // когда происходит обработки InterruptedException то флаг isInterrupted == false.
                // Это логично, так как мы обработали данное исключение и java восстанавливает его состояния в рабочее
            }
        }

        @Override
        public void send(String msg) {
            SwingUtilities.invokeLater(() -> {
                lblMsg.setText(msg);
            });
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
                    !TextPatterns.PERSON_NAME_PATTERN_V1.matches(textField.getText())) {
                    msg = Localizer.get("panel.main.input.validator.example");
                } else if (textField == tfProtocol &&
                           !TextPatterns.PROTOCOL_PATTERN.matches(textField.getText())) {
                    msg = Localizer.get("panel.main.input.validator.protocol.access");
                } else if ((textField == tfQuantityTickets || textField == tfQuantityQuestionTickets) &&
                           !TextPatterns.NUMBER_PATTERN.matches(textField.getText())) {
                    msg = Localizer.get("panel.main.input.validator.number.access");
                } else if (textField != tfTeacher && textField != tfHeadDepartment &&
                           textField != tfProtocol &&
                           textField != tfQuantityQuestionTickets && textField != tfQuantityTickets &&
                           !TextPatterns.COMMON_PATTERN.matches(textField.getText())) {
                    msg = Localizer.get("panel.main.input.validator.symbols.access");
                } else { // кидаем ошибку
                    textField.setForeground(Color.BLACK);
                }

                if (!msg.isEmpty()) {
                    JOptionPane.showMessageDialog(null, Localizer.getWithValues("panel.main.input.validator.input.incorrect",
                            "\n" + msg));
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
                int res = chooserUpLoad.showDialog(frameRoot, Localizer.get("panel.load"));
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
                if (tmpFileDocx != null) {
                    new Thread(() -> {
                        int res = chooserSave.showSaveDialog(frameRoot);
                        if (res == JFileChooser.APPROVE_OPTION) {
                            File saveFile = chooserSave.getSelectedFile();
                            if (!saveFile.getName().toLowerCase().endsWith(".docx")) {
                                saveFile = new File(saveFile.getParentFile(),
                                        saveFile.getName() + ".docx");
                            }
                            try {
                                Files.deleteIfExists(saveFile.toPath());
                                Files.copy(tmpFileDocx.toPath(), saveFile.toPath());
                                int selected = JOptionPane.showInternalConfirmDialog(null,
                                        Localizer.get("panel.message.file.saved"),
                                        Localizer.get("panel.message.title.msg"), JOptionPane.YES_NO_OPTION,
                                        JOptionPane.QUESTION_MESSAGE);

                                if (selected == JOptionPane.OK_OPTION) {
                                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "
                                                              + saveFile.getPath());
                                }
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(null,
                                        Localizer.get("panel.message.error.unknown"), //panel.message.error.unknown
                                        Localizer.get("panel.message.title.error"), JOptionPane.ERROR_MESSAGE);
                                log.warn("", ex);
                            }
                        }
                    }).start();
                } else {
                    log.warn("To save file not can: tmpFileDocx == null");
                }

            } else if (e.getSource() == exitItem) {
                // вызываем события закрытия окна, чтобы вызвался слушатель и он закроет все потоки безопасно
                frameRoot.dispatchEvent(new WindowEvent(frameRoot,
                        WindowEvent.WINDOW_CLOSING));

            } else if (e.getSource() == aboutAuthorItem) {
                SwingUtilities.invokeLater(() -> {
                    aboutAuthorDialog = (Objects.isNull(aboutAuthorDialog)) ? (AboutAuthor) FrameDialogFactory.getInstance()
                            .createJDialog(frame, FrameType.ABOUT_AUTHOR, PanelType.ABOUT_AUTHOR) : aboutAuthorDialog;
                    aboutAuthorDialog.setVisible(true);
                });
            } else if (e.getSource() == aboutProgramItem) {
                SwingUtilities.invokeLater(() -> {
                    aboutProgramDialog = (Objects.isNull(aboutProgramDialog)) ? (AboutProgram) FrameDialogFactory.getInstance()
                            .createJDialog(frame, FrameType.ABOUT_PROGRAM, PanelType.ABOUT_PROGRAM) : aboutProgramDialog;
                    aboutProgramDialog.setVisible(true);
                });

            } else if (e.getSource() == recordSettingItem) {
                recordSettingDialog.setVisible(true);
            } else if (e.getSource() == databaseSettingItem) {
                SwingUtilities.invokeLater(() -> {
                    dataBaseDialog = (Objects.isNull(dataBaseDialog)) ? (InputParametersDialog) FrameDialogFactory.getInstance()
                            .createJDialog(frame, FrameType.INPUT_PARAM_DB, PanelType.INPUT_PARAM_DB) : dataBaseDialog;
                    dataBaseDialog.setVisible(true);
                });
            } else if (e.getSource() == tglAppTheme) {
                SwingUtilities.invokeLater(() -> {
                    AppThemeManager.swapTheme();
                    tglAppTheme.setIcon((AppThemeManager.getCurrentTheme() == ThemeApp.LIGHT)
                            ? new ImageIcon(Objects.requireNonNull(FileNames.getResource(FileNames.nightModeApp)))
                            : new ImageIcon(Objects.requireNonNull(FileNames.getResource(FileNames.lightModeApp))));
                    tglAppTheme.updateUI();
                });
            } else if (e.getSource() == btnRemove) {
                File[] selectedElements = jList.getSelectedValuesList().toArray(new File[0]);
                if (selectedElements.length > 0) {
                    for (File element : selectedElements) {
                        modelListFilesRsc.removeElement(element);
                    }
                    lblListSize.setText("" + modelListFilesRsc.size());
                }

            } else if (e.getSource() == rdiBtnRandom) {
                isRandomRead = true;
            } else if (e.getSource() == rdiBtnSequence) {
                isRandomRead = false;
            } else if (e.getSource() == rdiBtnWriteRandom) {
                isRandomWrite = true;
            } else if (e.getSource() == rdiBtnWriteSequence) {
                isRandomWrite = false;
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
                new Thread(() -> viewFileDialog.setVisible(true)).start();
            }
        }
    }
}