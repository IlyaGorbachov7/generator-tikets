package bntu.fitr.gorbachev.ticketsgenerator.main;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.UniversityService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityDTO;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.factory.impl.ServiceFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.MyBasicComboBoxRenderer;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.MyJCompoBox;
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
import javax.swing.plaf.metal.MetalComboBoxUI;

public class Answer extends JFrame {

    UniversityService universityService = ServiceFactoryImpl.getInstance().universityService();


    public static final int MAXITEMS = 100;
    BorderLayout layout = new BorderLayout();
    JPanel panel = new JPanel(layout);
    MyJCompoBox<UniversityDTO> box = new MyJCompoBox<>();
    DefaultComboBoxModel<UniversityDTO> model = ((DefaultComboBoxModel<UniversityDTO>) box.getModel());

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


//        box.setSelectedIndex(-1);
        box.setRenderer(new MyBasicComboBoxRenderer((DefaultComboBoxModel<UniversityDTO>) box.getModel()));

        box.setEditor(new MyMetalComboBoxEditor());
        var model = ((DefaultComboBoxModel<UniversityDTO>) box.getModel());
        var jList = box.getJList();

        JTextField editorCom = (JTextField) box.getEditor().getEditorComponent();

        editorCom.addKeyListener(new KeyListener() {
            boolean keyPressedCtrl;

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (!keyPressedCtrl) {
                    keyPressedCtrl = KeyEvent.getKeyText(e.getKeyCode()).equals("Ctrl");
                }
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
                    || e.getKeyCode() == KeyEvent.VK_BACK_SPACE
                    || e.getKeyChar() == 'ё' || e.getKeyChar() == 'б' || e.getKeyChar() == 'ю' || e.getKeyChar() == 'х'
                    || e.getKeyChar() == 'ъ' || e.getKeyChar() == 'э' || e.getKeyChar() == 'ж') {

                    model.removeAllElements();
                    System.out.println("+++++++++++++++++++++++++++++++++++++");
                    System.out.println("search text: " + editorCom.getText());
                    var list = universityService.getByLikeName(editorCom.getText());
                    System.out.println(list.stream().map(UniversityDTO::getName).collect(Collectors.joining("\n")));
                    System.out.println(" - -- - -- - --  -- - - - - - -  -- -");
                    model.addAll(list);
                    box.setMaximumRowCount(Math.min(list.size(), 5));
                    box.showPopup();
                    if (box.getModel().getSize() > 0) {
//                        jList.setSelectedIndex(0);
                        jList.setSelectedIndex(0);
                    }
//                    editorCom.select();
//                    editorCom.requestFocus();
                }

                if (keyPressedCtrl && e.getKeyCode() == KeyEvent.VK_SPACE) {
                    box.showPopup();
                }

                if (keyPressedCtrl && KeyEvent.getKeyText(e.getKeyCode()).equals("Ctrl")) {
                    keyPressedCtrl = false;
                    if (box.getModel().getSize() > 0) {
                        jList.setSelectedIndex(0);
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    box.hidePopup();
                }
                if (box.getModel().getSize() <= 0) {
                    box.hidePopup();

                }

            }
        });

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