package game;

import java.awt.Color;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JButton;

import main.App;
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
     * generic width of the buttons in the starting menu
     */
    private final int buttonWidth = 230;

    /**
     * generic height of the buttons in the starting menu
     */
    private final int buttonHeight = 35;

    /**
     * size of a tile
     */
    private final int tileSize = 64;

    /**
     * desired frame rate of the game, it can't be higher but can drop under load
     */
    private final int frameRate;

    /**
     * thread that keeps the game running
     */
    private Thread gameThread;

    /**
     * instance of the gameController to allow interaction with the Model classes
     */
    private GameController gameController;

    /**
     * instance of the gameInputHandler to allow interaction with the Controller classes and player inputs
     */
    private GameInputHandler gameInputHandler;

    /**
     * HashMap to load the images displayed in the game (e.g. grass, buildings)
     */
    HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>(60);

    /**
     * HashMap to load the file paths of the images in the game
     */
    HashMap<String, String> imgPaths = new HashMap<String, String>();

    /**
     * HashMap of the JLabels used to display the current resource count in the top right
     */
    HashMap<Item, JLabel> resourceLabels = new HashMap<Item, JLabel>();

    /**
     * number to keep track of which frame of the animation to draw for each redraw
     */
    private long drawState = 0;

    /**
     * to be done
     */
    private boolean gamePaused;

    /**
     * to be done
     */
    private JButton resumeButton;

    /**
     * to be done
     */
    private JButton exitButton;

    /**
     * to be done
     */
    private JButton saveAndExitButton;

    /**
     * Constructor of the GamePanel class
     * Sets up the frame and the GameController, InputHandler and GameInputHandler
     * to allow for interaction later.
     * Loads all of the required textures into their respective dictionary
     * 
     * @param pFr       Frame rate
     * @param pFilePath to be done
     */
    public GamePanel(int pFr, String pFilePath) {
        this.setPreferredSize(new Dimension(1000, 600));// random values, TO DO: choose better
        this.setDoubleBuffered(true);
        frameRate = pFr;
        if (pFilePath == null) {
            gameController = new GameController();
        } else {
            gameController = new GameController(pFilePath);
        }
        InputHandler inputHandler = new InputHandler();
        this.addKeyListener(inputHandler);
        this.setFocusable(true);
        gameInputHandler = new GameInputHandler(gameController, this, inputHandler);

        // Load images into dictionary
        // Misc
        imgPaths.put("sideBar", "./Graphics/sidebar.png");
        imgPaths.put("player", "./Graphics/FalkeOben.png");
        imgPaths.put("grass", "./Graphics/between grass (64x64).png");
        imgPaths.put("arrow", "./Graphics/arrow.png");
        imgPaths.put("preview", "./Graphics/Bauen Preview.png");

        // Items
        imgPaths.put("stoneIngot", "./Graphics/Ziegelstein.png");
        imgPaths.put("copperIngot", "./Graphics/CopperIngotFinal.png");
        imgPaths.put("ironIngot", "./Graphics/IronIngotFinal.png");
        imgPaths.put("goldIngot", "./Graphics/GoldIngotFinal.png");
        
        imgPaths.put("stone", "./Graphics/StoneConveyor.png");
        imgPaths.put("copper", "./Graphics/CopperConveyor.png");
        imgPaths.put("iron", "./Graphics/IronConveyor.png");
        imgPaths.put("gold", "./Graphics/GoldConveyor.png");

        // Conveyor belts
        imgPaths.put("conveyor200", "./Graphics/ConveyerBelt-oben.png"); // oben 0
        imgPaths.put("conveyor201", "./Graphics/ConveyerBelt-oben f7.png"); // oben 1

        imgPaths.put("conveyor130", "./Graphics/ConveyerBelt-links.png"); // links 0
        imgPaths.put("conveyor131", "./Graphics/ConveyerBelt-links f7.png"); // links 1

        imgPaths.put("conveyor020", "./Graphics/ConveyerBelt-unten f1.png"); // unten 0
        imgPaths.put("conveyor021", "./Graphics/ConveyerBelt-unten f7.png"); // unten 1

        imgPaths.put("conveyor310", "./Graphics/ConveyerBelt-rechts.png"); // rechts 0
        imgPaths.put("conveyor311", "./Graphics/ConveyerBelt-rechts f7.png"); // rechts 1

        imgPaths.put("conveyor03", "./Graphics/CB - Rotiert unten Rechts final.png"); // oben zu links
        imgPaths.put("conveyor32", "./Graphics/CB - Rotiert oben Rechts final.png"); // links zu unten
        imgPaths.put("conveyor21", "./Graphics/CB - Rotiert oben Links final.png"); // unten zu rechts
        imgPaths.put("conveyor10", "./Graphics/CB - Rotiert unten Links final.png"); // rechts zu oben

        imgPaths.put("conveyor30", "./Graphics/CB - Rotiert unten Rechts(gespiegelt) final.png"); // links zu oben
        imgPaths.put("conveyor23", "./Graphics/CB - Rotiert oben Rechts(gespiegelt) final.png"); // unten zu links
        imgPaths.put("conveyor12", "./Graphics/CB - Rotiert oben Rechts (gespiegelt final).png"); // rechts zu unten
        imgPaths.put("conveyor01", "./Graphics/CB - Rotiert unten Links (gespiegelt)final.png"); // oben zu rechts

        // Extractors
        imgPaths.put("extractorUP0", "./Graphics/drillTop.png");
        imgPaths.put("extractorUP1", "./Graphics/drillTop.png");
        imgPaths.put("extractorUP2", "./Graphics/drillTop.png");
        imgPaths.put("extractorUP3", "./Graphics/drillTop.png");

        imgPaths.put("extractorLEFT0", "./Graphics/drillLeft.png");
        imgPaths.put("extractorLEFT1", "./Graphics/drillLeft.png");
        imgPaths.put("extractorLEFT2", "./Graphics/drillLeft.png");
        imgPaths.put("extractorLEFT3", "./Graphics/drillLeft.png");

        imgPaths.put("extractorDOWN0", "./Graphics/drill.png");
        imgPaths.put("extractorDOWN1", "./Graphics/drill.png");
        imgPaths.put("extractorDOWN2", "./Graphics/drill.png");
        imgPaths.put("extractorDOWN3", "./Graphics/drill.png");

        imgPaths.put("extractorRIGHT0", "./Graphics/drillRIGHT.png");
        imgPaths.put("extractorRIGHT1", "./Graphics/drillRIGHT.png");
        imgPaths.put("extractorRIGHT2", "./Graphics/drillRIGHT.png");
        imgPaths.put("extractorRIGHT3", "./Graphics/drillRIGHT.png");

        // Smelters
        imgPaths.put("smelterUP0", "./Graphics/Furnace f1 unten.png");
        imgPaths.put("smelterUP1", "./Graphics/Furnace f2 unten.png");
        imgPaths.put("smelterUP2", "./Graphics/Furnace f3 unten.png");
        imgPaths.put("smelterUP3", "./Graphics/Furnace f4 unten .png");

        imgPaths.put("smelterLEFT0", "./Graphics/Furnace f1 links.png");
        imgPaths.put("smelterLEFT1", "./Graphics/Furnace f2 links.png");
        imgPaths.put("smelterLEFT2", "./Graphics/Furnace f3 links.png");
        imgPaths.put("smelterLEFT3", "./Graphics/Furnace f4 links .png");

        imgPaths.put("smelterDOWN0", "./Graphics/Furnace f1 oben.png");
        imgPaths.put("smelterDOWN1", "./Graphics/Furnace f2 oben.png");
        imgPaths.put("smelterDOWN2", "./Graphics/Furnace f3 oben.png");
        imgPaths.put("smelterDOWN3", "./Graphics/Furnace f4 oben .png");

        imgPaths.put("smelterRIGHT0", "./Graphics/Furnace f1 rechts.png");
        imgPaths.put("smelterRIGHT1", "./Graphics/Furnace f2 rechts.png");
        imgPaths.put("smelterRIGHT2", "./Graphics/Furnace f3 rechts.png");
        imgPaths.put("smelterRIGHT3", "./Graphics/Furnace f4 rechts .png");

        // Collection Sites
        imgPaths.put("collectionSite10", "./Graphics/collectionEdgeTopLeft.png");
        imgPaths.put("collectionSite20", "./Graphics/collectionMiddleTop.png");
        imgPaths.put("collectionSite30", "./Graphics/collectionEdgeTopRight.png");
        imgPaths.put("collectionSite40", "./Graphics/collectionMiddleLeft.png");
        imgPaths.put("collectionSite50", "./Graphics/collectionSiteMitte.jpg");
        imgPaths.put("collectionSite60", "./Graphics/collectionMidRight.png");
        imgPaths.put("collectionSite70", "./Graphics/CollectionEdgeBotLeft.png");
        imgPaths.put("collectionSite80", "./Graphics/CollectionMiddleBottom.png");
        imgPaths.put("collectionSite90", "./Graphics/CollectionEdgeBotRight.png");

        imgPaths.put("collectionSite11", "./Graphics/collectionEdgeTopLeft.png");
        imgPaths.put("collectionSite21", "./Graphics/collectionMiddleTop.png");
        imgPaths.put("collectionSite31", "./Graphics/collectionEdgeTopRight.png");
        imgPaths.put("collectionSite41", "./Graphics/collectionMiddleLeft.png");
        imgPaths.put("collectionSite51", "./Graphics/collectionSiteMitte.jpg");
        imgPaths.put("collectionSite61", "./Graphics/collectionMidRight.png");
        imgPaths.put("collectionSite71", "./Graphics/CollectionEdgeBotLeft.png");
        imgPaths.put("collectionSite81", "./Graphics/CollectionMiddleBottom.png");
        imgPaths.put("collectionSite91", "./Graphics/CollectionEdgeBotRight.png");

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
        Music.SpawnMusic("./Music/spawn sound.wav");
        Music.stopMusic();
        Music.LoopMusic("./Music/Hintergrund.wav");

        resumeButton = new JButton("Resume");
        exitButton = new JButton("Exit");
        saveAndExitButton = new JButton("Save and Exit");

        resumeButton.setFont(new Font("Arial", Font.BOLD, 15));
        exitButton.setFont(new Font("Arial", Font.BOLD, 15));
        saveAndExitButton.setFont(new Font("Arial", Font.BOLD, 15));

        resumeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showPauseMenu();
            }
        });

        saveAndExitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showPauseMenu();
                gameController.saveWorld();
                App.loadStartingScreen();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showPauseMenu();
                App.loadStartingScreen();
            }
        });

        resumeButton.setRequestFocusEnabled(false);
        saveAndExitButton.setRequestFocusEnabled(false);
        exitButton.setRequestFocusEnabled(false);
        add(resumeButton);
        add(saveAndExitButton);
        add(exitButton);
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
                    drawState = drawState + 1;
                    if (drawState % 4 == 0) {
                        gameController.update();
                    }
                }
                repaint();
                count = (count + 1) % 5;
                delta--;
            }
        }
    }

    public void showPauseMenu() {
        gamePaused = !gamePaused;
    }

    /**
     * Repaints the panel 
     * Always keeps player centered and redraws the background to imitate movement
     * Responsible for the majority of visuals displayed on the screen
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
                    g2d.drawImage(images.get("collectionSite" + b.getRotation() + ((drawState % 4) / 2)), x, y, null);
                } else if (b.getClass() == ConveyorBelt.class) {
                    this.drawConveyorBelt(g2d, b, x, y);
                } else if (b.getClass() == Extractor.class) {
                    switch ((rotation + 2) % 4) {
                        case 0:
                            g2d.drawImage(images.get("extractorUP" + drawState % 4), x, y, null);
                            break;
                        case 1:
                            g2d.drawImage(images.get("extractorRIGHT" + drawState % 4), x, y, null);
                            break;
                        case 2:
                            g2d.drawImage(images.get("extractorDOWN" + drawState % 4), x, y, null);
                            break;
                        case 3:
                            g2d.drawImage(images.get("extractorLEFT" + drawState % 4), x, y, null);
                    }
                } else if (b.getClass() == Smelter.class) {
                    switch (rotation) {
                        case 0:
                            g2d.drawImage(images.get("smelterUP" + (drawState/2) % 4), x, y, null);
                            break;
                        case 1:
                            g2d.drawImage(images.get("smelterLEFT" + (drawState/2) % 4), x, y, null);
                            break;
                        case 2:
                            g2d.drawImage(images.get("smelterDOWN" + (drawState/2) % 4), x, y, null);
                            break;
                        case 3:
                            g2d.drawImage(images.get("smelterRIGHT" + (drawState/2) % 4), x, y, null);
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
                        g2d.drawImage(images.get("extractorUP" + drawState % 4), previewCoords, previewCoords, null);
                        break;
                    case 1:
                        g2d.drawImage(images.get("extractorRIGHT" + drawState % 4), previewCoords, previewCoords, null);
                        break;
                    case 2:
                        g2d.drawImage(images.get("extractorDOWN" + drawState % 4), previewCoords, previewCoords, null);
                        break;
                    case 3:
                        g2d.drawImage(images.get("extractorLEFT" + drawState % 4), previewCoords, previewCoords, null);
                }
            } else if (gameController.getBuildingToBePlaced().getClass() == Smelter.class) {
                switch (gameController.getBuildingToBePlaced().getRotation()) {
                    case 0:
                        g2d.drawImage(images.get("smelterUP" + (drawState/2) % 4), previewCoords, previewCoords, null);
                        break;
                    case 1:
                        g2d.drawImage(images.get("smelterLEFT" + (drawState/2) % 4), previewCoords, previewCoords, null);
                        break;
                    case 2:
                        g2d.drawImage(images.get("smelterDOWN" + (drawState/2) % 4), previewCoords, previewCoords, null);
                        break;
                    case 3:
                        g2d.drawImage(images.get("smelterRIGHT" + (drawState/2) % 4), previewCoords, previewCoords, null);
                }
            }

            // draw control options (rotate, build, exit)
            g2d.setColor(Color.BLACK);
            g2d.fillRect(width - 50 - 256, height - 50 - 100, 256, 100);
        }

        // Draw the sidebar
        int border = 30;
        g2d.drawImage(images.get("sideBar"), (int) Math.round(0.6 * tileSize),
                (int) (this.getHeight() / 2 - 2 * tileSize), null); // Draw sidebar Image
        g2d.drawImage(images.get("conveyor020"), (int) Math.round(0.6 * tileSize) + border / 2,
                (int) (this.getHeight() / 2 - 2 * tileSize) + border / 2, null); // Draw Buildings inside
        g2d.drawImage(images.get("extractorDOWN"), (int) Math.round(0.6 * tileSize) + border / 2,
                (int) (this.getHeight() / 2 - (border - 5)), null);
        g2d.drawImage(images.get("smelterDOWN"), (int) Math.round(0.6 * tileSize) + border / 2,
                (int) (this.getHeight() / 2 + tileSize), null);

        // Implement Font
        try {
            File fontFile = new File("./Graphics/D3Litebitmapism.ttf");
            Font D3Litemapism = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(D3Litemapism);
            g2d.setFont(D3Litemapism);
            g2d.setColor(Color.black);
            g2d.drawString(gameInputHandler.getKey("placeConveyorBelt"), (int) Math.round(0.6 * tileSize) + 7,
                    (int) (this.getHeight() / 2 - 2 * tileSize) + tileSize + border - 6); // Draw Keybinds
            g2d.drawString(gameInputHandler.getKey("placeExtractor"), (int) Math.round(0.6 * tileSize) + 7,
                    (int) (this.getHeight() / 2) + border * 2 - 12);
            g2d.drawString(gameInputHandler.getKey("placeSmelter"), (int) Math.round(0.6 * tileSize) + 7,
                    (int) (this.getHeight() / 2 + tileSize) + border * 2 + 14);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        // Draw the player 
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


        // Draw resource count
        g2d.setColor(new Color(255, 255, 255, 127));
        g2d.fillRect(width - 170, 30, 140, 265);

        HashMap<Item, Integer> tempResources = gameController.getInventory();
        int i = 0;
        Font resourceFont = new Font("Courier New", Font.BOLD, 15);
        for (Entry<Item, Integer> entry : tempResources.entrySet()) {
            JLabel temp = resourceLabels.get(entry.getKey());
            temp.setForeground(Color.DARK_GRAY);
            temp.setFont(resourceFont);
            temp.setText("" + entry.getValue());
            temp.setLocation(width - 120, 50 + 30 * entry.getKey().getItemID());
            String itemToDraw = "";
            switch(i) {
                case 0:
                    itemToDraw = "stoneIngot";
                    break;
                case 1:
                    itemToDraw = "copperIngot";
                    break;
                case 2:
                    itemToDraw = "ironIngot";
                    break;
                case 3:
                    itemToDraw = "goldIngot";
                    break;
                case 4:
                    itemToDraw = "stone";
                    break;
                case 5:
                    itemToDraw = "copper";
                    break;
                case 6:
                    itemToDraw = "iron";
                    break;
                case 7:
                    itemToDraw = "gold";
            }
            g2d.drawImage(images.get(itemToDraw), width - 155, (50 + 30*i)-5, 25, 25, null);
            i++;
        }

        if (gamePaused) {
            g2d.setColor(Color.WHITE);
            g2d.fillRect((width / 2) - (width / 3) / 2, (height / 2) - (height / 3) / 2, width / 3, height / 3);

            resumeButton.setBounds((this.getWidth() - buttonWidth) / 2, (this.getHeight() - buttonHeight) / 2 - 45,
                    buttonWidth, buttonHeight);
            saveAndExitButton.setBounds((this.getWidth() - buttonWidth) / 2, (this.getHeight() - buttonHeight) / 2 + 45,
                    buttonWidth, buttonHeight);
            exitButton.setBounds((this.getWidth() - buttonWidth) / 2, (this.getHeight() - buttonHeight) / 2,
                    buttonWidth, buttonHeight);
        }
    }

    /**
     * Rotates and moves the arrow pointing to the centered Collection Site using the
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
     * Checks where the the Collection Site is relative to the player
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

    /**
     * draws the correct rotation of the conveyor belt
     * draws the items that move along the conveyor belt
     * 
     * @param g2d   current Graphics2D object to draw outside of paintComponent()       
     * @param b     conveyor belt that need to be placed
     * @param posX  x-coordinate of the conveyor belt
     * @param posY  y-coordinate of the conveyor belt   
     */
    private void drawConveyorBelt(Graphics2D g2d, Building b, int posX, int posY) {
        byte input = b.getRotation();
        byte output = (byte) ((b.getOutputDirections()[0] + input) % 4);
        ArrayList<Item> content = new ArrayList<Item>(b.getInventory()); // 16 by 16 pixels for items on conveyor belts
        HashSet<Integer> movedItems = b.getMovedItems();
        int[][] itemCoordinates = new int[3][2];
        int middle = tileSize / 2;
        int state = (int) (3 * (drawState % 4));
        String conveyor = "conveyor" + input + output;
        conveyor += input == ((output + 2) % 4) ? ((drawState % 4) / 2) : "";

        if (input == 0) {
            itemCoordinates[2][0] = middle - 4;
            itemCoordinates[2][1] = movedItems.contains(2) ? middle - 25 + state : middle - 25;
        } else if (input == 1) {
            itemCoordinates[2][0] = movedItems.contains(2) ? middle + 17 - state : middle + 17;
            itemCoordinates[2][1] = middle - 4;
        } else if (input == 2) {
            itemCoordinates[2][0] = middle - 4;
            itemCoordinates[2][1] = movedItems.contains(2) ? middle + 17 - state : middle + 17;
        } else if (input == 3) {
            itemCoordinates[2][0] = movedItems.contains(2) ? middle - 25 + state : middle - 25;
            itemCoordinates[2][1] = middle - 4;
        }

        if (output == 0) {
            itemCoordinates[1][0] = middle - 4;
            itemCoordinates[1][1] = movedItems.contains(1) ? middle - 4 - state : middle - 4;
            itemCoordinates[0][0] = middle - 4;
            itemCoordinates[0][1] = movedItems.contains(0) ? middle - 25 - state : middle - 25;
        } else if (output == 1) {
            itemCoordinates[1][0] = movedItems.contains(1) ? middle - 4 + state : middle - 4;
            itemCoordinates[1][1] = middle - 4;
            itemCoordinates[0][0] = movedItems.contains(0) ? middle + 17 + state : middle + 17;
            itemCoordinates[0][1] = middle - 4;
        } else if (output == 2) {
            itemCoordinates[1][0] = middle - 4;
            itemCoordinates[1][1] = movedItems.contains(1) ? middle - 4 + state : middle - 4;
            itemCoordinates[0][0] = middle - 4;
            itemCoordinates[0][1] = movedItems.contains(0) ? middle + 17 + state : middle + 17;
        } else if (output == 3) {
            itemCoordinates[1][0] = movedItems.contains(1) ? middle - 4 - state : middle - 4;
            itemCoordinates[1][1] = middle - 4;
            itemCoordinates[0][0] = movedItems.contains(0) ? middle - 25 - state : middle - 25;
            itemCoordinates[0][1] = middle - 4;
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