package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.combobox;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.combobox.abservers.RelatedComponentEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.combobox.abservers.RelatedComponentListener;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MyJCompoBox extends JComboBox<Object> {
    @Getter
    private final MyComboBoxUI compoBoxUI = new MyComboBoxUI(this);

    private DefaultComboBoxModel<Object> model;

    @Getter
    private JTextField editorTextField;

    @Getter
    @Setter
    private Function<Object, String> mapper;

    @Setter
    @Getter
    private Function<String, List<?>> supplierListElem;

    @Getter
    private JList<?> jList;

    @Getter
    JButton arrowButton;

    @Getter
    ComboPopup popup;

    List<RelatedComponentListener> subscribersRelatedComponent = new ArrayList<>();

    @Builder
    private MyJCompoBox(Function<String, List<?>> supplierListElem, Function<Object, String> mapperViewElem) {
        super(new Object[0]);
        this.mapper = mapperViewElem;
        this.supplierListElem = supplierListElem;
        setUI(compoBoxUI);
        initField();
        initListener();
        config();
    }

    private void initField() {
        this.setEnabled(true);
        this.setEditable(true);
        this.setRenderer(new MyBasicComboBoxRenderer(mapper));
        this.setEditor(new MyMetalComboBoxEditor(mapper));
        model = (DefaultComboBoxModel<Object>) super.getModel();
        editorTextField = (JTextField) this.getEditor().getEditorComponent();
        arrowButton = compoBoxUI.getArrowButton();
        popup = compoBoxUI.getPopup();
        jList = popup.getList();
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
                    selectItemListSimilar();
                    fireRelatedComponentListener(new RelatedComponentEvent(MyJCompoBox.this));

                }
                if (getModel().getSize() <= 0) {
                    if (popup.isVisible()) {
                        hidePopup();
                    }
                }
                if (keyPressedCtrl && e.getKeyCode() == KeyEvent.VK_SPACE) {
                    updateDropDownList();
                    showPopup();
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    MyJCompoBox.this.setSelectedItem(null);
                    ((MyMetalComboBoxEditor) getEditor()).setRealValue();
                }
                if (keyPressedCtrl && KeyEvent.getKeyText(e.getKeyCode()).equals("Ctrl")) {
                    keyPressedCtrl = false;
                }

            }
        });

        editorTextField.addKeyListener(this.compoBoxUI.createKeyListener());

        arrowButton.removeMouseListener(popup.getMouseListener());

        arrowButton.addActionListener((e) -> {
            updateDropDownList();
            if (model.getSize() > 0) {
                // toggle to show/hide dropList
                popup.getMouseListener().mousePressed(new MouseEvent((Component) e.getSource(), 1, 0, InputEvent.BUTTON1_DOWN_MASK, 1, 1, 1, false));
            }
        });
    }

    private void selectItemListSimilar() {
        if (model.getSize() > 0) {
            Optional<Object> similarItem = IntStream.range(0, model.getSize())
                    .mapToObj(model::getElementAt)
                    .min((elem, elem2) -> {
                        String name = mapper.apply(elem);
                        String name2 = mapper.apply(elem2);
                        return Integer.min(name.length(), name2.length());
                    });

            int indexOfSimilarItem = model.getIndexOf(similarItem.orElseThrow());
            jList.setSelectedIndex(indexOfSimilarItem);
            ((MyMetalComboBoxEditor) getEditor()).setItemByPassedKeyEnter(jList.getSelectedValue());
        }
    }

    public void updateDropDownList() {
        model.removeAllElements();
        model.addAll(supplierListElem.apply(editorTextField.getText()));
        setMaximumRowCount(Math.min(model.getSize(), 5));
    }

    public void enableElements(Element elem, boolean enable) {
        switch (elem) {
            case ARROW_BUTTON -> this.getArrowButton().setEnabled(enable);
            case TEXT_FIELD -> this.getEditorTextField().setEnabled(enable);
            case ALL -> this.setEnabled(enable);
        }
    }

    // -------------------- Listeners -----------------------------
    public void addRelatedComponentListener(RelatedComponentListener listener) {
        subscribersRelatedComponent.add(listener);
    }

    public void removeRelatedComponentListener(RelatedComponentListener listener) {
        subscribersRelatedComponent.remove(listener);
    }

    public void fireRelatedComponentListener(RelatedComponentEvent event) {
        subscribersRelatedComponent.forEach((handler) -> {
            handler.perform(event);
        });
    }

    public enum Element {
        ARROW_BUTTON,
        TEXT_FIELD,
        ALL
    }
}
