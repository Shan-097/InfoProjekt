package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * to be done
 */
public class CreateGamePanel extends JPanel implements Runnable {
    /**
     * to be done
     */
    private final int frameRate;

    /**
     * to be done
     */
    private JTextField worldNameTextField;

    /**
     * to be done
     */
    private JButton createNewGame;

    /**
     * to be done
     */
    private Thread thread;

    public CreateGamePanel(int pFR) {
        frameRate = pFR;
        thread = null;

        worldNameTextField = new JTextField(20);
        add(worldNameTextField);

        createNewGame = new JButton("Create new Game");
        createNewGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!"".equals(worldNameTextField.getText())) {
                    App.loadGameScreen(null, worldNameTextField.getText());
                }
            }
        });
        add(createNewGame);
    }

    /**
     * Executed upon starting the thread.
     * Starts the game loop.
     * Controls the maximum frame rate and calls @moveButtons to update their
     * position.
     */
    @Override
    public void run() {
        // TODO: Is this method even executed?
        double frameDisplayTime = 1000000000 / frameRate;
        long lastTime = System.nanoTime();
        long currentTime;
        double delta = 0;

        while (thread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / frameDisplayTime;
            lastTime = currentTime;
            if (delta >= 1) {
                repaint();
                delta--;
            }
        }
    }
}
