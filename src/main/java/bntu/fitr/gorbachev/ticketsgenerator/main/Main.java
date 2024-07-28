package bntu.fitr.gorbachev.ticketsgenerator.main;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.FrameDialogFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.FrameType;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.PanelType;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.IOException;

@Slf4j
public class Main {

    // TODO: нужно сделать какой то мэнеджер  А не работать через Main класс
    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        try{
            TicketGeneratorUtil.init();
            FrameDialogFactory.getInstance().createJFrame(FrameType.SPLASH_SCREEN, PanelType.SPLASH_SCREEN).setVisible(true);
        }catch (Throwable ignored){
            ignored.printStackTrace();
        }
    }
    }
