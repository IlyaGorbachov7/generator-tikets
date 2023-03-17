package bntu.fitr.gorbachev.ticketsgenerator.main.views.component;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.SpecializationDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.UniversityDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl.SpecializationDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityDTO;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;

public class MyBasicComboBoxRenderer extends BasicComboBoxRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//        System.out.println("++++++++++++++++");
//        System.out.println(value);
//        System.out.println("index: " + index);
//        System.out.println("isSelected: " + isSelected);
//        System.out.println("cellHasFocus: " + cellHasFocus);

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
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
