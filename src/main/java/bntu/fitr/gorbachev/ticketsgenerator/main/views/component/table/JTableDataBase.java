package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table;

import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.Vector;

@Builder
@AllArgsConstructor
public class JTableDataBase extends JTable {
    Class<?> classTableView;

    public JTableDataBase() {
        setBackground(new Color(225, 223, 223, 255));
    }
}
