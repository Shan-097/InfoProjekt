package game;

public class GameController {
    private int posXinArray;
    private int posYinArray;
    private byte posXonTile;// max is 100, min is 0
    private byte posYonTile;// max is 100, min is 0
    private WorldGenerator wGenerator;

    public GameController() {
        wGenerator = new WorldGenerator();
        posXinArray = 10;
        posYinArray = 8;
        posXonTile = 50;
        posYinArray = 50;
    }

    public void update() {

    }

    public void checkBuildings() {
        Field[][] map = wGenerator.getMap();
        for (int i = 0; i <= map.length; i++) {
            for (int j = 0; j <= map[0].length; j++) {

            }
        }
    }
}
