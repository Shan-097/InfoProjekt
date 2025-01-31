package game;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;



/**
 * to be done
 */
public class GamePanel extends JPanel implements Runnable {
    /**
     * to be done
     */
    private final int tileSize = 16;

    /**
     * to be done
     */
    private final int frameRate;

    /**
     * to be done
     */
    private Thread gameThread;

    /**
     * to be done
     */
    private GameController gameController;



    /**
     * to be done
     */
    public GamePanel(int pFr) {
        this.setPreferredSize(new Dimension(1000, 600));// random values, TO DO: choose better
        this.setDoubleBuffered(true);
        frameRate = pFr;
    }


    /**
     * to be done
     */
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * to be done
     */
    @Override
    public void run() {
        double frameDisplayTime = 1000000000 / frameRate;
        long lastTime = System.nanoTime();
        long currentTime;
        double delta = 0;

        gameController = new GameController();
        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / frameDisplayTime;
            lastTime = currentTime;
            if (delta >= 1) {
                // update
                gameController.update();

                // draw updated
                repaint();
            }
        }
    }

    /** 
     * to be done
     * @param g to be done
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLUE);
        g2d.fillRect((this.getWidth() - tileSize) / 2, (this.getHeight() - tileSize) / 2, tileSize, tileSize);
        g2d.dispose();
    }
}