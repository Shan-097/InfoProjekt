package game;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

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
        checkBuildings();
    }

    public void checkBuildings() {
        ArrayList<Tuple> buildingList = new ArrayList<Tuple>();
        
        Field[][] map = wGenerator.getMap();
        for (int i = 0; i <= map.length; i++) {
            for (int j = 0; j <= map[0].length; j++) {
                if(map[i][j].getBuilding() != null){
                    buildingList.add(new Tuple(i, j));
                }
            }
        }
        
        
        int randomNum = ThreadLocalRandom.current().nextInt(map.length, map[0].length + 1);
        buildingList.get(randomNum);
    }
    
    class Tuple{
        private int a;
        private int b;
        public Tuple(int pa, int pb){
            a = pa;
            b = pb;
        }
        public int getA() {
            return a;
        }
        public int getB() {
            return b;
        }
    }
}
