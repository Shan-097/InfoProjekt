package game;


import java.util.ArrayList;
import java.util.HashMap;



/**
 * to be done
 */
public class GameController {
    /**
     * to be done
     */
    private static HashMap<Item, Integer> inventory;

    /**
     * to be done
     */
    private int posXinArray;

    /**
     * to be done
     */
    private int posYinArray;

    /**
     * to be done
     */
    private byte posXonTile;//max is 50, min is -50

    /**
     * to be done
     */
    private byte posYonTile;//max is 50, min is -50

    // Q W E 
    // A   D
    // Y S C
    /**
     * to be done
     */
    private char movementDirection;

    /**
     * to be done
     */
    private WorldGenerator wGenerator;



    /**
     * to be done
     */
    public GameController() {
        inventory = new HashMap<Item, Integer>();
        wGenerator = new WorldGenerator();
        posXinArray = 50;
        posYinArray = 50;
        posXonTile = 0;
        posYinArray = 0;
    }


    /**
     * to be done
     */
    public void update() {
        ArrayList<Tuple> startingPoints = getStartingPoints();
    }

    /**
     * to be done
     * @return ArrayList<Tuple> to be done
     */
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

    /**
     * to be done
     * @param item to be done
     * @return boolean to be done
     */
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


    /**
     * to be done
     * @return int to be done
     */
    public int getPosX(){
        return posXinArray;
    }

    /**
     * to be done
     * @return int to be done
     */
    public int getPosY(){
        return posYinArray;
    }

    /**
     * to be done
     * @return byte to be done
     */
    public byte getOffsetX(){
        return posXonTile;
    }

    /**
     * to be done
     * @return byte to be done
     */
    public byte getOffsetY(){
        return posYonTile;
    }

    /**
     * to be done
     */
    public char getDirection(){
        return movementDirection;
    }


    /**
     * to be done
     */
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
