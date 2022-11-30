package bntu.fitr.gorbachev.ticketsgenerator.main.test;
// Java Program to create a
// simple Progress bar

import bntu.fitr.gorbachev.ticketsgenerator.main.panels.tools.FileNames;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Progress extends JFrame {

    // create a frame
    static JFrame f;

    static JProgressBar b;

    public static void main(String[] args) {


        // create a frame
        f = new JFrame("ProgressBar demo");

        // create a panel
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        // create a progressbar
        b = new JProgressBar();

        // set initial value
        b.setValue(0);

        b.setStringPainted(true);

        // add progressbar
        System.out.println(FileNames.getResource(FileNames.spinnerLoaderIcon));
        ImageIcon icon = new ImageIcon(FileNames.getResource(FileNames.spinnerLoaderIcon));
        JLabel lbl = new JLabel(icon);
        p.add(lbl);

        // add panel
        f.add(p);

        // set the size of the frame
        f.setSize(500, 500);
        f.setVisible(true);

    }

    // function to increase Progress
    public static void fill() {
        int i = 0;
        try {
            while (i <= 100) {
                // fill the menu bar
                b.setValue(i + 10);

                // delay the thread
                Thread.sleep(1000);
                i += 20;
            }
        } catch (Exception e) {
        }
    }

}

