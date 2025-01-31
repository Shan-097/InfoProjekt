package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;


public class StartingPanel extends JPanel implements Runnable{   
    private final int buttonWidth = 100;
    private final int buttonHeight = 30;
    private final int frameRate;

    private JButton startNewGame;
    private JButton changeHotkeys;
    private JButton loadGame;
    private Thread thread;

    public StartingPanel(int pFR){
        frameRate = pFR;
        this.setLayout(null);
        this.setPreferredSize(null);
        this.setDoubleBuffered(true);
        this.setOpaque(true);
        startNewGame = new JButton("Create new world");
        startNewGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                App.loadGameScreen();
                thread = null;
            }
        });
        changeHotkeys = new JButton("Change hotkeys");
        loadGame = new JButton("load existing world");
        add(startNewGame);
        add(changeHotkeys);
        add(loadGame);
    }

    public void moveButtons(){
        startNewGame.setBounds((this.getWidth() - buttonWidth) / 2, (this.getHeight() - buttonHeight) / 2 - 40, buttonWidth, buttonHeight);
        changeHotkeys.setBounds((this.getWidth() - buttonWidth) / 2, (this.getHeight() - buttonHeight) / 2, buttonWidth, buttonHeight);
        loadGame.setBounds((this.getWidth() - buttonWidth) / 2, (this.getHeight() - buttonHeight) / 2 + 40, buttonWidth, buttonHeight);
        this.validate();
        this.setVisible(true);
    }

    public void startGameThread() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        double frameDisplayTime = 1000000000 / frameRate;
        long lastTime = System.nanoTime();
        long currentTime;
        double delta = 0;

        while (thread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / frameDisplayTime;
            lastTime = currentTime;
            if(delta >= 1){
                moveButtons();
                delta--;
            }
        }
    }
}