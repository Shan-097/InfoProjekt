package hotKey;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.App;

public class HotKeyPanel extends JPanel implements Runnable {
    /**
     * generic width of the buttons in the starting menu
     */
    private final int buttonWidth = 230;

    /**
     * generic height of the buttons in the starting menu
     */
    private final int buttonHeight = 35;

    /**
     * desired frame rate of the game, it can't be higher but can drop under load
     */
    private final int frameRate;

    /**
     * button to return to main menu
     */
    private JButton returnToMainMenu;

    /**
     * to be done
     */
    private HashMap<String, JButton> buttons;

    /**
     * to be done
     */
    private HashMap<Integer, JTextField> actions;

    /**
     * thread that keeps the game running
     */
    private Thread thread;

    /**
     * Constructor of the starting panel.
     * Instantiates the buttons, adds listeners to them and
     * sets important values of the frame.
     * 
     * @param pFR the desired frame rate of the starting menu
     */
    public HotKeyPanel(int pFR) {
        frameRate = pFR;
        this.setLayout(null);
        this.setPreferredSize(null);
        this.setDoubleBuffered(true);
        this.setOpaque(true);
        returnToMainMenu = new JButton("return to main menu");
        returnToMainMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                App.loadStartingScreen();
                thread = null;
            }
        });
        returnToMainMenu.setFont(new Font("Arial", Font.BOLD, 15));

        actions.put(0, new JTextField("move up:"));

        add(returnToMainMenu);
    }

    /**
     * Updates the postion of the buttons so that they are centered and have the
     * correct height and width.
     */
    public void moveGraphicElements() {
        returnToMainMenu.setBounds(10, 10, buttonWidth, buttonHeight);
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
     * Controls the maximum frame rate and calls @moveButtons to update their
     * position.
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
            if (delta >= 1) {
                repaint();
                moveGraphicElements();
                delta--;
            }
        }
    }

    /**
     * to be done
     * 
     * @param g to be done
     */
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.dispose();
    }
}