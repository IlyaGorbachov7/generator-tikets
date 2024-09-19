package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.abservers;

import lombok.Builder;
import lombok.Getter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

@Builder
@Getter
public class   TableSelectedRowsEvent {
    private ListSelectionEvent eventSource;
    private Class<?> classTableView;
    private JButton btn;

    private Object[] selectedItems;
    private int[] selectedRows;
    private boolean isAdjusting;
}
