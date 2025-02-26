package game;

import java.awt.Color;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import javax.swing.JPanel;

import main.InputHandler;


/**
 * Responsible for the entirety of visual output in the main game and therefore the View-component, 
 * including drawing the map, the player and the buildings. 
 * Updates constantly at a set rate.
 */
public class GamePanel extends JPanel implements Runnable {
    /**
     * Size of a tile
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
     * Dictionary to load the textures displayed in the game (e.g. grass, buildings)
     */
    Dictionary<String, BufferedImage> images = new Hashtable<>();

    /**
     * Dictionary to load the different rotations of the player character 
     */
    Dictionary<Character, BufferedImage> falke = new Hashtable<>();

    /**
     * Constructor of the GamePanel class
     * Sets up the frame and the GameController, InputHandler and GameInputHandler to allow for interaction later.
     * Loads all textures needed into their respective dictionary
     * @param pFr Frame rate 
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
            images.put("grass", ImageIO.read(new File("./Graphics/between grass (64x64).png")));
            images.put("stoneItem", ImageIO.read(new File("./Graphics/stein.png")));
            images.put("smelter", ImageIO.read(new File("./Graphics/Cartography_Table_JE2_BE1.png")));
            images.put("copper", ImageIO.read(new File("./Graphics/CopperConveyor.png")));
            images.put("arrow", ImageIO.read(new File("./Graphics/arrow.png")));
            
            falke.put('Q', ImageIO.read(new File("./Graphics/FalkeLinksOben.png")));
            falke.put('W', ImageIO.read(new File("./Graphics/FalkeOben.png")));
            falke.put('E', ImageIO.read(new File("./Graphics/FalkeRechtsOben.png")));
            falke.put('A', ImageIO.read(new File("./Graphics/FalkeLinks.png")));
            falke.put('D', ImageIO.read(new File("./Graphics/FalkeRechts.png")));
            falke.put('Y', ImageIO.read(new File("./Graphics/FalkeLinksUnten.png")));
            falke.put('S', ImageIO.read(new File("./Graphics/FalkeUnten.png")));
            falke.put('C', ImageIO.read(new File("./Graphics/FalkeRechtsUnten.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Music.LoopMusic(".\\Music\\Wizard.wav");
    }

    /**
     * Start game thread
     */
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Run-method which acts as the game loop
     * Calculates the frame time, updates game state and redraws the game 
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
     * Repaints the panel
     * Always keeps player centered and redraws the background to imitate movement
     * 
     * @param g to be done
     */
    public void paintComponent(Graphics g) { // paint() oder paintComponent() ???
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        
        // tile on which player is standing, center tile Field-coords
        int posXinArray = gameController.getPosX();
        int posYinArray = gameController.getPosY();

        int indexCurrentX;
        int indexCurrentY;

        int movementX;
        int movementY;

        int width = this.getWidth();
        int height = this.getHeight();

        // number of squares on the screen 
        int numWidth = (int) width / tileSize + 2;
        int numHeight = (int) height / tileSize + 2;

        // Coordinates of topleft corner of the player square
        int TileCenterX = ((width - tileSize) / 2);
        int TileCenterY = ((height - tileSize) / 2);

        Field field;

        int mapXLength = gameController.getXLengthMap();
        int mapYLength = gameController.getYLengthMap();

        for(int i = 0; i<numWidth; i++){ 
            for(int j = 0; j<numHeight; j++){
                indexCurrentX = posXinArray-(numWidth / 2)+i;
                indexCurrentY = posYinArray-(numHeight / 2)+j;

                movementX = (int)(TileCenterX-(gameController.getOffsetX()*tileSize)-(tileSize*(posXinArray-indexCurrentX)));
                movementY = (int)(TileCenterY-(gameController.getOffsetY()*tileSize)-(tileSize*(posYinArray-indexCurrentY)));

                if (indexCurrentX < 0 || indexCurrentY < 0 || indexCurrentX >= mapXLength || indexCurrentY >= mapYLength) {
                        g2d.setColor(Color.BLUE);
                        g2d.fillRect(movementX, movementY, tileSize, tileSize);
                        continue;
                }

                field = gameController.getField(indexCurrentX, indexCurrentY);

                if(field.getResourceID() == 0){
                    g2d.drawImage(images.get("grass"), movementX, movementY,null);
                    continue;
                } else if (field.getResourceID() == 1){
                    g2d.setColor(Color.GRAY);
                } else if (field.getResourceID() == 2){
                    g2d.drawImage(images.get("copper"), movementX, movementY,null);
                    continue;
                } else if (field.getResourceID() == 3){
                    g2d.setColor(Color.LIGHT_GRAY);
                } else if (field.getResourceID() == 4){
                    g2d.setColor(Color.YELLOW);
                }
                g2d.fillRect(movementX, movementY, tileSize, tileSize);
            }
        }
        

        g2d.setColor(Color.ORANGE);
        g2d.fillRect((int)Math.round(0.6*tileSize), (int)(this.getHeight() / 2 - 2*tileSize), tileSize, tileSize*4);

        // Draw player
        if(gameController.getDirection() != '0') {
            g2d.drawImage(falke.get(gameController.getDirection()), TileCenterX, TileCenterY, null);
        } else {
            g2d.drawImage(falke.get('W'), TileCenterX, TileCenterY, null);
        }
        
        AffineTransform tx = rotateArrow(images.get("arrow"), numWidth, numHeight);
        if(tx != null)
            g2d.drawImage(images.get("arrow"), tx, null);


        // add wolken
        g2d.dispose();
    }



    /**
     * NEEDS TO BE MOVED TO MODEL-CLASS !!!
     * Rotates and moves the arrow pointing 'home' (to Collection Site) using the AffineTransform data type
     * @return AffineTransform returns the correct translation of the arrow 
     */
    AffineTransform rotateArrow(Image arrow, int numWidth, int numHeight) {
        AffineTransform tx = new AffineTransform();

        int imgWidth = arrow.getWidth(null);
        int imgHeight = arrow.getHeight(null);
        
        char directionHome = locateHome(numWidth, numHeight);
        
        int arrowMargin = 50;
        double angle = 0;
        
        int width = this.getWidth();
        int height = this.getHeight();

        int offsetX = 0, offsetY = 0;

        switch (directionHome) {
            case 'Q':
                angle = Math.toRadians(315); 
                offsetX = arrowMargin;
                offsetY = arrowMargin;        
                break;
            case 'W':   
                angle = Math.toRadians(0);
                offsetX = width/2;
                offsetY = arrowMargin;
                break;
            case 'E':
                angle = Math.toRadians(45);    
                offsetX = width-arrowMargin;
                offsetY = arrowMargin; 
                break;
            case 'A':  
                angle = Math.toRadians(270); 
                offsetX = arrowMargin;
                offsetY = height/2;
                break;
            case 'D':
                angle = Math.toRadians(90);   
                offsetX = width-arrowMargin;
                offsetY = height/2;
                break;
            case 'Y':
                angle = Math.toRadians(225);
                offsetX = arrowMargin;
                offsetY = height-arrowMargin;     
                break;
            case 'X':   
                angle = Math.toRadians(180);    
                offsetX = width/2;
                offsetY = height-arrowMargin;
                break;
            case 'C':
                angle = Math.toRadians(135);     
                offsetX = width-arrowMargin;
                offsetY = height-arrowMargin;
                break;       
            default:
                break;           
        }
        tx.translate(offsetX - imgWidth / 2, offsetY - imgHeight / 2); 
        tx.rotate(angle, imgWidth / 2, imgHeight / 2);

        if (directionHome == 'S')
            tx = null;

        return tx;
    }

    /**
     * NEEDS TO BE MOVED TO MODEL-CLASS !!!
     * Calculates where the 'home' (or Collection Site) is relative to the player
     * 
     * @return char returns the relative location in the form of a char according to the mapping: 
     * Q W E
     * A S D
     * Y X C
     * where each letter symbolizes the direction (e.g. 'Q' is top-left, 'C' is bottom-right)
     */
    char locateHome(int numWidth, int numHeight) {
        // Temporary location of the Collection Site, need getter from GameController
        int xHome = 100;
        int yHome = 100;
        
        int posXPlayer = gameController.getPosX();
        int posYPlayer = gameController.getPosY();

        byte xLocation;
        byte yLocation;

        if(posXPlayer - 0.5*numWidth > xHome) 
            xLocation = -1;
        else if(posXPlayer + 0.5*numWidth < xHome) 
            xLocation = 1;
        else 
            xLocation = 0;

        if(posYPlayer - 0.5*numHeight > yHome)
            yLocation = -1;
        else if(posYPlayer + 0.5*numHeight < yHome)
            yLocation = 1;
        else
            yLocation = 0;
        

        if(xLocation == -1 && yLocation == -1)
            return 'Q';
        else if(xLocation == 0 && yLocation == -1)
            return 'W';
        else if(xLocation == 1 && yLocation == -1)
            return 'E';
        else if(xLocation == -1 && yLocation == 0)
            return 'A';
        else if(xLocation == 0 && yLocation == 0)
            return 'S';
        else if(xLocation == 1 && yLocation == 0)
            return 'D';
        else if(xLocation == -1 && yLocation == 1)
            return 'Y';
        else if(xLocation == 0 && yLocation == 1)
            return 'X';
        else if(xLocation == 1 && yLocation == 1)
            return 'C';
        else
            return 0;
    }
}