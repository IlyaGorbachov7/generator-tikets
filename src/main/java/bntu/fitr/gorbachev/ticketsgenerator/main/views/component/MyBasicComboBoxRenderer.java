package bntu.fitr.gorbachev.ticketsgenerator.main.views.component;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.SpecializationDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.UniversityDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.UniversityService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl.SpecializationDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityDTO;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.factory.impl.ServiceFactoryImpl;
import lombok.Getter;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.util.List;

//TODO:: Как мы знаем при кажом commit-е произдайет автоматическая закрытие Session
//TODO:: то есть Connection-ы закрыты не будут, однако будет новый объект Session с пустым hash-ом - это приведет к лишним или
//TODO:: повторным запросом в базу данных, что замедлит работу программы, поэтому
//TODO:: Чтобы этого избежать НЕ НУЖНО закрывать сессию, то есть commit- не нужен. Но если мы не будет commit-ить, то изминения в
//TODO:: в базе данный мы не увидим. Поэтому нужно разделить операции на 2 типа, те которые нужно вносить изменение в БД -- это нужно commit-ить
//TODO:: которые не вносят никакие изменения -- такие не нужно commit-ить, так как может обратиться повторный раз.

public class MyBasicComboBoxRenderer extends BasicComboBoxRenderer {
    UniversityService universityService = ServiceFactoryImpl.getInstance().universityService();
    DefaultComboBoxModel<UniversityDTO> model;

    public MyBasicComboBoxRenderer(DefaultComboBoxModel<UniversityDTO> model) {
        this.model = model;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (isSelected) {
            setBackground(Color.LIGHT_GRAY);
            setForeground(Color.BLACK);
        }else{
            setBackground(Color.white);
            setForeground(Color.BLACK);
        }

        setFont(list.getFont());

        if (value instanceof Icon) {
            setIcon((Icon) value);
        } else {
            setText((value == null) ? "" : ((UniversityDTO) value).getName());
        }
        return this;
    }

    private JList<? extends UniversityDTO> defineUniversityDto(JList<?> list) {
        try {
            return (JList<? extends UniversityDTO>) list;
        } catch (ClassCastException ex) {
            return null;
        }
    }

    private JList<? extends FacultyDto> defineFacultyDto(JList<?> list) {
        try {
            return (JList<? extends FacultyDto>) list;
        } catch (ClassCastException ex) {
            return null;
        }
    }

    private JList<? extends DepartmentDto> defineDepartmentDto(JList<?> list) {
        try {
            return (JList<? extends DepartmentDto>) list;
        } catch (ClassCastException ex) {
            return null;
        }
    }

    private JList<? extends SpecializationDto> defineSpecializationDto(JList<?> list) {
        try {
            return (JList<? extends SpecializationDto>) list;
        } catch (ClassCastException ex) {
            return null;
        }
    }
}


