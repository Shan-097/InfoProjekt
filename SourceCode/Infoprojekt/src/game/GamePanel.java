package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
    private final int tileSize = 50;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int frameWidth;  
    int frameHeight;

    int numWidth; // number of squares on the screen 
    int numHeight;
    
    Thread gameThread;
    GameController gameController;

    public GamePanel(){
        this.setPreferredSize(new Dimension(1000,600)); //random values, TO DO: choose better
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

    int TileCenterX; // Coordinates of topleft corner of the player square
    int TileCenterY;

    int a = 50;// tile on which player is standing, center tile Field-coords
    int b = 50;

    int indexCurrentX;
    int indexCurrentY;

    char[][] field = testField();
    public void paintComponent(Graphics g){ // paint() oder paintComponent() ???
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;

        // here cuz this.getHeight only works in paint()
        frameWidth = this.getWidth(); 
        frameHeight = this.getHeight();

        numWidth = (int) frameWidth / tileSize + 3;
        numHeight = (int) frameHeight / tileSize + 3;

        TileCenterX = ((frameWidth - tileSize) / 2);
        TileCenterY = ((frameHeight - tileSize) / 2);

        for(int i = 0; i<numWidth; i++){
            for(int j = 0; j<numHeight; j++){
                indexCurrentX = a-(numWidth / 2)+(i*1);
                indexCurrentY = b-(numHeight / 2)+(j*1);

                if(indexCurrentX == a && indexCurrentY == b){
                    g2d.setColor(Color.YELLOW);
                }else if(field[indexCurrentX][indexCurrentY] == 'X'){
                    g2d.setColor(Color.BLUE);
                } else {
                    g2d.setColor(Color.GRAY);
                }
                g2d.fillRect((TileCenterX-(tileSize*(a-indexCurrentX))), (TileCenterY-(tileSize*(b-indexCurrentY))), tileSize, tileSize); //find location of the square relative to player square
            }
        } 
        g2d.dispose();
    }
    
    public char[][] testField(){ // 2D array with checkerboard pattern to test the paint method
        int rows = 100; 
        int cols = 100; 
        char[][] field = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                field[i][j] = (i + j) % 2 == 0 ? 'X' : 'O';
            }
        }        
        return field;
    }
}