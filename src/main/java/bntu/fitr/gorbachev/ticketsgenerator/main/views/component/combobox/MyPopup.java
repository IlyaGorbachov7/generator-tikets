package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.combobox;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.combobox.abservers.RelatedComponentEvent;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboPopup;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyPopup extends BasicComboPopup {
    /**
     * Constructs a new instance of {@code BasicComboPopup}.
     *
     * @param combo an instance of {@code JComboBox}
     */
    public MyPopup(JComboBox<Object> combo) {
        super(combo);
    }

    @Override
    protected MouseListener createMouseListener() {
        MouseListener listener = super.createMouseListener();
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.mouseClicked(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                listener.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                listener.mouseReleased(e);
                ((MyJCompoBox) comboBox).fireRelatedComponentListener(new RelatedComponentEvent(comboBox));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                listener.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                listener.mouseExited(e);
            }
        };
    }

    @Override
    protected MouseListener createListMouseListener() {
        MouseListener listener = super.createListMouseListener();
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.mouseClicked(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                listener.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                listener.mouseReleased(e);
                ((MyJCompoBox) comboBox).fireRelatedComponentListener(new RelatedComponentEvent(comboBox));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                listener.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                listener.mouseExited(e);
            }
        };
    }
}
