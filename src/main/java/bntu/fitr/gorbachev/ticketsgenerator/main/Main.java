package bntu.fitr.gorbachev.ticketsgenerator.main;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.FrameDialogFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.FrameType;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.PanelType;

import javax.swing.*;

public class Main {

    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        try{
            Class.forName(TicketGeneratorUtil.class.getName());
            FrameDialogFactory.getInstance().createJFrame(FrameType.SPLASH_SCREEN, PanelType.SPLASH_SCREEN).setVisible(true);
        }catch (Throwable ignored){
            ignored.printStackTrace();
        }
    }
    }
