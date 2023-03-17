package bntu.fitr.gorbachev.ticketsgenerator.main;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityDTO;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.MyBasicComboBoxRenderer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxEditor;

public class Answer extends JFrame {
    public static final int MAXITEMS = 100;
    BorderLayout layout = new BorderLayout();
    JPanel panel = new JPanel(layout);
    JComboBox<UniversityDTO> box = new JComboBox<>();

    String[] myDataBase = {"Juby", "Jaz", "Jasmine", "Joggy", "one", "dog", "cat", "parot"};

    public Answer() {
        this.add(panel);
        panel.setPreferredSize(new Dimension(500, 300));
        panel.add(box,BorderLayout.NORTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Arrays.stream(myDataBase)
                .map(value -> UniversityDTO.builder().name(value).build())
                .forEach(box::addItem);

        box.setOpaque(true);
        box.addNotify();
        box.setMaximumRowCount(3);
        box.setForeground(Color.RED);
        box.setBackground(Color.DARK_GRAY);
        box.setEnabled(true);

        box.setEditable(true);
        System.out.println(box.getEditor().getItem());
        box.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println(box.getEditor().getItem());
//                System.out.println(box.getEditor().getItem().getClass());
            }
        });
        box.getEditor().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println("-----------------------------------------");
//                box.setPopupVisible(false);
            }
        });

        box.setRenderer(new MyBasicComboBoxRenderer());


        JTextField editorCom = (JTextField) box.getEditor().getEditorComponent();
//        editorCom.setBackground(Color.GREEN);
        editorCom.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
//                System.out.println(KeyEvent.getKeyText(e.getKeyCode()));
                if (!box.isPopupVisible()
                    && e.getKeyCode() >= KeyEvent.VK_COMMA && e.getKeyCode() <= KeyEvent.VK_SLASH
                    || e.getKeyCode() >= KeyEvent.VK_0 && e.getKeyCode() <= KeyEvent.VK_9
                    || e.getKeyCode() >= KeyEvent.VK_SEMICOLON && e.getKeyCode() <= KeyEvent.VK_EQUALS
                    || e.getKeyCode() >= KeyEvent.VK_A && e.getKeyCode() <= KeyEvent.VK_Z
                    || e.getKeyCode() >= KeyEvent.VK_OPEN_BRACKET && e.getKeyCode() <= KeyEvent.VK_CLOSE_BRACKET
                    || e.getKeyCode() == KeyEvent.VK_BACK_QUOTE
                    || e.getKeyCode() == KeyEvent.VK_QUOTE
                    || e.getKeyCode() == KeyEvent.VK_DELETE
                    || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    box.setPopupVisible(true);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    box.setPopupVisible(false);
                }
            }
        });

    }

    public static void main(String[] args) {
        final Answer answer = new Answer();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                answer.pack();
                answer.setVisible(true);
            }
        });
    }

}