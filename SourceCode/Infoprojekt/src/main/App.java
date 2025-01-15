package main;

import javax.swing.JFrame;

import game.GamePanel;

public class App {
    public static void main(String[] args) throws Exception {
        JFrame window = new JFrame("some name");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        loadStartingScreen(window);
    }

    private static void loadStartingScreen(JFrame window){
        StartingPanel startingPanel = new StartingPanel();
        window.add(startingPanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private static void loadGameScreen(JFrame window){
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        gamePanel.startGameThread();
    }
}
