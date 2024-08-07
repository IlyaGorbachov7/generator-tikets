package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.combobox;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.util.function.Function;

//TODO:: Как мы знаем при кажом commit-е произдайет автоматическая закрытие Session
//TODO:: то есть Connection-ы закрыты не будут, однако будет новый объект Session с пустым hash-ом - это приведет к лишним или
//TODO:: повторным запросом в базу данных, что замедлит работу программы, поэтому
//TODO:: Чтобы этого избежать НЕ НУЖНО закрывать сессию, то есть commit- не нужен. Но если мы не будет commit-ить, то изминения в
//TODO:: в базе данный мы не увидим. Поэтому нужно разделить операции на 2 типа, те которые нужно вносить изменение в БД -- это нужно commit-ить
//TODO:: которые не вносят никакие изменения -- такие не нужно commit-ить, так как может обратиться повторный раз.

public class MyBasicComboBoxRenderer extends BasicComboBoxRenderer {
    private final Function<Object, String> mapper;

    public MyBasicComboBoxRenderer(Function<Object, String> mapper) {
        this.mapper = mapper;
    }

    // TODO: You should will be fixing  color mode related with view item of list. black | light
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Icon) {
            setIcon((Icon) value);
        } else {
            setText((value == null) ? "" : mapper.apply(value));
        }
        return this;
    }


}


