package bntu.fitr.gorbachev.ticketsgenerator.main;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.UniversityService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityDTO;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.factory.impl.ServiceFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.MyBasicComboBoxRenderer;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.MyMetalComboBoxEditor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.plaf.metal.MetalComboBoxEditor;

public class Answer extends JFrame {

    UniversityService universityService = ServiceFactoryImpl.getInstance().universityService();


    public static final int MAXITEMS = 100;
    BorderLayout layout = new BorderLayout();
    JPanel panel = new JPanel(layout);
    JComboBox<UniversityDTO> box = new JComboBox<>();

    public Answer() {
        this.add(panel);
        panel.setPreferredSize(new Dimension(500, 300));
        panel.add(box, BorderLayout.NORTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        universityService.getAll().forEach(box::addItem);

        box.setOpaque(true);
        box.addNotify();
//        box.setMaximumRowCount(3);
        box.setSelectedIndex(-1);
        box.setForeground(Color.RED);
        box.setBackground(Color.DARK_GRAY);
        box.setEnabled(true);

        box.setEditable(true);
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


//        box.setSelectedIndex(-1);
        box.setRenderer(new MyBasicComboBoxRenderer((DefaultComboBoxModel<UniversityDTO>) box.getModel()));

        box.setEditor(new MyMetalComboBoxEditor());
        var model = box.getModel();
        JTextField editorCom = (JTextField) box.getEditor().getEditorComponent();
//        editorCom.setBackground(Color.GREEN);
        editorCom.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
//                System.out.println(KeyEvent.getKeyText(e.getKeyCode()));
//                System.out.println(editorCom.getText());

            }

            @Override
            public void keyReleased(KeyEvent e) {
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

                    ((DefaultComboBoxModel<UniversityDTO>) box.getModel()).removeAllElements();
                    System.out.println("+++++++++++++++++++++++++++++++++++++");
                    System.out.println("search text: " + editorCom.getText());
                    var list = universityService.getByLikeName(editorCom.getText());
                    System.out.println(list.stream().map(UniversityDTO::getName).collect(Collectors.joining("\n")));
                    System.out.println(" - -- - -- - --  -- - - - - - -  -- -");
                    ((DefaultComboBoxModel<UniversityDTO>) box.getModel()).addAll(list);
//                editorCom.requestFocus();
//                editorCom.selectAll();
                    box.showPopup();
                }

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    box.hidePopup();
                }
                if (box.getModel().getSize() <= 0) {
                    box.hidePopup();
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