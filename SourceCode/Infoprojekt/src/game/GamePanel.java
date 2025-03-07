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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

import main.InputHandler;

/**
 * Responsible for the entirety of visual output in the main game and therefore
 * the View-component,
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
    Dictionary<String, String> imgPaths = new Hashtable<>();

    /**
     * Constructor of the GamePanel class
     * Sets up the frame and the GameController, InputHandler and GameInputHandler
     * to allow for interaction later.
     * Loads all textures needed into their respective dictionary
     * 
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

        // Load images into dictionary
        // Misc
        imgPaths.put("player", "./Graphics/FalkeOben.png");
        imgPaths.put("grass", "./Graphics/between grass (64x64).png");
        imgPaths.put("stoneItem", "./Graphics/Stein.png");
        imgPaths.put("copper", "./Graphics/GoldOre.png");
        imgPaths.put("arrow", "./Graphics/arrow.png");
        imgPaths.put("preview", "./Graphics/Bauen Preview.png");

        // Conveyor belts
        imgPaths.put("conveyorUP", "./Graphics/ConveyerBelt-oben.png");
        imgPaths.put("conveyorLEFT", "./Graphics/ConveyerBelt-links.png");
        imgPaths.put("conveyorDOWN", "./Graphics/ConveyerBelt-unten.png");
        imgPaths.put("conveyorRIGHT", "./Graphics/ConveyerBelt-rechts.png");

        // Corner conveyor belts
        imgPaths.put("conveyorUPtoRIGHT", "./Graphics/conveyorUPtoRIGHT.png");
        imgPaths.put("conveyorRIGHTtoDOWN", "./Graphics/conveyorRIGHTtoDOWN.png");
        imgPaths.put("conveyorDOWNtoLEFT", "./Graphics/conveyorDOWNtoLEFT.png");
        imgPaths.put("conveyorLEFTtoUP", "./Graphics/conveyorLEFTtoUP.png");
        imgPaths.put("conveyorRIGHTtoUP", "./Graphics/conveyorRIGHTtoUP.png");
        imgPaths.put("conveyorDOWNtoRIGHT", "./Graphics/conveyorDOWNtoRIGHT.png");
        imgPaths.put("conveyorLEFTtoDOWN", "./Graphics/conveyorLEFTtoDOWN.png");
        imgPaths.put("conveyorUPtoLEFT", "./Graphics/conveyorUPtoLEFT.png");

        // Extractors
        imgPaths.put("extractorUP", "./Graphics/drill.png");
        imgPaths.put("extractorLEFT", "./Graphics/faceLEFT.png");
        imgPaths.put("extractorDOWN", "./Graphics/drill.png");
        imgPaths.put("extractorRIGHT", "./Graphics/faceRIGHT.png");

        // Smelters
        imgPaths.put("smelterUP", "./Graphics/faceUP.png");
        imgPaths.put("smelterLEFT", "./Graphics/faceLEFT.png");
        imgPaths.put("smelterDOWN", "./Graphics/faceDOWN.png");
        imgPaths.put("smelterRIGHT", "./Graphics/faceRIGHT.png");

        // Collection Sites
        // imgPaths.put("collectionSiteQ", "./Graphics/collectionSiteQ.jpg");
        // imgPaths.put("collectionSiteW", "./Graphics/collectionSiteW.jpg");
        // imgPaths.put("collectionSiteE", "./Graphics/collectionSiteE.jpg");
        // imgPaths.put("collectionSiteA", "./Graphics/collectionSiteA.jpg");
        // imgPaths.put("collectionSiteS", "./Graphics/collectionSiteS.jpg");
        // imgPaths.put("collectionSiteD", "./Graphics/collectionSiteD.jpg");
        // imgPaths.put("collectionSiteY", "./Graphics/collectionSiteY.jpg");
        // imgPaths.put("collectionSiteX", "./Graphics/collectionSiteX.jpg");
        // imgPaths.put("collectionSiteC", "./Graphics/collectionSiteC.jpg");

        Enumeration<String> keys = imgPaths.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            try {
                images.put(key, ImageIO.read(new File(imgPaths.get(key))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Music.LoopMusic(".\\Music\\Wizard.wav");
    }

    /**^
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


    // Image newImage = yourImage.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);


    /**
     * Repaints the panel
     * Always keeps player centered and redraws the background to imitate movement
     * 
     * @param g to be done
     */
    public void paintComponent(Graphics g) { 
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // tile on which player is standing, center tile Field-coordinates
        int posXinArray = gameController.getPosX();
        int posYinArray = gameController.getPosY();

        int indexCurrentX;
        int indexCurrentY;

        int movementX;
        int movementY;

        int width = this.getWidth();
        int height = this.getHeight();

        // number of squares on the screen
        int numWidth = (int) width / tileSize + 3;
        int numHeight = (int) height / tileSize + 3;

        // Coordinates of topleft corner of the player square
        int TileCenterX = ((width - tileSize) / 2);
        int TileCenterY = ((height - tileSize) / 2);

        Field field;

        int mapXLength = gameController.getXLengthMap();
        int mapYLength = gameController.getYLengthMap();

        for (int i = 0; i < numWidth; i++) {
            for (int j = 0; j < numHeight; j++) {
                indexCurrentX = posXinArray - (numWidth / 2) + i;
                indexCurrentY = posYinArray - (numHeight / 2) + j;

                movementX = (int) (TileCenterX - (gameController.getOffsetX() * tileSize) - (tileSize * (posXinArray - indexCurrentX)));
                movementY = (int) (TileCenterY - (gameController.getOffsetY() * tileSize) - (tileSize * (posYinArray - indexCurrentY)));

                // Color the outside of the map blue
                if (indexCurrentX < 0 || indexCurrentY < 0 || indexCurrentX >= mapXLength || indexCurrentY >= mapYLength) {
                    g2d.setColor(Color.BLUE);
                    g2d.fillRect(movementX, movementY, tileSize, tileSize);
                    continue;
                }

                // Draw the resources for each field
                field = gameController.getField(indexCurrentX, indexCurrentY);
                if (field.getResourceID() == 0) {
                    g2d.drawImage(images.get("grass"), movementX, movementY, null);
                } else if (field.getResourceID() == 1) {
                    g2d.setColor(Color.GRAY);
                    g2d.fillRect(movementX, movementY, tileSize, tileSize);
                } else if (field.getResourceID() == 2) {
                    g2d.drawImage(images.get("copper"), movementX, movementY, null);
                } else if (field.getResourceID() == 3) {
                    g2d.setColor(Color.LIGHT_GRAY);
                    g2d.fillRect(movementX, movementY, tileSize, tileSize);
                } else if (field.getResourceID() == 4) {
                    g2d.setColor(Color.YELLOW);
                    g2d.fillRect(movementX, movementY, tileSize, tileSize);
                }
                
                // Draw the buildings
                if (field.getBuilding() != null) {
                    byte rotation = field.getBuilding().getRotation();
                    if (field.getBuilding().getClass() == CollectionSite.class) {
                        switch (rotation) {
                            case 1:
                                g2d.drawImage(images.get("extractorUP"), movementX, movementY, null);
                                break;
                            case 2: 
                                g2d.drawImage(images.get("collectionSiteSide"), movementX, movementY, null);
                                break;
                            case 3:
                                g2d.drawImage(images.get("extractorDOWN"), movementX, movementY, null);
                                break;
                            case 4: 
                                g2d.drawImage(images.get("collectionSiteSide"), movementX, movementY, null);
                                break;
                            case 5: 
                                g2d.drawImage(images.get("collectionSiteMid"), movementX, movementY, null);
                                break;
                            case 6: 
                                g2d.drawImage(images.get("collectionSiteSide"), movementX, movementY, null);
                                break;
                            case 7: 
                                g2d.drawImage(images.get("extractorLEFT"), movementX, movementY, null);
                                break;
                            case 8: 
                                g2d.drawImage(images.get("collectionSiteSide"), movementX, movementY, null);
                                break;
                            case 9: 
                                g2d.drawImage(images.get("extractorLEFT"), movementX, movementY, null);
                        }
                    } else if (field.getBuilding().getClass() == ConveyorBelt.class) {
                        g2d.drawImage(images.get(rotateConveyorBelt(field.getBuilding().getRotation(), field.getBuilding().getOutputDirections()[0])), movementX, movementY, null);
                    } else if (field.getBuilding().getClass() == Extractor.class) {
                        switch (rotation) {
                            case 0:
                                g2d.drawImage(images.get("extractorUP"), movementX, movementY, null);
                                break;
                            case 1: 
                                g2d.drawImage(images.get("extractorRIGHT"), movementX, movementY, null);
                                break;
                            case 2:
                                g2d.drawImage(images.get("extractorDOWN"), movementX, movementY, null);
                                break;
                            case 3: 
                                g2d.drawImage(images.get("extractorLEFT"), movementX, movementY, null);
                        }
                        g2d.drawImage(images.get("extractorUP"), movementX, movementY, null);
                    } else if (field.getBuilding().getClass() == Smelter.class) {
                        g2d.setColor(Color.RED);
                        g2d.fillRect(movementX, movementY, tileSize, tileSize);
                        switch (rotation) {
                            case 0:
                                g2d.drawImage(images.get("smelterUP"), movementX, movementY, null);
                                break;
                            case 1: 
                                g2d.drawImage(images.get("smelterRIGHT"), movementX, movementY, null);
                                break;
                            case 2:
                                g2d.drawImage(images.get("smelterDOWN"), movementX, movementY, null);
                                break;
                            case 3: 
                                g2d.drawImage(images.get("smelterLEFT"), movementX, movementY, null);
                        }
                    }
                }

                // Draw the building highlight preview
                if (gameController.getBuildingToBePlaced() != null && indexCurrentX == posXinArray && indexCurrentY == posYinArray) {
                    g2d.drawImage(images.get("preview"), movementX, movementY, null);
                }
            }
        }


        // Draw the preview of the building about to be placed
        if (gameController.getBuildingToBePlaced() != null) {
            g2d.setColor(new Color(90, 160, 255, 130));
            int borderWidth = 20;
            g2d.fillRect(0 + borderWidth, 0, width - 2*borderWidth, borderWidth);           
            g2d.fillRect(0 + borderWidth, height - borderWidth, width - 2*borderWidth, borderWidth);  
            g2d.fillRect(0, 0, borderWidth, height);            
            g2d.fillRect(width - borderWidth, 0, borderWidth, height);   

            int previewCoords = 100;
            g2d.setColor(new Color(255,255,255, 157));
            g2d.fillRect(100-64, 100-64, 64*3, 64*3);
            if (gameController.getBuildingToBePlaced().getClass() == ConveyorBelt.class) {
                g2d.drawImage(images.get(rotateConveyorBelt(gameController.getBuildingToBePlaced().getRotation(), gameController.getBuildingToBePlaced().getOutputDirections()[0])), previewCoords, previewCoords, null);
            } else if (gameController.getBuildingToBePlaced().getClass() == Extractor.class) {
                switch (gameController.getBuildingToBePlaced().getRotation()) {
                    case 0:
                        g2d.drawImage(images.get("extractorUP"), previewCoords, previewCoords, null);
                        break;
                    case 1: 
                        g2d.drawImage(images.get("extractorRIGHT"), previewCoords, previewCoords, null);
                        break;
                    case 2:
                        g2d.drawImage(images.get("extractorDOWN"), previewCoords, previewCoords, null);
                        break;
                    case 3: 
                        g2d.drawImage(images.get("extractorLEFT"), previewCoords, previewCoords, null);
                }             
            } else if (gameController.getBuildingToBePlaced().getClass() == Smelter.class) {
                switch (gameController.getBuildingToBePlaced().getRotation()) {
                    case 0:
                        g2d.drawImage(images.get("smelterUP"), previewCoords, previewCoords, null);
                        break;
                    case 1: 
                        g2d.drawImage(images.get("smelterRIGHT"), previewCoords, previewCoords, null);
                        break;
                    case 2:
                        g2d.drawImage(images.get("smelterDOWN"), previewCoords, previewCoords, null);
                        break;
                    case 3: 
                        g2d.drawImage(images.get("smelterLEFT"), previewCoords, previewCoords, null);
                }
            }
        }


        // Building: array von input, array von output (bytes) 
        //   0
        // 3   1  
        //   2
        // 


        // Draw the sidebar
        g2d.setColor(Color.ORANGE);
        g2d.fillRect((int) Math.round(0.6 * tileSize), (int) (this.getHeight() / 2 - 2 * tileSize), tileSize,
                tileSize * 4);


        // Draw the player [IN EIGENE METHODE AUSLAGERN???]
        if (gameController.getDirection() != '0') {    
            AffineTransform playerTX = new AffineTransform();
            char dir = gameController.getDirection();
            double angle = 0;    
            switch(dir) {
                case 'E':
                    angle = Math.toRadians(45);
                    break;
                case 'D':
                    angle = Math.toRadians(90);
                    break;
                case 'C':
                    angle = Math.toRadians(135);
                    break;
                case 'S':
                    angle = Math.toRadians(180);
                    break;
                case 'Y':
                    angle = Math.toRadians(225);
                    break;
                case 'A':
                    angle = Math.toRadians(270);
                    break;
                case 'Q':
                    angle = Math.toRadians(315);
            }
            playerTX.translate(width/2 - images.get("player").getWidth() / 2, height/2 - images.get("player").getHeight() / 2);
            playerTX.rotate(angle, images.get("player").getWidth() / 2, images.get("player").getHeight() / 2);
            
            g2d.drawImage(images.get("player"), playerTX, null);
        } else {
            g2d.drawImage(images.get("player"), TileCenterX, TileCenterY, null);
        }


        // Draw the arrow
        AffineTransform tx = rotateArrow(images.get("arrow"), numWidth, numHeight);
        if (tx != null)
            g2d.drawImage(images.get("arrow"), tx, null);


        g2d.setColor(Color.BLACK);
        g2d.fillRect(width - 50 - 256, height - 50 - 100, 256, 100);


        // add wolken?
    }

    /**
     * NEEDS TO BE MOVED TO MODEL-CLASS !!!
     * Rotates and moves the arrow pointing 'home' (to Collection Site) using the
     * AffineTransform data type
     * 
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
                offsetX = width / 2;
                offsetY = arrowMargin;
                break;
            case 'E':
                angle = Math.toRadians(45);
                offsetX = width - arrowMargin;
                offsetY = arrowMargin;
                break;
            case 'A':
                angle = Math.toRadians(270);
                offsetX = arrowMargin;
                offsetY = height / 2;
                break;
            case 'D':
                angle = Math.toRadians(90);
                offsetX = width - arrowMargin;
                offsetY = height / 2;
                break;
            case 'Y':
                angle = Math.toRadians(225);
                offsetX = arrowMargin;
                offsetY = height - arrowMargin;
                break;
            case 'X':
                angle = Math.toRadians(180);
                offsetX = width / 2;
                offsetY = height - arrowMargin;
                break;
            case 'C':
                angle = Math.toRadians(135);
                offsetX = width - arrowMargin;
                offsetY = height - arrowMargin;
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
     * @return char returns the relative location in the form of a char according to
     *         the mapping:
     *         Q W E
     *         A S D
     *         Y X C
     *         where each letter symbolizes the direction (e.g. 'Q' is top-left, 'C'
     *         is bottom-right)
     */
    char locateHome(int numWidth, int numHeight) {
        // Temporary location of the Collection Site, need getter from GameController
        int xHome = gameController.getXLengthMap() / 2;
        int yHome = gameController.getYLengthMap() / 2;

        int posXPlayer = gameController.getPosX();
        int posYPlayer = gameController.getPosY();

        byte xLocation;
        byte yLocation;

        // Check where the Collection Site is relative to Player
        if (posXPlayer - 0.5 * numWidth > xHome)
            xLocation = -1;
        else if (posXPlayer + 0.5 * numWidth < xHome)
            xLocation = 1;
        else
            xLocation = 0;

        if (posYPlayer - 0.5 * numHeight > yHome)
            yLocation = -1;
        else if (posYPlayer + 0.5 * numHeight < yHome)
            yLocation = 1;
        else
            yLocation = 0;

        // Return direction as char 
        if (xLocation == -1 && yLocation == -1)
            return 'Q';
        else if (xLocation == 0 && yLocation == -1)
            return 'W';
        else if (xLocation == 1 && yLocation == -1)
            return 'E';
        else if (xLocation == -1 && yLocation == 0)
            return 'A';
        else if (xLocation == 0 && yLocation == 0)
            return 'S';
        else if (xLocation == 1 && yLocation == 0)
            return 'D';
        else if (xLocation == -1 && yLocation == 1)
            return 'Y';
        else if (xLocation == 0 && yLocation == 1)
            return 'X';
        else if (xLocation == 1 && yLocation == 1)
            return 'C';
        else
            return 0;
    }

    public String rotateConveyorBelt(byte input, byte output) {
        
        if (input == 0 && output == 1)
            return "conveyorUPtoLEFT";

        else if (input == 0 && output == 2)
            return "conveyorDOWN";

        else if (input == 0 && output == 3)
            return "conveyorUPtoRIGHT";




        else if (input == 1 && output == 1)
            return "conveyorRIGHTtoDOWN";

        else if (input == 1 && output == 2)
            return "conveyorLEFT";

        else if (input == 1 && output == 3)
            return "conveyorRIGHTtoUP";




        else if (input == 2 && output == 1)
            return "conveyorDOWNtoLEFT";

        else if (input == 2 && output == 2)
            return "conveyorUP";

        else if (input == 2 && output == 3)
            return "conveyorDOWNtoRIGHT";


        else if (input == 3 && output == 1)
            return "conveyorLEFTtoUP";

        else if (input == 3 && output == 2)
            return "conveyorRIGHT";

        else if (input == 3 && output == 3)
            return "conveyorLEFTtoDOWN";

        return null;
    }
}