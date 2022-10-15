package panels.impl;


import frames.FrameDialogFactory;
import panels.BasePanel;
import panels.PanelType;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * The class represent splash screen panel
 *
 * @author Gorbachev I. D.
 * @version 24.03.2022
 */
public class SplashScreenPanel extends BasePanel {

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
    private final Window frame;
    private final Thread thread;

    private JFrame mainWindow;

    {
        String university = "Беларусский национальный технический университет";
        String faculty = "Факультет информационных технологий и робототехники";
        String department = "Кафедра программного обеспечения " +
                            "информационных систем и технологий";
        String infoDeveloper = "Выполнил: Студент группы 10702419";
        String nameDeveloper = "Горбачёв Илья Дмитриевич";
        String infoTeacher = "Преподаватель: к.ф.-м.н.,доц.";
        String nameTeacher = "Сидорик Валерий Владимирович";
        lbUniversity = new JLabel(university);
        lbFaculty = new JLabel(faculty);
        lbDepartment = new JLabel(department);
        lbCoursework = new JLabel("Курсовая работа");
        lbDiscipline = new JLabel("по дисциплине: " +
                                  "\"Программирование на языке Java\"");
        lbNameCoursework = new JLabel("Конструктор" +
                                      " экзаменационных билетов");
        labelIcon = new JLabel(new ImageIcon(Objects.requireNonNull(
                SplashScreenPanel.class.getResource("/resources/iconCoursework.png"))));
        lbInfoDeveloper = new JLabel(infoDeveloper);
        lbNameDeveloper = new JLabel(nameDeveloper);
        lbInfoTeacher = new JLabel(infoTeacher);
        lbNameTeacher = new JLabel(nameTeacher);
        lbYear = new JLabel("Минск, 2022");
        btnNext = new JButton("Далее");
        btnExit = new JButton("Выход");

        thread = new Thread(() -> {
            try {
                Thread.sleep(60_000);
                System.exit(0);
            } catch (InterruptedException ignored) {
            }
        });
    }

    /**
     * The constructor creates a panel
     *
     * @param frame frame contains created this  panel
     */
    public SplashScreenPanel(Window frame) {
        super(frame);
        this.frame = frame;
        mainWindow = FrameDialogFactory.getInstance().createJFrame(PanelType.MAIN_WINDOW);
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
        JPanel miniPanels1 = new JPanel(new GridLayout(2, 1));
        miniPanels1.add(lbYear);
        JPanel panelBtn = new JPanel(new GridLayout(1, 2, 5, 5));
        panelBtn.add(btnNext);
        btnNext.setBorder(BorderFactory.createRaisedBevelBorder());
        panelBtn.add(btnExit);
        miniPanels1.add(panelBtn);
        setConfigComponents();
        this.add(miniPanels, BorderLayout.NORTH);
        this.add(miniPanels1, BorderLayout.SOUTH);
    }

    /**
     * The method sets configuration all components panels. This method invoked once panel generation
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
    }

    /**
     * The method sets necessary all components listeners
     */
    @Override
    public void setComponentsListeners() {
        btnExit.addActionListener(e -> System.exit(0));

        btnNext.addActionListener(e -> {
            thread.interrupt();
            frame.setVisible(false);
            mainWindow.setVisible(true);
        });
    }

}
