package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.combobox;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class MyJCompoBox extends JComboBox<Object> {
    @Getter
    private MyComboBoxUI compoBoxUI = new MyComboBoxUI(this);

    private DefaultComboBoxModel<Object> model;

    @Getter
    private JTextField editorTextField;

    @Getter
    @Setter
    private Function<Object, String> mapper;

    @Setter
    @Getter
    private Function<String, List<?>> supplierListElem;


    @Builder
    private MyJCompoBox(Object[] source, Function<String,List<?>> supplierListElem, Function<Object, String> mapperViewElem) {
        super(source);
        this.mapper = mapperViewElem;
        this.supplierListElem = supplierListElem;
        setUI(compoBoxUI);
        initField();
        config();
    }

    private void initField() {
        this.setEnabled(true);
        this.setEditable(true);
        this.setRenderer(new MyBasicComboBoxRenderer(mapper));
        this.setEditor(new MyMetalComboBoxEditor(mapper));
        model = (DefaultComboBoxModel<Object>) super.getModel();
        editorTextField = (JTextField) this.getEditor().getEditorComponent();
        initListener();
    }

    private void config() {
        setMaximumRowCount(Math.min(model.getSize(), 5));
        this.setSelectedIndex(-1);
        this.getEditorTextField().setText("");
    }

    private void initListener() {
        editorTextField.addKeyListener(new KeyAdapter() {
            boolean keyPressedCtrl;

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

                    updateDropDownList();
                    showPopup();
                }
                if (keyPressedCtrl && e.getKeyCode() == KeyEvent.VK_SPACE) {
                    showPopup();
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    MyJCompoBox.this.setSelectedItem(null);
                }
                if (keyPressedCtrl && KeyEvent.getKeyText(e.getKeyCode()).equals("Ctrl")) {
                    keyPressedCtrl = false;
                }
                if (getModel().getSize() <= 0) {
                    hidePopup();
                }

            }
        });

        editorTextField.addKeyListener(this.compoBoxUI.createKeyListener());

        this.addMouseListener(new MouseAdapter() {
        });
    }

    protected void updateDropDownList() {
        model.removeAllElements();
        model.addAll(supplierListElem.apply(editorTextField.getText()));
        setMaximumRowCount(Math.min(model.getSize(), 5));
    }

}
