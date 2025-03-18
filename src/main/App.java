package main;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Dimension;

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
     * loadStartingScreen sets the necessary values and creates the window of and
     * for the starting screen closing the others in the process.
     */
    protected static void loadStartingScreen() {
        StartingPanel startingPanel = new StartingPanel(FRAME_RATE);
        setStandardProperties(startingPanel);

        window.setMinimumSize(new Dimension(600, 500));
        startingPanel.startGameThread();
    }

    /**
     * loadLoadGameScreen sets the necessary values and creates the window of and
     * for the load game screen closing the others in the process.
     */
    protected static void loadLoadGameScreen() {
        LoadGamePanel loadGamePanel = new LoadGamePanel(FRAME_RATE);
        setStandardProperties(loadGamePanel);

        // random values, TODO: choose better
        window.setMinimumSize(new Dimension(500, 300));
    }

    /**
     * loadHotKeyScreen sets the necessary values and creates the window of and for
     * the hot key screen closing the others in the process.
     */
    protected static void loadHotKeyScreen() {
        HotKeyPanel hotKeyPanel = new HotKeyPanel(FRAME_RATE);
        setStandardProperties(hotKeyPanel);

        // random values, TODO: choose better
        window.setMinimumSize(new Dimension(500, 300));
        hotKeyPanel.startGameThread();
    }

    /**
     * loadGameScreen sets the necessary values and creates the window of and for
     * the game screen closing the others in the process.
     */
    protected static void loadGameScreen(String worldPath) {
        GamePanel gamePanel = new GamePanel(FRAME_RATE, worldPath);
        setStandardProperties(gamePanel);

        window.setMinimumSize(new Dimension(1000, 600));
        gamePanel.startGameThread();
    }

    /**
     * Sets the standard properties of the window and adds the given panel.
     * 
     * @param panel The Panel to be added to the frame
     */
    private static void setStandardProperties(JPanel panel) {
        window.dispose();
        window = new JFrame("Keria");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(panel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}
