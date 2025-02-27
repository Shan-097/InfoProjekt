package main;

import game.GamePanel;
import hotKey.HotKeyPanel;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Main class of the programm.
 * Starts the starting screen on launch.
 * Has methods to change screen e.g. when starting a new game.
 */
public class App {
    /**
     * JFrame object that holds the currently open window.
     */
    private static JFrame window = new JFrame();

    /**
     * Specifies the frame rate of the programm.
     */
    private final static int FRAME_RATE = 60;

    /**
     * The main method loads the game upon launch of the programm.
     * 
     * @param args is not used but necessary
     */
    public static void main(String[] args) {
        loadStartingScreen();
    }

    /**
     * Empty and unused constructor of App
     */
    public App() {
    }

    /**
     * loadStartingScreen sets the necessary values and creates
     * the window for and of the starting screen
     * closing the others in the process
     */
    public static void loadStartingScreen() {
        window.dispose();
        StartingPanel startingPanel = new StartingPanel(FRAME_RATE);
        setStandardProperties(startingPanel);
        startingPanel.startGameThread();
    }

    /**
     * to be done
     */
    public static void loadHotKeyScreen() {
        window.dispose();
        HotKeyPanel hotKeyPanel = new HotKeyPanel(FRAME_RATE);
        setStandardProperties(hotKeyPanel);
        hotKeyPanel.startGameThread();
    }

    /**
     * loadStartingScreen sets the necessary values and creates
     * the window for and of the starting screen
     * closing the others in the process
     */
    public static void loadGameScreen() {
        window.dispose();
        GamePanel gamePanel = new GamePanel(FRAME_RATE);
        setStandardProperties(gamePanel);
        gamePanel.startGameThread();
    }

    /**
     * to be done
     * 
     * @param panel to be done
     */
    private static void setStandardProperties(JPanel panel){
        window = new JFrame("Keria");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(panel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setMinimumSize(new Dimension(500, 300));// random values, TO DO: choose better
    }
}
