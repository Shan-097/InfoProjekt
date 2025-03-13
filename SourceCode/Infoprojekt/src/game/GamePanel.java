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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.InputHandler;
import main.Tuple;

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
     * HashMap to load the textures displayed in the game (e.g. grass, buildings)
     */
    HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>(60);

    /**
     * to be done
     */
    HashMap<String, String> imgPaths = new HashMap<String, String>();

    /**
     * to be done
     */
    HashMap<Item, JLabel> resourceLabels = new HashMap<Item, JLabel>();

    /**
     * to be done
     */
    private int drawState = 0;

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
        imgPaths.put("sideBar", "./Graphics/sidebar.png");
        imgPaths.put("player", "./Graphics/FalkeOben.png");
        imgPaths.put("grass", "./Graphics/between grass (64x64).png");
        imgPaths.put("arrow", "./Graphics/arrow.png");
        imgPaths.put("preview", "./Graphics/Bauen Preview.png");

        // Items
        imgPaths.put("gold", "./Graphics/GoldConveyor.png");
        imgPaths.put("iron", "./Graphics/IronConveyor.png");
        imgPaths.put("stone", "./Graphics/StoneConveyor.png");
        imgPaths.put("copper", "./Graphics/CopperConveyor.png");

        imgPaths.put("copperIngot", "./Graphics/CopperIngotFinal.png");
        imgPaths.put("goldIngot", "./Graphics/GoldIngotFinal.png");
        imgPaths.put("ironIngot", "./Graphics/IronIngotFinal.png");
        // imgPaths.put("stoneIngot", "./Graphics/Stein.png");

        // Conveyor belts
        imgPaths.put("conveyorUP0", "./Graphics/ConveyerBelt-oben.png");
        imgPaths.put("conveyorUP1", "./Graphics/ConveyerBelt-oben f7.png");

        imgPaths.put("conveyorLEFT0", "./Graphics/ConveyerBelt-links.png");
        imgPaths.put("conveyorLEFT1", "./Graphics/ConveyerBelt-links f7.png");

        imgPaths.put("conveyorDOWN0", "./Graphics/ConveyerBelt-unten f1.png");
        imgPaths.put("conveyorDOWN1", "./Graphics/ConveyerBelt-unten f7.png");

        imgPaths.put("conveyorRIGHT0", "./Graphics/ConveyerBelt-rechts.png");
        imgPaths.put("conveyorRIGHT1", "./Graphics/ConveyerBelt-rechts f7.png");

        imgPaths.put("conveyorUPtoLEFT", "./Graphics/CB - rotiert oben Links.png");
        imgPaths.put("conveyorLEFTtoDOWN", "./Graphics/CB - rotiert oben Rechts.png");
        imgPaths.put("conveyorDOWNtoRIGHT", "./Graphics/CB - rotiert unten Links.png");
        imgPaths.put("conveyorRIGHTtoUP", "./Graphics/CB - rotiert unten Rechts.png");

        imgPaths.put("conveyorLEFTtoUP", "./Graphics/CB - rotiert oben Links.png");
        imgPaths.put("conveyorDOWNtoLEFT", "./Graphics/CB - rotiert oben Rechts.png");
        imgPaths.put("conveyorRIGHTtoDOWN", "./Graphics/CB - rotiert unten Links.png");
        imgPaths.put("conveyorUPtoRIGHT", "./Graphics/CB - rotiert unten Rechts.png");

        // Extractors
        imgPaths.put("extractorUP", "./Graphics/drillTop.png");
        imgPaths.put("extractorLEFT", "./Graphics/drillLeft.png");
        imgPaths.put("extractorDOWN", "./Graphics/drill.png");
        imgPaths.put("extractorRIGHT", "./Graphics/drillRIGHT.png");

        // Smelters
        imgPaths.put("smelterUP", "./Graphics/Furnace final.png");
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

        for (String key : imgPaths.keySet()) {
            try {
                images.put(key, ImageIO.read(new File(imgPaths.get(key))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        HashMap<Item, Integer> tempResources = gameController.getInventory();
        for (Entry<Item, Integer> entry : tempResources.entrySet()) {
            JLabel temp = new JLabel("" + entry.getValue());
            resourceLabels.put(entry.getKey(), temp);
            add(resourceLabels.get(entry.getKey()));
        }

        Music.stopMusic();
        Music.SpawnMusic(".\\Music\\spawn sound.wav");
        Music.stopMusic();
        Music.LoopMusic(".\\Music\\Hintergrund.wav");
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
        int count = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / frameDisplayTime;
            lastTime = currentTime;
            if (delta >= 1) {
                gameInputHandler.invokeMethodsFromInput();
                if (count == 4) {
                    drawState = (drawState + 1) % 4;
                    if (drawState == 0) {
                        gameController.update();
                    }
                }
                repaint();
                count = (count + 1) % 5;
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
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

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
        int numWidth = (int) width / tileSize + 3;
        int numHeight = (int) height / tileSize + 3;

        // Coordinates of topleft corner of the player square
        int TileCenterX = ((width - tileSize) / 2);
        int TileCenterY = ((height - tileSize) / 2);

        Field field;

        int mapXLength = gameController.getXLengthMap();
        int mapYLength = gameController.getYLengthMap();

        // draw resources and the outside
        for (int i = 0; i < numWidth; i++) {
            for (int j = 0; j < numHeight; j++) {
                indexCurrentX = posXinArray - (numWidth / 2) + i;
                indexCurrentY = posYinArray - (numHeight / 2) + j;

                movementX = (int) (TileCenterX - (gameController.getOffsetX() * tileSize)
                        - (tileSize * (posXinArray - indexCurrentX)));
                movementY = (int) (TileCenterY - (gameController.getOffsetY() * tileSize)
                        - (tileSize * (posYinArray - indexCurrentY)));

                // Color the outside of the map blue
                if (indexCurrentX < 0 || indexCurrentY < 0 || indexCurrentX >= mapXLength
                        || indexCurrentY >= mapYLength) {
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
            }
        }

        ArrayList<Tuple> startingPoints = gameController.getStartingPoints();
        int minX = posXinArray - (numWidth / 2);
        int maxX = posXinArray + (numWidth / 2);
        int minY = posYinArray - (numHeight / 2);
        int maxY = posYinArray + (numHeight / 2);
        while (true) {
            if (startingPoints.size() == 0) {
                break;
            }

            int x = startingPoints.get(0).getA();
            int y = startingPoints.get(0).getB();

            Building b = gameController.getField(x, y).getBuilding();
            if (b == null) {
                startingPoints.remove(0);
                continue;
            }
            byte[] inputDirections = b.getInputDirections();
            byte rotation = b.getRotation();
            for (int i = 0; i < inputDirections.length; i++) {
                byte direction;
                if (b.getClass() == CollectionSite.class) {
                    direction = (byte) (inputDirections[i] % 4);
                } else {
                    direction = (byte) ((inputDirections[i] + rotation) % 4);
                }
                int newX = x;
                int newY = y;
                switch (direction) {
                    case 0:
                        newY--;
                        break;

                    case 1:
                        newX++;
                        break;

                    case 2:
                        newY++;
                        break;

                    case 3:
                        newX--;
                        break;
                }
                if (newX < 0 || newX >= mapXLength || newY < 0 || newY >= mapYLength) {
                    continue;
                }
                Field temp2 = gameController.getField(newX, newY);
                if (temp2 == null || temp2.getBuilding() == null) {
                    continue;
                }
                byte[] outputDirectionsOfOtherBuilding = temp2.getBuilding().getOutputDirections();
                byte rotationOfOtherBuilding = temp2.getBuilding().getRotation();
                for (int j = 0; j < outputDirectionsOfOtherBuilding.length; j++) {
                    if ((outputDirectionsOfOtherBuilding[j] + rotationOfOtherBuilding + 2) % 4 == direction) {
                        startingPoints.add(new Tuple(newX, newY));
                    }
                }
            }

            startingPoints.remove(0);

            if (x >= minX || x <= maxX || y >= minY || y <= maxY) {
                x = (int) (TileCenterX + (x - posXinArray - gameController.getOffsetX()) * tileSize);
                y = (int) (TileCenterY + (y - posYinArray - gameController.getOffsetY()) * tileSize);
                if (b.getClass() == CollectionSite.class) {
                    switch (rotation) {
                        case 1:
                            g2d.drawImage(images.get("extractorUP"), x, y, null);
                            break;
                        case 2:
                            g2d.drawImage(images.get("collectionSiteSide"), x, y, null);
                            break;
                        case 3:
                            g2d.drawImage(images.get("extractorDOWN"), x, y, null);
                            break;
                        case 4:
                            g2d.drawImage(images.get("collectionSiteSide"), x, y, null);
                            break;
                        case 5:
                            g2d.drawImage(images.get("collectionSiteMid"), x, y, null);
                            break;
                        case 6:
                            g2d.drawImage(images.get("collectionSiteSide"), x, y, null);
                            break;
                        case 7:
                            g2d.drawImage(images.get("extractorLEFT"), x, y, null);
                            break;
                        case 8:
                            g2d.drawImage(images.get("collectionSiteSide"), x, y, null);
                            break;
                        case 9:
                            g2d.drawImage(images.get("extractorLEFT"), x, y, null);
                    }
                } else if (b.getClass() == ConveyorBelt.class) {
                    this.drawConveyorBelt(g2d, b, x, y);
                } else if (b.getClass() == Extractor.class) {
                    switch ((rotation + 2) % 4) {
                        case 0:
                            g2d.drawImage(images.get("extractorUP"), x, y, null);
                            break;
                        case 1:
                            g2d.drawImage(images.get("extractorRIGHT"), x, y, null);
                            break;
                        case 2:
                            g2d.drawImage(images.get("extractorDOWN"), x, y, null);
                            break;
                        case 3:
                            g2d.drawImage(images.get("extractorLEFT"), x, y, null);
                    }
                } else if (b.getClass() == Smelter.class) {
                    switch (rotation) {
                        case 0:
                            g2d.drawImage(images.get("smelterUP"), x, y, null);
                            break;
                        case 1:
                            g2d.drawImage(images.get("smelterRIGHT"), x, y, null);
                            break;
                        case 2:
                            g2d.drawImage(images.get("smelterDOWN"), x, y, null);
                            break;
                        case 3:
                            g2d.drawImage(images.get("smelterLEFT"), x, y, null);
                    }
                }
            }
        }

        // Draw the building highlight preview
        if (gameController.getBuildingToBePlaced() != null) {
            g2d.drawImage(images.get("preview"), (int) (TileCenterX - gameController.getOffsetX() * tileSize),
                    (int) (TileCenterY - gameController.getOffsetY() * tileSize), null);
        }

        // Draw the preview of the building about to be placed
        if (gameController.getBuildingToBePlaced() != null) {
            g2d.setColor(new Color(90, 160, 255, 130));
            int borderWidth = 20;
            g2d.fillRect(0 + borderWidth, 0, width - 2 * borderWidth, borderWidth);
            g2d.fillRect(0 + borderWidth, height - borderWidth, width - 2 * borderWidth, borderWidth);
            g2d.fillRect(0, 0, borderWidth, height);
            g2d.fillRect(width - borderWidth, 0, borderWidth, height);

            int previewCoords = 100;
            g2d.setColor(new Color(255, 255, 255, 157));
            g2d.fillRect(100 - 64, 100 - 64, 64 * 3, 64 * 3);
            if (gameController.getBuildingToBePlaced().getClass() == ConveyorBelt.class) {
                this.drawConveyorBelt(g2d, gameController.getBuildingToBePlaced(), previewCoords, previewCoords);
            } else if (gameController.getBuildingToBePlaced().getClass() == Extractor.class) {
                switch ((gameController.getBuildingToBePlaced().getRotation() + 2) % 4) {
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

        // Draw the sidebar
        int border = 30;
        g2d.drawImage(images.get("sideBar"), (int) Math.round(0.6 * tileSize),
                (int) (this.getHeight() / 2 - 2 * tileSize), null); // Draw sidebar Image
        g2d.drawImage(images.get("conveyorUP"), (int) Math.round(0.6 * tileSize) + border / 2,
                (int) (this.getHeight() / 2 - 2 * tileSize) + border / 2, null); // Draw Buildings inside

        // Draw the player [IN EIGENE METHODE AUSLAGERN???]
        if (gameController.getDirection() != '0') {
            AffineTransform playerTX = new AffineTransform();
            char dir = gameController.getDirection();
            double angle = 0;
            switch (dir) {
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
            playerTX.translate(width / 2 - images.get("player").getWidth() / 2,
                    height / 2 - images.get("player").getHeight() / 2);
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

        // Draw resource count
        g2d.setColor(new Color(255, 255, 255, 127));
        g2d.fillRect(width - 180, 30, 150, 270);

        HashMap<Item, Integer> tempResources = gameController.getInventory();
        for (Entry<Item, Integer> entry : tempResources.entrySet()) {
            JLabel temp = resourceLabels.get(entry.getKey());
            temp.setText("" + entry.getValue());
            // temp.setLocation(, );

        }
    }

    /**
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

    private void drawConveyorBelt(Graphics2D g2d, Building b, int posX, int posY) {
        byte input = b.getRotation();
        byte output = b.getOutputDirections()[0];
        ArrayList<Item> content = new ArrayList<Item>(b.getInventory()); // 16 by 16 pixels for items on conveyor belts
        HashSet<Integer> movedItems = b.getMovedItems();
        int[][] itemCoordinates = new int[3][2];
        int middle = tileSize / 2;
        int state = 3 * drawState;
        String conveyor = "";

        if (input == 0 && output == 1) {
            conveyor = "conveyorUPtoRIGHT";
            itemCoordinates[2][0] = middle - 4;
            itemCoordinates[2][1] = movedItems.contains(2) ? middle - 25 + state : middle - 25;
            itemCoordinates[1][0] = movedItems.contains(1) ? middle - 4 + state : middle - 4;
            itemCoordinates[1][1] = middle - 4;
            itemCoordinates[0][0] = movedItems.contains(0) ? middle + 17 + state : middle + 17;
            itemCoordinates[0][1] = middle - 4;
        } else if (input == 0 && output == 2) {
            conveyor = drawState < 2 ? "conveyorDOWN0" : "conveyorDOWN1";
            itemCoordinates[2][0] = middle - 4;
            itemCoordinates[2][1] = movedItems.contains(2) ? middle - 25 + state : middle - 25;
            itemCoordinates[1][0] = middle - 4;
            itemCoordinates[1][1] = movedItems.contains(1) ? middle - 4 + state : middle - 4;
            itemCoordinates[0][0] = middle - 4;
            itemCoordinates[0][1] = movedItems.contains(0) ? middle + 17 + state : middle + 17;
        } else if (input == 0 && output == 3) {
            conveyor = "conveyorUPtoLEFT";
            itemCoordinates[2][0] = middle - 4;
            itemCoordinates[2][1] = movedItems.contains(2) ? middle - 25 + state : middle - 25;
            itemCoordinates[1][0] = movedItems.contains(1) ? middle - 4 - state : middle - 4;
            itemCoordinates[1][1] = middle - 4;
            itemCoordinates[0][0] = movedItems.contains(0) ? middle - 25 - state : middle - 25;
            itemCoordinates[0][1] = middle - 4;
        } else if (input == 1 && output == 1) {
            conveyor = "conveyorRIGHTtoDOWN";
            itemCoordinates[2][0] = movedItems.contains(2) ? middle + 17 - state : middle + 17;
            itemCoordinates[2][1] = middle - 4;
            itemCoordinates[1][0] = middle - 4;
            itemCoordinates[1][1] = movedItems.contains(1) ? middle - 4 + state : middle - 4;
            itemCoordinates[0][0] = middle - 4;
            itemCoordinates[0][1] = movedItems.contains(0) ? middle + 17 + state : middle + 17;
        } else if (input == 1 && output == 2) {
            conveyor = drawState < 2 ? "conveyorLEFT0" : "conveyorLEFT1";
            itemCoordinates[2][0] = movedItems.contains(2) ? middle + 17 - state : middle + 17;
            itemCoordinates[2][1] = middle - 4;
            itemCoordinates[1][0] = movedItems.contains(1) ? middle - 4 - state : middle - 4;
            itemCoordinates[1][1] = middle - 4;
            itemCoordinates[0][0] = movedItems.contains(0) ? middle - 25 - state : middle - 25;
            itemCoordinates[0][1] = middle - 4;
        } else if (input == 1 && output == 3) {
            conveyor = "conveyorRIGHTtoUP";
            itemCoordinates[2][0] = movedItems.contains(2) ? middle + 17 - state : middle + 17;
            itemCoordinates[2][1] = middle - 4;
            itemCoordinates[1][0] = middle - 4;
            itemCoordinates[1][1] = movedItems.contains(1) ? middle - 4 - state : middle - 4;
            itemCoordinates[0][0] = middle - 4;
            itemCoordinates[0][1] = movedItems.contains(0) ? middle - 25 - state : middle - 25;
        } else if (input == 2 && output == 1) {
            conveyor = "conveyorDOWNtoLEFT";
            itemCoordinates[2][0] = middle - 4;
            itemCoordinates[2][1] = movedItems.contains(2) ? middle + 17 - state : middle + 17;
            itemCoordinates[1][0] = movedItems.contains(1) ? middle - 4 - state : middle - 4;
            itemCoordinates[1][1] = middle - 4;
            itemCoordinates[0][0] = movedItems.contains(0) ? middle - 25 - state : middle - 25;
            itemCoordinates[0][1] = middle - 4;
        } else if (input == 2 && output == 2) {
            conveyor = drawState < 2 ? "conveyorUP0" : "conveyorUP1";
            itemCoordinates[2][0] = middle - 4;
            itemCoordinates[2][1] = movedItems.contains(2) ? middle + 17 - state : middle + 17;
            itemCoordinates[1][0] = middle - 4;
            itemCoordinates[1][1] = movedItems.contains(1) ? middle - 4 - state : middle - 4;
            itemCoordinates[0][0] = middle - 4;
            itemCoordinates[0][1] = movedItems.contains(0) ? middle - 25 - state : middle - 25;
        } else if (input == 2 && output == 3) {
            conveyor = "conveyorDOWNtoRIGHT";
            itemCoordinates[2][0] = middle - 4;
            itemCoordinates[2][1] = movedItems.contains(2) ? middle + 17 - state : middle + 17;
            itemCoordinates[1][0] = movedItems.contains(1) ? middle - 4 + state : middle - 4;
            itemCoordinates[1][1] = middle - 4;
            itemCoordinates[0][0] = movedItems.contains(0) ? middle + 17 + state : middle + 17;
            itemCoordinates[0][1] = middle - 4;
        } else if (input == 3 && output == 1) {
            conveyor = "conveyorLEFTtoUP";
            itemCoordinates[2][0] = movedItems.contains(2) ? middle - 25 + state : middle - 25;
            itemCoordinates[2][1] = middle - 4;
            itemCoordinates[1][0] = middle - 4;
            itemCoordinates[1][1] = movedItems.contains(1) ? middle - 4 - state : middle - 4;
            itemCoordinates[0][0] = middle - 4;
            itemCoordinates[0][1] = movedItems.contains(0) ? middle - 25 - state : middle - 25;
        } else if (input == 3 && output == 2) {
            conveyor = drawState < 2 ? "conveyorRIGHT0" : "conveyorRIGHT1";
            itemCoordinates[2][0] = movedItems.contains(2) ? middle - 25 + state : middle - 25;
            itemCoordinates[2][1] = middle - 4;
            itemCoordinates[1][0] = movedItems.contains(1) ? middle - 4 + state : middle - 4;
            itemCoordinates[1][1] = middle - 4;
            itemCoordinates[0][0] = movedItems.contains(0) ? middle + 17 + state : middle + 17;
            itemCoordinates[0][1] = middle - 4;
        } else if (input == 3 && output == 3) {
            conveyor = "conveyorLEFTtoDOWN";
            itemCoordinates[2][0] = movedItems.contains(2) ? middle - 25 + state : middle - 25;
            itemCoordinates[2][1] = middle - 4;
            itemCoordinates[1][0] = middle - 4;
            itemCoordinates[1][1] = movedItems.contains(1) ? middle - 4 + state : middle - 4;
            itemCoordinates[0][0] = middle - 4;
            itemCoordinates[0][1] = movedItems.contains(0) ? middle + 17 + state : middle + 17;
        }

        g2d.drawImage(images.get(conveyor), posX, posY, null);
        for (int i = 0; i < content.size(); i++) {
            if (content.get(i) == null) {
                continue;
            }
            switch (content.get(i).getItemID()) {
                case 0:
                    g2d.drawImage(images.get("stoneIngot"), itemCoordinates[i][0] + posX - 6,
                            itemCoordinates[i][1] + posY - 6, 20, 20,
                            null, null);
                    break;
                case 1:
                    g2d.drawImage(images.get("copperIngot"), itemCoordinates[i][0] + posX - 6,
                            itemCoordinates[i][1] + posY - 6, 20, 20,
                            null, null);
                    break;
                case 2:
                    g2d.drawImage(images.get("ironIngot"), itemCoordinates[i][0] + posX - 6,
                            itemCoordinates[i][1] + posY - 6, 20, 20,
                            null, null);
                    break;
                case 3:
                    g2d.drawImage(images.get("goldIngot"), itemCoordinates[i][0] + posX - 6,
                            itemCoordinates[i][1] + posY - 6, 20, 20,
                            null, null);
                    break;
                case 4:
                    g2d.drawImage(images.get("stone"), itemCoordinates[i][0] + posX - 6,
                            itemCoordinates[i][1] + posY - 6, 20, 20,
                            null, null);
                    break;

                case 5:
                    g2d.drawImage(images.get("copper"), itemCoordinates[i][0] + posX - 6,
                            itemCoordinates[i][1] + posY - 6, 20, 20,
                            null, null);
                    break;

                case 6:
                    g2d.drawImage(images.get("iron"), itemCoordinates[i][0] + posX - 6,
                            itemCoordinates[i][1] + posY - 6, 20, 20,
                            null, null);
                    break;

                case 7:
                    g2d.drawImage(images.get("gold"), itemCoordinates[i][0] + posX - 6,
                            itemCoordinates[i][1] + posY - 6, 20, 20,
                            null, null);
                    break;
            }
        }
    }
}