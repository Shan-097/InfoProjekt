package game;

import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONObject;

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
        try (FileWriter file = new FileWriter("saves/" + worldName + ".json")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("worldName", worldName);
            jsonObject.put("posX", posXinArray);
            jsonObject.put("posY", posYinArray);
            file.write(jsonObject.toString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
