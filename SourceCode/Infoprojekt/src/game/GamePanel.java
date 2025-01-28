package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
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

    BufferedImage grass;

    public GamePanel(){
        this.setPreferredSize(new Dimension(1000,600)); //random values, TO DO: choose better
        this.setDoubleBuffered(true);

        try {
            grass = ImageIO.read(new File("H:\\Informatik\\projektQ4\\MajasBranch\\Graphics\\between grass (64x64).png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel picLabel = new JLabel(new ImageIcon(grass));
        add(picLabel);
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

            posXinArray = gameController.getPosX(); 
            posYinArray = gameController.getPosY();
            calcMovement(gameController.getDirection());

            // wait so during testing, it doens't get overwhelmed
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            posXinArray += 1;

            //draw updated
            repaint();
        }
    }
    int TileCenterX; // Coordinates of topleft corner of the player square
    int TileCenterY;

    int posXinArray = 50;// tile on which player is standing, center tile Field-coords
    int posYinArray = 50;

    int posXonTile = 0;
    int posYonTile = 0;

    // temp please ignore!!
    int aHelp = posXinArray-4;
    int bHelp = posYinArray-4;
    // temp

    int indexCurrentX;
    int indexCurrentY;

    int movementX;
    int movementY;

    char[][] field = testField();
    public void paintComponent(Graphics g){ // paint() oder paintComponent() ???
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;

        frameWidth = this.getWidth(); 
        frameHeight = this.getHeight();

        numWidth = (int) frameWidth / tileSize + 3;
        numHeight = (int) frameHeight / tileSize + 3;

        TileCenterX = ((frameWidth - tileSize) / 2);
        TileCenterY = ((frameHeight - tileSize) / 2);

        for(int i = 0; i<numWidth; i++){ // TODO: add exception for border of world (arrayOutOfBounds)
            for(int j = 0; j<numHeight; j++){
                indexCurrentX = posXinArray-(numWidth / 2)+(i*1);
                indexCurrentY = posYinArray-(numHeight / 2)+(j*1);

                if(indexCurrentX == posXinArray && indexCurrentY == posYinArray){
                    g2d.setColor(Color.YELLOW); // highlight current player square yellow
                }
                
                 else if(field[indexCurrentX][indexCurrentY] == 'X'){
                    g2d.setColor(Color.WHITE);
                } else {
                    g2d.setColor(Color.GRAY);
                }
                // lagere die coords berechnung von fillRect aus, berechne mithilfe je 1/10 square, musst wissen wo TileCenter liegt (ändere Tilecenter je nachdem)
                movementX = (int)(TileCenterX-(gameController.getOffsetX()*tileSize)-(tileSize*(posXinArray-indexCurrentX)));
                movementY = (int)(TileCenterY-(gameController.getOffsetY()*tileSize)-(tileSize*(posYinArray-indexCurrentY)));

                g2d.drawImage(grass, movementX, movementY,null);
                if(indexCurrentX == aHelp && indexCurrentY == bHelp) {
                    g2d.setColor(Color.BLUE); // paint random square blue to visualize movement
                    g2d.fillRect(movementX, movementY, tileSize, tileSize); //find location of the square relative to player square
                }
            }
        } 
        // temp player dot
        g2d.setColor(Color.BLACK);
        g2d.fillOval((int)(TileCenterX + (0.125*tileSize)),(int) (TileCenterY + (0.125*tileSize)), (int)(tileSize*0.75), (int)(tileSize*0.75));

        g2d.dispose();
    }


    // Q W E 
    // A   D
    // Y S C
    public void calcMovement(char mov) {

        if(mov == 'Q' || mov == 'W' || mov == 'E') {
        } else if(mov == 'Y' || mov == 'S' || mov == 'C') {
        }
        if(mov == 'Q' || mov == 'A' || mov == 'Y') {
        } else if (mov == 'E' || mov == 'D' || mov == 'C') {
        }
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