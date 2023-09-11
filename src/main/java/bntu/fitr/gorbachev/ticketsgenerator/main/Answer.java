package bntu.fitr.gorbachev.ticketsgenerator.main;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.UniversityService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityDTO;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.factory.impl.ServiceFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.combobox.MyJCompoBox;

import java.awt.*;

import javax.swing.*;

public class Answer extends JFrame {

    UniversityService universityService = ServiceFactoryImpl.getInstance().universityService();


    public static final int MAXITEMS = 100;
    BorderLayout layout = new BorderLayout();
    JPanel panel = new JPanel(layout);
    MyJCompoBox box;

    {
        box = MyJCompoBox.builder()
                .mapperViewElem((obj) -> ((UniversityDTO) obj).getName())
                .supplierListElem((textField) ->
                        ServiceFactoryImpl.getInstance().universityService().getByLikeName(textField)).build();
    }

    public Answer() {
        this.add(panel);
        panel.setPreferredSize(new Dimension(500, 300));
        panel.add(box, BorderLayout.NORTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        final Answer answer = new Answer();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                answer.pack();
                answer.setVisible(true);
            }
        });
    }

}