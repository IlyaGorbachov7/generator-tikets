package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.textfield;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class HintTextField extends JTextField implements FocusListener {

    private String hint;
    private boolean showingHint;
    private Color colorTxt;
    private Font fontTxt;
    private final Color transparentColorTxt = new Color(0, 0, 0, 63);
    private final Font hintFontTxt = new Font("DIALOG", Font.ITALIC, 13);

    // This is constructor very important because Intelige IDE used for assembling project and it's running
    public HintTextField() {
        this.showingHint = true;
        colorTxt = this.getForeground();
        fontTxt = this.getFont();

        this.setForeground(transparentColorTxt);
        this.setFont(hintFontTxt);
        super.addFocusListener(this);
    }

    public HintTextField(final String hint) {
        super(hint);
        this.hint = hint;
        this.showingHint = true;
        colorTxt = this.getForeground();
        fontTxt = this.getFont();

        this.setForeground(transparentColorTxt);
        this.setFont(hintFontTxt);
        super.addFocusListener(this);
    }


    @Override
    public void focusGained(FocusEvent e) {
        this.selectAll();
        if (this.getText().isEmpty()) {
            super.setText("");
            super.setForeground(colorTxt);
            super.setFont(fontTxt);
            showingHint = false;
        }
    }

    @Override
    public void setText(String t) {
        if (hint != null) {
            if (!t.isEmpty()) {
                focusGained(null);
            } else {
                super.setText("");
                focusLost(null);
                return;
            }
        }
        super.setText(t);
        if (hint == null) {
            hint = t;
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (this.getText().isEmpty()) {
            super.setText(hint);
            System.err.println(hint);
            super.setForeground(transparentColorTxt);
            super.setFont(hintFontTxt);
            showingHint = true;
        }
    }

    @Override
    public String getText() {
        return showingHint ? "" : super.getText();
    }
}