package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
    private final int tileSize = 16;
    private int screenWidth;
    private int screenHeight;
    
    Thread gameThread;
    GameController gameController;

    public GamePanel(){
        this.setPreferredSize(new Dimension(1000,600));//random values, TO DO: choose better
        this.setDoubleBuffered(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        gameController = new GameController();
        while (gameThread != null) {
            //update
            gameController.update();
            //draw updated
            repaint();
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.BLUE);
        g2d.fillRect((this.getWidth() - tileSize) / 2, (this.getHeight() - tileSize) / 2, tileSize, tileSize);
        g2d.dispose();
    }

}