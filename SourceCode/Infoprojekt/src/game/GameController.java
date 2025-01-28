package game;

import game.WorldTile.TileValue;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class GameController {

    private final String worldName = "TestWorld";

    private ArrayList<WorldTile> worldMap = new ArrayList<>();

    private int posXinArray;
    private int posYinArray;
    private byte posXonTile;//max is 100, min is 0
    private byte posYonTile;//max is 100, min is 0


    public GameController(){
        posXinArray = 10;
        posYinArray = 8;
        posXonTile = 50;
        posYinArray = 50;

        worldMap.add(new WorldTile(TileValue.copperore));
        worldMap.add(new WorldTile(TileValue.ironore));
    }

    public void update(){

    }

    public boolean saveWorld() {
        try (FileWriter file = new FileWriter("saves/" + worldName + ".json")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("worldName", worldName);
            jsonObject.put("posX", posXinArray);
            jsonObject.put("posY", posYinArray);

            JSONArray tilesArray = new JSONArray();
            for (WorldTile tile : worldMap) {
                JSONObject tileJson = new JSONObject();
                tileJson.put("value", tile.getValue());
                tilesArray.put(tileJson);
            }

            jsonObject.put("worldMap", tilesArray);

            file.write(jsonObject.toString(4));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
