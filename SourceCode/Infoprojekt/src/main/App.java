package main;

import java.awt.Dimension;

import javax.swing.JFrame;

import game.GamePanel;

public class App {
    final static int frameRate = 60;

    public static void main(String[] args) throws Exception {
        JFrame window = new JFrame("some name");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loadStartingScreen(window);
    }

    private static void loadStartingScreen(JFrame window){
        StartingPanel startingPanel = new StartingPanel(frameRate);
        
        window.setContentPane(startingPanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setMinimumSize(new Dimension(500, 300));//random values, TO DO: choose better
        startingPanel.startGameThread();
    }

    private static void loadGameScreen(JFrame window){
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
