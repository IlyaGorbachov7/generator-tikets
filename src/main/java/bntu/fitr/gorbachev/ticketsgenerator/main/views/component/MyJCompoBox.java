package bntu.fitr.gorbachev.ticketsgenerator.main.views.component;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityDTO;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.factory.impl.ServiceFactoryImpl;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Vector;
import java.util.function.Function;
import java.util.function.Supplier;

public class MyJCompoBox extends JComboBox<Object> {
    @Getter
    private final MyComboBoxUI compoBoxUI = new MyComboBoxUI(this);

    private DefaultComboBoxModel<Object> model;

    @Getter
    private JList<Object> jList;

    @Getter
    private JTextField editorTextField;

    @Getter
    @Setter
    private Function<Object, String> mapper;

    @Setter
    @Getter
    private Supplier<List<?>> supplierListElem;

    @Builder
    private MyJCompoBox(Object[] source, Supplier<List<?>> supplierListElem, Function<Object, String> mapperViewElem) {
        super(source);
        this.mapper = mapperViewElem;
        this.supplierListElem = supplierListElem;
        initField();
        setUI(compoBoxUI);
    }

    private void initField() {
        this.setEnabled(true);
        this.setEditable(true);
        this.setRenderer(new MyBasicComboBoxRenderer(mapper));
        this.setEditor(new MyMetalComboBoxEditor(mapper));
        this.setMaximumRowCount(5);

        model = (DefaultComboBoxModel<Object>) super.getModel();
        jList = compoBoxUI.getJList();
        editorTextField = (JTextField) this.getEditor().getEditorComponent();
        initListener();
    }

    private void initListener() {
        editorTextField.addKeyListener(new KeyListener() {
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
                if (!isPopupVisible()
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
                    model.addAll(supplierListElem.get());
                    setMaximumRowCount(Math.min(model.getSize(), 5));
                    showPopup();
                }
                if (keyPressedCtrl && e.getKeyCode() == KeyEvent.VK_SPACE) {
                    showPopup();
                }
                if (keyPressedCtrl && KeyEvent.getKeyText(e.getKeyCode()).equals("Ctrl")) {
                    keyPressedCtrl = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    hidePopup();
                }
                if (getModel().getSize() <= 0) {
                    hidePopup();
                }

            }
        });

        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println(this.getEditor().getItem());
//                System.out.println(this.getEditor().getItem().getClass());
            }
        });

        this.getEditor().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println("-----------------------------------------");
//                this.setPopupVisible(false);

            }
        });

    }

}
