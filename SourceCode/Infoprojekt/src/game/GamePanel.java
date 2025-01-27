package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
    private final int tileSize = 64;
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

            // Movement prove of concept: player moves one square to the left every second
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //a -= 1;
            

            //draw updated
            repaint();
        }
    }
    int TileCenterX; // Coordinates of topleft corner of the player square
    int TileCenterY;

    int a = 50;// tile on which player is standing, center tile Field-coords
    int b = 50;

    int posXonTile = 0;
    int posYonTile = 0;

    // temp please ignore!!
    int aHelp = a-4;
    int bHelp = a-4;
    // temp

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

        for(int i = 0; i<numWidth; i++){ // TODO: add exception for border of world (arrayOutOfBounds)
            for(int j = 0; j<numHeight; j++){
                indexCurrentX = a-(numWidth / 2)+(i*1);
                indexCurrentY = b-(numHeight / 2)+(j*1);

                //if(indexCurrentX == a && indexCurrentY == b){
                //    g2d.setColor(Color.YELLOW); // highlight current player square yellow
                //} else 
                if(indexCurrentX == aHelp && indexCurrentY == bHelp) {
                    g2d.setColor(Color.BLUE); // paint random square blue to visualize movement
                } else if(field[indexCurrentX][indexCurrentY] == 'X'){
                    g2d.setColor(Color.WHITE);
                } else {
                    g2d.setColor(Color.GRAY);
                }
                // lagere die coords berechnung von fillRect aus, berechne mithilfe je 1/10 square, musst wissen wo TileCenter liegt (ändere Tilecenter je nachdem)
                // diagonal sollte auch (erweiterbar) möglich sein; free movement auf gesamten bildschirm; nur der hintergrund bewegt sich
                // int posXonTile/posYonTile von GameController (relativ zu TileCenterX/Y)

                g2d.fillRect((int)(TileCenterX-(tileSize*((a-indexCurrentX)*0.9))), (TileCenterY-(tileSize*(b-indexCurrentY))), tileSize, tileSize); //find location of the square relative to player square
            }
        } 
        // temp player dot
        g2d.setColor(Color.BLACK);
        g2d.fillOval((int)(TileCenterX + (0.125*tileSize)),(int) (TileCenterY + (0.125*tileSize)), (int)(tileSize*0.75), (int)(tileSize*0.75));

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