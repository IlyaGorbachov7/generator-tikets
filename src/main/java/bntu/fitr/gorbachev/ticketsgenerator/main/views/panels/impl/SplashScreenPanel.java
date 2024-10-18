package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.impl;


import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.poolcon.ConnectionPoolException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.poolcon.PoolConnection;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.Localizer;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.LocalizerListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.FrameDialogFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.FrameType;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.BasePanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.PanelType;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.FileNames;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

/**
 * The class represent splash screen panel
 *
 * @author Gorbachev I. D.
 * @version 24.03.2022
 */
@Slf4j
public class SplashScreenPanel extends BasePanel implements LocalizerListener {

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
        String university = Localizer.get("panel.splashscreen.university");
        String faculty = Localizer.get("panel.splashscreen.faculty");
        String department = Localizer.get("panel.splashscreen.department");
        String infoDeveloper = Localizer.get("panel.splashscreen.infoDev", "panel.infoDev");
        String nameDeveloper = Localizer.get("panel.nameDev");
        String infoTeacher = Localizer.get("panel.splashscreen.infoTeacher");
        String nameTeacher = Localizer.get("panel.splashscreen.nameTeacher");
        lbUniversity = new JLabel(university);
        lbFaculty = new JLabel(faculty);
        lbDepartment = new JLabel(department);
        lbCoursework = new JLabel(Localizer.get("panel.splashscreen.typeWork"));
        lbDiscipline = new JLabel(Localizer.get("panel.splashscreen.discipline"));
        lbNameCoursework = new JLabel(Localizer.get("panel.splashscreen.appName"));
        labelIcon = new JLabel(new ImageIcon(Objects.requireNonNull(
                FileNames.getResource(FileNames.iconCoursework))));
        lbInfoDeveloper = new JLabel(infoDeveloper);
        lbNameDeveloper = new JLabel(nameDeveloper);
        lbInfoTeacher = new JLabel(infoTeacher);
        lbNameTeacher = new JLabel(nameTeacher);
        lbYear = new JLabel(Localizer.get("panel.splashscreen.locationData"));

        btnNext = new JButton(Localizer.get("btn.next"));
        btnExit = new JButton(Localizer.get("btn.exit"));

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

        JPanel miniPanels1 = new JPanel(new BorderLayout());
        miniPanels1.setBorder(new EmptyBorder(0, 0, 20, 0));
//        miniPanels1.setBackground(Color.MAGENTA);
        miniPanels1.add(lbYear, BorderLayout.CENTER);
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
//        panelBtn.setBackground(Color.green);
        JPanel cellBtn = new JPanel(new BorderLayout());
        cellBtn.add(btnNext);
        panelBtn.add(cellBtn);

        cellBtn = new JPanel(new BorderLayout());
        cellBtn.add(btnExit);
        panelBtn.add(cellBtn);
        miniPanels1.add(panelBtn, BorderLayout.SOUTH);

        setConfigComponents();
        this.add(miniPanels, BorderLayout.CENTER);
        this.add(miniPanels1, BorderLayout.SOUTH);
        this.add(progressBar, BorderLayout.NORTH);
    }

    /**
     * The method sets configuration all components bntu.fitr.gorbachev.ticketsgenerator.main.views.panels. This method invoked once panel generation
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

        btnNext.setBackground(UIManager.getColor("Component.focusColor"));
        btnNext.setPreferredSize(new Dimension(250, 24));
        btnExit.setPreferredSize(new Dimension(250, 24));

        progressBar.setVisible(false);
        progressBar.setPreferredSize(new Dimension(progressBar.getWidth(), 7));
        progressBar.setMinimum(0);
        progressBar.setMaximum(200);
    }

    private final Thread threadProcess;
    private final Thread threadInit;

    @Override
    public void onUpdateLocale(Locale selectedLocale) {

    }

    /**
     * The method sets necessary all components listeners
     */
    @Override
    public void setComponentsListeners() {
        btnExit.addActionListener(e -> System.exit(0));

        btnNext.addActionListener(e -> {
            thread.interrupt();
            threadProcess.start();
            threadInit.start();
        });

        getRootFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                log.trace("Frame application is closing");
                getRootFrame().setVisible(false);
                try {
                    PoolConnection.Builder.build().destroy();
                    threadProcess.interrupt();
                } catch (ConnectionPoolException ex) {
                    throw new RuntimeException(ex);
                } finally {
                    log.info("Application is closed");
                }
                /* There are cases when throw NullPointerException.
                 * This event happening, if this thread trying
                 * to destroy Connection Pool, that still doesn't initialize arrayQueue
                 *
                 * This event you can achieve way fasted closing SplashScreen frame
                 * before initialization connection pool
                 *
                 * However, probability this case very, very small, since the thread, that
                 * initialize connection pool will be
                 * running first and method destroyConnectionPool and initPool is synchronized
                 *
                 * If this event still happened, then this thread-handler throw don't handled exception
                 * Then Swing-Framework don't complete success this process.
                 * In the finally, frame Window don't closed*/
            }
        });
    }

    private class ThreadInitMinWindow extends Thread {
        @SneakyThrows
        @Override
        public void run() {
            log.info("Started initialization application");
            CompletableFuture.runAsync(PoolConnection.Builder::build);
            CompletableFuture.runAsync(() -> {
                mainWindow = FrameDialogFactory.getInstance().createJFrame(FrameType.MAIN_WINDOW, PanelType.MAIN_WINDOW);
                threadProcess.interrupt();
            });
        }
    }

    private class ThreadProcessBar extends Thread {
        private final RandomGenerator generator = RandomGeneratorFactory.getDefault().create();

        //https://coderanch.com/t/501538/java/repainting-JFrame
        @Override
        public void run() {
            log.trace("Launch process bar loading");
            progressBar.setVisible(true);
            btnNext.setVisible(false);
            btnExit.setVisible(false);
            fill();
            frame.setVisible(false);
            mainWindow.setVisible(true);
            log.trace("process bar is filling");
        }

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
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }
        }
    }

}
