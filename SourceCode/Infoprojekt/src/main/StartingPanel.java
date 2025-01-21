package main;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;


public class StartingPanel extends JPanel implements Runnable{   
    private final int buttonWidth = 230;
    private final int buttonHeight = 35;
    private final int frameRate;

    JButton startNewGame;
    JButton controls;
    JButton loadGame;
    Thread thread;

    public StartingPanel(int pFR){
        frameRate = pFR;
        this.setLayout(null);
        this.setPreferredSize(null);
        this.setDoubleBuffered(true);
        this.setOpaque(true);
        startNewGame = new JButton("Create New World");
        startNewGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                App.loadGameScreen();
            }
        });
        controls = new JButton("Controls");
        loadGame = new JButton("Load Existing World");
        startNewGame.setFont(new Font("Arial", Font.BOLD, 15));
        loadGame.setFont(new Font("Arial", Font.BOLD, 15));
        controls.setFont(new Font("Arial", Font.BOLD, 15));
        add(startNewGame);
        add(controls);
        add(loadGame);
    }

    public void moveButtons(){
        startNewGame.setBounds((this.getWidth() - buttonWidth) / 2, (this.getHeight() - buttonHeight) / 2 - 45, buttonWidth, buttonHeight);
        controls.setBounds((this.getWidth() - buttonWidth) / 2, (this.getHeight() - buttonHeight) / 2 + 45, buttonWidth, buttonHeight);
        loadGame.setBounds((this.getWidth() - buttonWidth) / 2, (this.getHeight() - buttonHeight) / 2, buttonWidth, buttonHeight);
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