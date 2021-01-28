package edu.cmu.cs.cs214.hw4.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.Component;
import java.awt.Dimension;

/**
 * calss with main function, generate new game here
 */
public class CarcassonneUI {
    private static final String TITLE = "Carcassonne by Fangxian_Mi";
    private static final int FRAME_WIDTH = 500;
    private static final int FRAME_HEIGHT = 100;

    /**
     * main function, to control the game's initialization
     * @param args null
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(TITLE);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
            PlayersCreationClient t = new PlayersCreationClient(frame);
            frame.add(t);
            frame.pack();
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }
    static void showDialog(Component component, String title, String message) {
        JOptionPane.showMessageDialog(component, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}