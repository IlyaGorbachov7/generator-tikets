package main;

import frames.impl.LaunchFrame;
import panels.PanelType;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new LaunchFrame(PanelType.SPLASH_SCREEN);
        frame.setVisible(true);
    }
}
