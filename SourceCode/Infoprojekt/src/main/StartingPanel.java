package main;

import game.GameController;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The class of the starting panel.<br>
 * Controls the behavior of the window and the buttons in it.
 */
public class StartingPanel extends JPanel implements Runnable {
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
     * button to start a new game
     */
    private JButton startNewGame;

    /**
     * button to access the menu to change the key bindings
     */
    private JButton controls;

    /**
     * button to open the menu to load an existing saved game
     */
    private JButton loadGame;

    /**
     * thread that keeps the game running
     */
    private Thread thread;

    /**
     * to be done
     */
    private BufferedImage myPicture;

    /**
     * Constructor of the starting panel.
     * Instantiates the buttons, adds listeners to them and
     * sets important values of the frame.
     * 
     * @param pFR the desired frame rate of the starting menu
     */
    public StartingPanel(int pFR) {
        frameRate = pFR;
        this.setLayout(null);
        this.setPreferredSize(null);
        this.setDoubleBuffered(true);
        this.setOpaque(true);
        startNewGame = new JButton("Create New World");
        startNewGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                App.loadGameScreen();
                thread = null;
            }
        });
        loadGame = new JButton("Load Saved World");
        loadGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GameController controller = new GameController();
                controller.loadWorld("./SourceCode/Infoprojekt/saves/testWorld.json");
            }
        });
        controls = new JButton("Controls");
        controls.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                App.loadHotKeyScreen();
            }
        });
        startNewGame.setFont(new Font("Arial", Font.BOLD, 15));
        loadGame.setFont(new Font("Arial", Font.BOLD, 15));
        controls.setFont(new Font("Arial", Font.BOLD, 15));
        add(startNewGame);
        add(controls);
        add(loadGame);
        try {
            myPicture = ImageIO.read(new File("./Graphics/grass (64x64).png"));
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            add(picLabel);
        } catch (IOException e1) {
            System.out.println("aaaaa");
            e1.printStackTrace();
        }
    }

    /**
     * Updates the postion of the buttons so that they are centered and have the
     * correct height and width.
     */
    public void moveButtons() {
        startNewGame.setBounds((this.getWidth() - buttonWidth) / 2, (this.getHeight() - buttonHeight) / 2 - 45,
                buttonWidth, buttonHeight);
        controls.setBounds((this.getWidth() - buttonWidth) / 2, (this.getHeight() - buttonHeight) / 2 + 45, buttonWidth,
                buttonHeight);
        loadGame.setBounds((this.getWidth() - buttonWidth) / 2, (this.getHeight() - buttonHeight) / 2, buttonWidth,
                buttonHeight);
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
                moveButtons();
                delta--;
            }
        }
    }
}