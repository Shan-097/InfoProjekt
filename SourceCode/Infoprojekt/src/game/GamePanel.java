package game;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.InputHandler;



/**
 * to be done
 */
public class GamePanel extends JPanel implements Runnable {
    /**
     * to be done
     */
    private final int tileSize = 64;

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
    private GameInputHandler gameInputHandler;

    /**
     * to be done
     */
    private BufferedImage grass;



    /**
     * to be done
     */
    public GamePanel(int pFr) {
        this.setPreferredSize(new Dimension(1000, 600));// random values, TO DO: choose better
        this.setDoubleBuffered(true);
        frameRate = pFr;
        gameController = new GameController();
        InputHandler inputHandler = new InputHandler();
        this.addKeyListener(inputHandler);
        this.setFocusable(true);
        gameInputHandler = new GameInputHandler(gameController, inputHandler);

        try {
            grass = ImageIO.read(new File(".\\Graphics\\between grass (64x64).png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel picLabel = new JLabel(new ImageIcon(grass));
        add(picLabel);
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

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / frameDisplayTime;
            lastTime = currentTime;
            if (delta >= 1) {
                // update
                gameInputHandler.invokeMethodsFromInput();
                gameController.update();

                // draw updated
                repaint();
                delta--;
            }
        }
    }

    /**
     * to be done
     * @param g to be done
     */
    public void paintComponent(Graphics g){ // paint() oder paintComponent() ???
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;

        
        int posXinArray = gameController.getPosX();// tile on which player is standing, center tile Field-coords
        int posYinArray = gameController.getPosY();

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

        int frameWidth = this.getWidth(); 
        int frameHeight = this.getHeight();

        // number of squares on the screen 
        int numWidth = (int) frameWidth / tileSize + 3;
        int numHeight = (int) frameHeight / tileSize + 3;

        // Coordinates of topleft corner of the player square
        int TileCenterX = ((frameWidth - tileSize) / 2);
        int TileCenterY = ((frameHeight - tileSize) / 2);

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
                Resource r = gameController.getResource(indexCurrentX, indexCurrentY);
                if (r.getResourceID() != 0) {
                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(movementX, movementY, tileSize, tileSize);
                }
                Building b = gameController.getBuilding(indexCurrentX, indexCurrentY);
                if (b == null) {
                    continue;
                }
                if(b.getClass() == ConveyorBelt.class) {
                    if(b.getRotation() == 0) {
                        g2d.setColor(Color.CYAN);
                    } else {
                    g2d.setColor(Color.BLUE); // paint random square blue to visualize movement
                    }
                    g2d.fillRect(movementX, movementY, tileSize, tileSize); //find location of the square relative to player square
                }
            }
        } 
        // temp player dot
        g2d.setColor(Color.BLACK);
        g2d.fillOval((int)(TileCenterX + (0.125*tileSize)),(int) (TileCenterY + (0.125*tileSize)), (int)(tileSize*0.75), (int)(tileSize*0.75));

        g2d.dispose();
    }

    /**
     * to be done
     * @return char[][] to be done
     */
    private char[][] testField(){ // 2D array with checkerboard pattern to test the paint method
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