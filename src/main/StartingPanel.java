package main;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import javax.imageio.ImageIO;

import java.io.File;

import util.Music;

/**
 * The class of the starting panel.<br>
 * Controls the behavior of the window and the buttons in it.
 */
public class StartingPanel extends JPanel implements Runnable {
    /**
     * generic width of the buttons in the starting menu
     */
    private final int buttonWidth = 200;

    /**
     * generic height of the buttons in the starting menu
     */
    private final int buttonHeight = 50;

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
    private Image imgBackground;

    /**
     * Constructor of the starting panel.
     * Instantiates the buttons, adds listeners to them and
     * sets important values of the frame.
     * 
     * @param pFR the desired frame rate of the starting menu
     */
    protected StartingPanel(int pFR) {
        frameRate = pFR;
        this.setLayout(null);
        this.setPreferredSize(null);
        this.setDoubleBuffered(true);
        this.setOpaque(false);

        Image imgButton1;
        Image imgButton2;
        Image imgButton3;

        startNewGame = new JButton();
        loadGame = new JButton();
        controls = new JButton();

        try {
            imgButton1 = ImageIO.read(new File("./Graphics/button1.png"));
            imgButton1 = imgButton1.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_DEFAULT);
            imgButton2 = ImageIO.read(new File("./Graphics/button2.png"));
            imgButton2 = imgButton2.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_DEFAULT);
            imgButton3 = ImageIO.read(new File("./Graphics/button3.png"));
            imgButton3 = imgButton3.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_DEFAULT);

            startNewGame.setIcon(new ImageIcon(imgButton1));
            loadGame.setIcon(new ImageIcon(imgButton2));
            controls.setIcon(new ImageIcon(imgButton3));

            imgBackground = ImageIO.read(new File("./Graphics/hintergrundbild(latest).png"));
            imgBackground = imgBackground.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT);
            // this.drawImage(imgBackground);
        } catch (Exception ex) {
            System.out.println(ex);
        }

        startNewGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: Implement a way to create world with custom size and name.
                App.loadGameScreen(null);
            }
        });
        loadGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                App.loadLoadGameScreen();
            }
        });
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

        Music.stopMusic();
        Music.LoopMusic("./Music/mainmenu.wav");
    }

    /**
     * Updates the postion of the buttons so that they are centered and have the
     * correct height and width.
     */
    private void moveButtons() {
        startNewGame.setBounds((this.getWidth() - buttonWidth) / 2,
                (int) ((this.getHeight() - buttonHeight) / 2 - 1.1 * buttonHeight), buttonWidth, buttonHeight);
        controls.setBounds((this.getWidth() - buttonWidth) / 2,
                (int) ((this.getHeight() - buttonHeight) / 2 + 1.1 * buttonHeight), buttonWidth, buttonHeight);
        loadGame.setBounds((this.getWidth() - buttonWidth) / 2, (this.getHeight() - buttonHeight) / 2, buttonWidth,
                buttonHeight);
        this.validate();
        this.setVisible(true);
    }

    /**
     * Starts the thread to keep updating the window.
     */
    protected void startGameThread() {
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

    /**
     * to be done
     * 
     * @param g to be done
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Call this first!

        Graphics2D g2d = (Graphics2D) g;

        if (imgBackground != null) {
            g2d.drawImage(imgBackground, 0, 0, getWidth(), getHeight(), null);
        }
    }
}