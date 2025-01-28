package game;


public class WorldTile {
    public enum TileValue {
        ironore,
        copperore,
    }

    private final TileValue value;

    public WorldTile(TileValue value){
        this.value = value;
    }

    public TileValue getValue(){
        return value;
    }
}
