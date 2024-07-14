package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.textfield;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.AppThemeManager;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.ColorManager;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.ThemeChangerListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class HintTextField extends JTextField implements FocusListener, ThemeChangerListener {

    private String hint;
    private boolean showingHint;
    private Font fontTxt;
    private final Font hintFontTxt = new Font("DIALOG", Font.ITALIC, 13);

    {
        AppThemeManager.addThemeChangerListener(this);
    }

    // This is constructor very important because Intelige IDE used for assembling project and it's running
    public HintTextField() {
        this.showingHint = true;
        fontTxt = this.getFont();

        this.setForeground(ColorHint.TRANSPARENT_COLOR_TXT.getColor());
        this.setFont(hintFontTxt);
        super.addFocusListener(this);
    }

    public HintTextField(final String hint) {
        super(hint);
        this.hint = hint;
        this.showingHint = true;
        fontTxt = this.getFont();

        this.setForeground(ColorHint.TRANSPARENT_COLOR_TXT.getColor());
        this.setFont(hintFontTxt);
        super.addFocusListener(this);
    }


    @Override
    public void focusGained(FocusEvent e) {
        this.selectAll();
        if (this.getText().isEmpty()) {
            super.setText("");
            super.setForeground(ColorHint.SIMPLE_COLOR_TXT.getColor());
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
            super.setForeground(ColorHint.TRANSPARENT_COLOR_TXT.getColor());
            super.setFont(hintFontTxt);
            showingHint = true;
        }
    }

    @Override
    public String getText() {
        return showingHint ? "" : super.getText();
    }

    @Override
    public Component getComponent() {
        return null;
    }

    @Override
    public void updateComponent() {
        if (getText().isEmpty() || getText().equals(hint)) {
            super.setForeground(ColorHint.TRANSPARENT_COLOR_TXT.getColor());
        } else {
            super.setForeground(ColorHint.SIMPLE_COLOR_TXT.getColor());
        }
    }


    public enum ColorHint {
        SIMPLE_COLOR_TXT(() -> {
            // this UIManager toggle color in dependencies light/night mode application
            return UIManager.getColor("Button.default.foreground");
        }),

        TRANSPARENT_COLOR_TXT(() -> {
            // transparent color depend on the SIMPLE_COLOR. Constructor: Color(r,g,b, a),where a - argument transparency
            int r = SIMPLE_COLOR_TXT.getColor().getRed(),
                    g = SIMPLE_COLOR_TXT.getColor().getGreen(), b = SIMPLE_COLOR_TXT.getColor().getBlue();
            return new Color(r, g, b, 80);
        });
        private ColorManager colorManager;

        ColorHint(ColorManager colorManager) {
            this.colorManager = colorManager;
        }

        public Color getColor() {
            return colorManager.getColor();
        }
    }
}