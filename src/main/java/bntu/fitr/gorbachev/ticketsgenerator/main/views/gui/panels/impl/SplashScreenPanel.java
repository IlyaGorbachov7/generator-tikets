package bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.impl;


import bntu.fitr.gorbachev.ticketsgenerator.main.views.ChangeFieldModelEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.controller.impl.SplashScreenController;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.InitViewEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.frames.FrameDialogFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.BasePanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.PanelType;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.tools.*;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.model.impl.SplashScreenModel;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

/**
 * The class represent splash screen panel
 *
 * @author Gorbachev I. D.
 * @version 24.03.2022
 */
@Getter
public class SplashScreenPanel extends BasePanel {

    private final SplashScreenController controller;

    private final JLabel lbUniversity;
    private final JLabel lbFaculty;
    private final JLabel lbDepartment;
    private final JLabel lbCoursework;
    private final JLabel lbDiscipline;
    private final JLabel lbNameCoursework;
    private final JLabel labelIcon;
    private final JLabel lbInfoDeveloper;
    private final JLabel lbNameDeveloper;
    private final JLabel lbInfoTeacher;
    private final JLabel lbNameTeacher;
    private final JLabel lbYear;
    private final JButton btnNext;
    private final JButton btnExit;
    private final Thread thread;
    private final JProgressBar progressBar;
    private JFrame mainWindow;

    {
        String university = "Беларусский национальный технический университет";
        String faculty = "Факультет информационных технологий и робототехники";
        String department = "Кафедра программного обеспечения " +
                            "информационных систем и технологий";
        String infoDeveloper = "Выполнил: Студент группы 10702419";
        String nameDeveloper = "Горбачёв Илья Дмитриевич";
        String infoTeacher = "Преподаватель: к.ф.-м.н.,доц.";
        String nameTeacher = "Ковалёва Ирина Львовна";
        String courseType = "Курсовая работа";
        String destipline = "по дисциплине: \"Программирование на языке Java\"";
        String appName = "Конструктор экзаменационных билетов";
        String cityYear = "Минск, 2022";
        lbUniversity = new JLabel(university);
        lbFaculty = new JLabel(faculty);
        lbDepartment = new JLabel(department);
        lbCoursework = new JLabel(courseType);
        lbDiscipline = new JLabel(destipline);
        lbNameCoursework = new JLabel(appName);
        labelIcon = new JLabel(new ImageIcon(Objects.requireNonNull(
                FileNames.getResource(FileNames.iconCoursework))));
        lbInfoDeveloper = new JLabel(infoDeveloper);
        lbNameDeveloper = new JLabel(nameDeveloper);
        lbInfoTeacher = new JLabel(infoTeacher);
        lbNameTeacher = new JLabel(nameTeacher);
        lbYear = new JLabel(cityYear);

        btnNext = new JButton("Далее");
        btnExit = new JButton("Выход");

        progressBar = new JProgressBar();

        thread = new Thread(() -> {
            try {
                Thread.sleep(60_000);
                System.exit(0);
            } catch (InterruptedException ignored) {
            }
        });
        threadProcess = this.new ThreadProcessBar();
        threadInit = this.new ThreadInitMinWindow();
    }

    /**
     * The constructor creates a panel
     *
     * @param frame frame contains created this  panel
     */
    public SplashScreenPanel(Window frame) {
        super(frame);
        this.initPanel();
        this.setComponentsListeners();
        controller = new SplashScreenController(this, new SplashScreenModel());
        thread.start();
    }

    /**
     * The method create view panel
     */
    @Override
    public void initPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel miniPanels = new JPanel(new GridLayout(2, 1));
        JPanel panelTop = new JPanel(new GridLayout(6, 1));
        panelTop.add(lbUniversity);
        panelTop.add(lbFaculty);
        panelTop.add(lbDepartment);
        panelTop.add(lbCoursework);
        panelTop.add(lbDiscipline);
        panelTop.add(lbNameCoursework);
        miniPanels.add(panelTop);
        JPanel panelInfo = new JPanel(new GridLayout(1, 2));
        panelInfo.add(labelIcon);
        JPanel pInfoDevs = new JPanel(new GridLayout(4, 1));
        pInfoDevs.add(lbInfoDeveloper);
        pInfoDevs.add(lbNameDeveloper);
        pInfoDevs.add(lbInfoTeacher);
        pInfoDevs.add(lbNameTeacher);
        panelInfo.add(pInfoDevs);
        miniPanels.add(panelInfo);
        // В неё год, и кнопки

        JPanel miniPanels1 = new JPanel(new GridLayout(2, 1));
        miniPanels1.add(lbYear);

        JPanel panelBtn = new JPanel(new GridLayout(1, 2, 5, 5));
        panelBtn.add(btnNext);
        btnNext.setBorder(BorderFactory.createRaisedBevelBorder());
        panelBtn.add(btnExit);

        miniPanels1.add(panelBtn);

        setConfigComponents();
        this.add(miniPanels, BorderLayout.CENTER);
        this.add(miniPanels1, BorderLayout.SOUTH);
        this.add(progressBar, BorderLayout.NORTH);
    }

    /**
     * The method sets configuration all components bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels. This method invoked once panel generation
     */
    @Override
    public void setConfigComponents() {
        lbUniversity.setFont(new Font("Dialog", Font.BOLD, 15));
        lbUniversity.setHorizontalAlignment(SwingConstants.CENTER);
        lbUniversity.setVerticalAlignment(SwingConstants.BOTTOM);
        lbFaculty.setHorizontalAlignment(SwingConstants.CENTER);
        lbFaculty.setVerticalAlignment(SwingConstants.BOTTOM);
        lbDepartment.setHorizontalAlignment(SwingConstants.CENTER);
        lbDepartment.setVerticalAlignment(SwingConstants.TOP);
        lbCoursework.setFont(new Font("Dialog", Font.BOLD, 20));
        lbCoursework.setHorizontalAlignment(SwingConstants.CENTER);
        lbCoursework.setVerticalAlignment(SwingConstants.BOTTOM);
        lbDiscipline.setHorizontalAlignment(SwingConstants.CENTER);
        lbDiscipline.setVerticalAlignment(SwingConstants.TOP);
        lbNameCoursework.setFont(new Font("Dialog", Font.BOLD, 20));
        lbNameCoursework.setHorizontalAlignment(SwingConstants.CENTER);
        lbNameCoursework.setVerticalAlignment(SwingConstants.TOP);
        labelIcon.setVerticalAlignment(SwingConstants.CENTER);
        labelIcon.setHorizontalAlignment(SwingConstants.CENTER);
        lbInfoDeveloper.setVerticalAlignment(SwingConstants.BOTTOM);
        lbNameDeveloper.setVerticalAlignment(SwingConstants.TOP);
        lbInfoTeacher.setFont(new Font("Dialog", Font.BOLD, 12));
        lbInfoTeacher.setVerticalAlignment(SwingConstants.BOTTOM);
        lbNameTeacher.setFont(new Font("Dialog", Font.BOLD, 12));
        lbNameTeacher.setVerticalAlignment(SwingConstants.TOP);
        lbYear.setHorizontalAlignment(SwingConstants.CENTER);
        lbYear.setFont(new Font("Dialog", Font.BOLD, 15));

        progressBar.setVisible(false);
        progressBar.setPreferredSize(new Dimension(progressBar.getWidth(), 7));
        progressBar.setMinimum(0);
        progressBar.setMaximum(200);
    }

    @Override
    public void changeStateViewElems(ChangeFieldModelEvent event) {

    }

    @Override
    public void primaryInitViewElems(InitViewEvent event) {

    }

    @Getter
    private final Thread threadProcess;
    @Getter
    private final Thread threadInit;

    /**
     * The method sets necessary all components listeners
     */
    @Override
    public void setComponentsListeners() {
        btnExit.addActionListener(e -> controller.actionExitBtn());

        btnNext.addActionListener(e -> controller.actionNextBtn());

        getRootFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.actionWindowClosing();
            }
        });
    }

    private class ThreadInitMinWindow extends Thread {
        @SneakyThrows
        @Override
        public void run() {
            System.out.println("Start init Main Window...<");
            mainWindow = FrameDialogFactory.getInstance().createJFrame(PanelType.MAIN_WINDOW);
            threadProcess.interrupt();
            System.out.println("mainWindow created is success");
        }
    }

    private class ThreadProcessBar extends Thread {
        private final RandomGenerator generator = RandomGeneratorFactory.getDefault().create();

        //https://coderanch.com/t/501538/java/repainting-JFrame
        @Override
        public void run() {
            System.out.println("Launch process bar loading");
            progressBar.setVisible(true);
            btnNext.setVisible(false);
            btnExit.setVisible(false);
            fill();
            frame.setVisible(false);
            mainWindow.setVisible(true);
            System.out.println("process bar is filling");
        }

        @SneakyThrows
        public void fill() {
            int i = 0;
            try {
                while (mainWindow == null) {
                    Thread.sleep(200);
                    progressBar.setValue(i);
                    i += generator.nextInt(10);
                }
            } catch (InterruptedException ignored) {
            }
            progressBar.setValue(500);
            Thread.sleep(500);
        }
    }

}
