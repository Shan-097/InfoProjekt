package game;

import java.io.FileWriter;
import java.io.IOException;

public class GameController {

    private final String worldName = "TestWorld";

    private int posXinArray;
    private int posYinArray;
    private byte posXonTile;//max is 100, min is 0
    private byte posYonTile;//max is 100, min is 0


    public GameController(){
        posXinArray = 10;
        posYinArray = 8;
        posXonTile = 50;
        posYinArray = 50;
    }

    public void update(){

    }

    public boolean saveWorld(){
        try (FileWriter file = new FileWriter("SourceCode/Infoprojekt/saves/" + worldName + ".json")) {
            file.write("{\"worldName\": \"" + worldName + "\"}");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
