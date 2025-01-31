package main;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;



/**
 * The class of the starting panel.
 * Controls the behavior of the window and the buttons in it.
 */
public class StartingPanel extends JPanel implements Runnable{  
    /**
     * generic width of the buttons in the starting menu
     */
    private final int buttonWidth = 100;

    /**
     * generic height of the buttons in the starting menu
     */
    private final int buttonHeight = 30;

    /**
     * desired frame rate of the game, it can't be higher but can drop under load
     */
    private final int frameRate;

    /**
     * button to start a new game
     */
    private JButton startNewGame;

    /**
     * button to access the menu to change the key bindings
     */
    private JButton changeHotkeys;

    /**
     * button to open the menu to load an existing saved game
     */
    private JButton loadGame;

    /**
     * thread that keeps the game running
     */
    private Thread thread;



    /**
     * Constructor of the starting panel. 
     * Instantiates the buttons, adds listeners to them and 
     * sets important values of the frame.
     * @param pFR the desired frame rate of the starting menu
     */
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


    /**
     * Updates the postion of the buttons so that they are centered and have the correct height and width.
     */
    public void moveButtons(){
        startNewGame.setBounds((this.getWidth() - buttonWidth) / 2, (this.getHeight() - buttonHeight) / 2 - 40, buttonWidth, buttonHeight);
        changeHotkeys.setBounds((this.getWidth() - buttonWidth) / 2, (this.getHeight() - buttonHeight) / 2, buttonWidth, buttonHeight);
        loadGame.setBounds((this.getWidth() - buttonWidth) / 2, (this.getHeight() - buttonHeight) / 2 + 40, buttonWidth, buttonHeight);
        this.validate();
        this.setVisible(true);
    }

    /**
     * Starts the thread to keep updating the window.
     */
    public void startGameThread() {
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Executed upon starting the thread.
     * Starts the game loop.
     * Controls the maximum frame rate and calls @moveButtons to update their position.
     */
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