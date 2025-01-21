package main;

import java.awt.Dimension;

import javax.swing.JFrame;

import game.GamePanel;

public class App {
    private static JFrame window;
    private final static int frameRate = 60;

    public static void main(String[] args) {
        loadStartingScreen();
    }

    public static void loadStartingScreen(){
        window = new JFrame("some name");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        StartingPanel startingPanel = new StartingPanel(frameRate);
        
        window.setContentPane(startingPanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setMinimumSize(new Dimension(500, 300));//random values, TO DO: choose better
        startingPanel.startGameThread();
    }

    public static void loadGameScreen(){
        window.dispose();
        window = new JFrame("some name");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setMinimumSize(new Dimension(500, 300));//random values, TO DO: choose better
        gamePanel.startGameThread();
    }
}
