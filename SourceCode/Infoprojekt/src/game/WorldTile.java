package game;

import org.json.JSONObject;

public class WorldTile {
    public enum TileValue {
        ironore,
        copperore,
    }

    private final int posX;
    private final int posY;

    private final TileValue value;

    public WorldTile(int posX, int posY, TileValue value){
        this.posX = posX;
        this.posY = posY;

        this.value = value;
    }

    public int getPosX(){
        return posX;
    }

    public int getPosY(){
        return posY;
    }

    public TileValue getValue(){
        return value;
    }

    public JSONObject toJSON(){
        JSONObject tmpObject = new JSONObject();
        
        tmpObject.put("posX", this.posX);
        tmpObject.put("posY", this.posY);
        tmpObject.put("value", this.value);

        return tmpObject;
    }
}
