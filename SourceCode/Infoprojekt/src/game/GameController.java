package game;

import java.util.ArrayList;
import java.util.HashMap;

public class GameController {
    private static HashMap<Item, Integer> inventory;
    private static int posXinArray;
    private static int posYinArray;
    private static byte posXonTile;// max is 50, min is -50
    private static byte posYonTile;// max is 50, min is -50
    private WorldGenerator wGenerator;

    public GameController() {
        inventory = new HashMap<Item, Integer>();
        wGenerator = new WorldGenerator();
        posXinArray = 10;
        posYinArray = 8;
        posXonTile = 50;
        posYinArray = 50;
    }

    public void update() {
        ArrayList<Tuple> startingPoints = getStartingPoints();
    }

    public ArrayList<Tuple> getStartingPoints() {
        ArrayList<Tuple> buildingList = new ArrayList<Tuple>();
        Field[][] map = wGenerator.getMap();
        map[0][0] = new Field(new Extractor(), null);
        map[0][0].getBuilding().setRotation((byte) 3);
        map[1][0] = new Field(new Smelter(), null);
        map[1][0].getBuilding().setRotation((byte) 3);

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] != null && map[i][j].getBuilding() != null) {
                    buildingList.add(new Tuple(i, j));
                }
            }
        }

        ArrayList<Tuple> listOfStartingPoints = (ArrayList<Tuple>) buildingList.clone();
        for (Tuple tuple : buildingList) {
            int x = tuple.getA();
            int y = tuple.getB();
            byte[] inputDirectionsOfBuilding = map[x][y].getBuilding().getInputDirections();
            byte rotation = map[x][y].getBuilding().getRotation();
            for (int i = 0; i < inputDirectionsOfBuilding.length; i++) {
                byte direction = (byte) ((inputDirectionsOfBuilding[i] + rotation) % 4);
                int deltaX = 0;
                int deltaY = 0;
                switch (direction) {
                    case 0:
                        deltaY--;
                        break;

                    case 1:
                        deltaX++;
                        break;

                    case 2:
                        deltaY++;
                        break;

                    case 3:
                        deltaX--;
                        break;
                }
                x += deltaX;
                y += deltaY;
                if (x < 0 || x >= map.length || y < 0 || y >= map[0].length) {
                    continue;
                }
                if (map[x][y] == null || map[x][y].getBuilding() == null) {
                    continue;
                }
                byte[] outputDirectionsOfOtherBuilding = map[x][y].getBuilding().getOutputDirections();
                byte rotationOfOtherBuilding = map[x][y].getBuilding().getRotation();
                for (int j = 0; j < outputDirectionsOfOtherBuilding.length; j++){
                    if ((outputDirectionsOfOtherBuilding[j] + rotationOfOtherBuilding + 2) % 4 == direction){
                        listOfStartingPoints.remove(new Tuple(x, y));
                    }
                }
            }
        }
        return listOfStartingPoints;
    }

    public static boolean addItemToInventory(Item item){
        if (inventory.containsKey(item)){
            if (inventory.get(item) == Integer.MAX_VALUE) {
                return false;
            }
            inventory.replace(item, inventory.get(item) + 1);
        } else {
            inventory.put(item, 1);
        }
        return true;
    }

    public int getXKoordinateInArray(){
        return posXinArray;
    }

    public int getYKoordinateInArray(){
        return posYinArray;
    }
    
    public int getXKoordinateOnTile(){
        return posXonTile;
    }
    
    public int getYKoordinateOnTile(){
        return posYonTile;
    }

    private class Tuple{
        private int a;
        private int b;

        public Tuple(int pA, int pB) {
            a = pA;
            b = pB;
        }

        public int getA() {
            return a;
        }

        public int getB() {
            return b;
        }

        @Override
        public boolean equals(Object obj){
            Tuple other = (Tuple) obj;
            if (a == other.getA() && b == other.getB()) {
                return true;
            }
            return false;
        }
    }
}
