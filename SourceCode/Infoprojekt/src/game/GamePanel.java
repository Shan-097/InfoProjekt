package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import main.InputHandler;


/**
 * to be done
 */
public class GamePanel extends JPanel implements Runnable {
    /**
     * Size of 1 tile
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

    Dictionary<String, BufferedImage> images = new Hashtable<>();
    Dictionary<Character, BufferedImage> falke = new Hashtable<>();

    /**
     * Constuctor, setup frame, load images
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
            
            falke.put('S', ImageIO.read(new File("./Graphics/MileniumFalkeUnten.png")));
            falke.put('Q', ImageIO.read(new File("./Graphics/Download.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Start game thread
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

        int indexCurrentX;
        int indexCurrentY;

        int movementX;
        int movementY;

        int frameWidth = this.getWidth(); 
        int frameHeight = this.getHeight();

        // number of squares on the screen 
        int numWidth = (int) frameWidth / tileSize + 3;
        int numHeight = (int) frameHeight / tileSize + 3;

        // Coordinates of topleft corner of the player square
        int TileCenterX = ((frameWidth - tileSize) / 2);
        int TileCenterY = ((frameHeight - tileSize) / 2);

        Field field;

        int mapXLength = gameController.getXLengthMap();
        int mapYLength = gameController.getYLengthMap();

        for(int i = 0; i<numWidth; i++){ 
            for(int j = 0; j<numHeight; j++){
                indexCurrentX = posXinArray-(numWidth / 2)+(i*1);
                indexCurrentY = posYinArray-(numHeight / 2)+(j*1);

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
                    //g2d.drawImage(images.get("grass"), movementX, movementY,null);
                    //g2d.drawImage(images.get("smelter"), movementX, movementY,null);
                    //continue;
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
        g2d.setColor(Color.MAGENTA);
        g2d.fillRect((int)Math.round(0.6*tileSize), (int)(frameHeight/2-2*tileSize), tileSize, tileSize*4);

        // Draw player
        g2d.drawImage(falke.get(gameController.getDirection()), TileCenterX, TileCenterY, null);
        g2d.dispose();
    }
}