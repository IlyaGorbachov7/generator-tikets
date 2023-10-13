package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist;

import lombok.NoArgsConstructor;

import javax.swing.*;

@NoArgsConstructor
public class MyJList extends JList<String> {
    public MyJList(ListModel<String> dataModel) {
        super(dataModel);
    }

    public MyJList(String[] listData) {
        super(listData);
    }
}
