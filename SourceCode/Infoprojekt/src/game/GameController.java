package game;

import game.WorldTile.TileValue;
import java.io.FileWriter;
import java.io.IOException;
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
     * min = -0.5
     * max < 0.5
     */
    private float posXonTile;

    /**
     * to be done
     * min = -0.5
     * max < 0.5
     */
    private float posYonTile;

    // Q W E
    // A D
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
        posYonTile = 0;
    }

    /**
     * to be done
     */
    public void update() {
        moveItems(getStartingPoints());
    }

    /**
     * to be done
     * 
     * @return ArrayList<Tuple> to be done
     */
    private ArrayList<Tuple> getStartingPoints() {
        ArrayList<Tuple> buildingList = new ArrayList<Tuple>();

        int mapLengthX = wGenerator.getXLengthMap();
        int mapLengthY = wGenerator.getYLengthMap();

        for (int i = 0; i < mapLengthX; i++) {
            for (int j = 0; j < mapLengthY; j++) {
                Field temp = wGenerator.getField(i, j);
                if (temp != null && temp.getBuilding() != null) {
                    buildingList.add(new Tuple(i, j));
                }
            }
        }

        ArrayList<Tuple> listOfStartingPoints = (ArrayList<Tuple>) buildingList.clone();
        for (Tuple tuple : buildingList) {
            int x = tuple.getA();
            int y = tuple.getB();
            Field temp = wGenerator.getField(x, y);
            byte[] inputDirectionsOfBuilding = temp.getBuilding().getInputDirections();
            byte rotation = temp.getBuilding().getRotation();
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
                if (x < 0 || x >= mapLengthX || y < 0 || y >= mapLengthY) {
                    continue;
                }
                Field temp2 = wGenerator.getField(x, y);
                if (temp2 == null || temp2.getBuilding() == null) {
                    continue;
                }
                byte[] outputDirectionsOfOtherBuilding = temp2.getBuilding().getOutputDirections();
                byte rotationOfOtherBuilding = temp2.getBuilding().getRotation();
                for (int j = 0; j < outputDirectionsOfOtherBuilding.length; j++) {
                    if ((outputDirectionsOfOtherBuilding[j] + rotationOfOtherBuilding + 2) % 4 == direction) {
                        listOfStartingPoints.remove(new Tuple(x, y));
                    }
                }
            }
        }
        return listOfStartingPoints;
    }

    /**
     * to be done
     * 
     * @param startingPoints to be done
     */
    private void moveItems(ArrayList<Tuple> startingPoints) {
        int mapLengthX = wGenerator.getXLengthMap();
        int mapLengthY = wGenerator.getYLengthMap();

        for (Tuple tuple : startingPoints) {
            int x = tuple.getA();
            int y = tuple.getB();
            Building b = wGenerator.getField(x, y).getBuilding();
            byte[] inputDirectionsOfBuilding = b.getInputDirections();
            byte rotation = b.getRotation();
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
                if (x < 0 || x >= mapLengthX || y < 0 || y >= mapLengthY) {
                    continue;
                }
                Field temp2 = wGenerator.getField(x, y);
                if (temp2 == null || temp2.getBuilding() == null) {
                    continue;
                }
                Building b2 = temp2.getBuilding();
                byte[] outputDirectionsOfOtherBuilding = b2.getOutputDirections();
                byte rotationOfOtherBuilding = b2.getRotation();
                for (int j = 0; j < outputDirectionsOfOtherBuilding.length; j++) {
                    if ((outputDirectionsOfOtherBuilding[j] + rotationOfOtherBuilding + 2) % 4 == direction) {
                        b2.moveItemToNextBuilding(b);
                    }
                }
            }
        }
    }

    /**
     * to be done
     * Q W E
     * A _ D
     * Y S C
     */
    public void movePlayer(char direction) {
        if ("QWEADYSC".indexOf(direction) == -1) {
            throw new IllegalArgumentException(
                    "The supplied direction is exspected to be one of Q, W, E, A, D, Y, S or C.");
        }

        movementDirection = direction;
        float delta = 0.1f;
        float deltaDiagonal = (float) Math.sqrt(delta * delta / 2f);
        switch (direction) {
            case 'Q':
                posXonTile -= deltaDiagonal;
                posYonTile -= deltaDiagonal;
                break;

            case 'W':
                posYonTile -= delta;
                break;

            case 'E':
                posXonTile += deltaDiagonal;
                posYonTile -= deltaDiagonal;
                break;

            case 'A':
                posXonTile -= delta;
                break;

            case 'D':
                posXonTile += delta;
                break;

            case 'Y':
                posXonTile -= deltaDiagonal;
                posYonTile += deltaDiagonal;
                break;

            case 'S':
                posYonTile += delta;
                break;

            case 'C':
                posXonTile += deltaDiagonal;
                posYonTile += deltaDiagonal;
                break;
        }

        if (posXonTile < -0.5) {
            posXinArray--;
            posXonTile++;
        } else if (posXonTile >= 0.5) {
            posXinArray++;
            posXonTile--;
        }

        if (posYonTile < -0.5) {
            posYinArray--;
            posYonTile++;
        } else if (posYonTile >= 0.5) {
            posYinArray++;
            posYonTile--;
        }

        int maxX = wGenerator.getXLengthMap() - 1;
        int maxY = wGenerator.getYLengthMap() - 1;
        if (posXinArray < 0 || (posXinArray == 0 && posXonTile < 0f)) {
            posXinArray = 0;
            posXonTile = 0f;
        } else if (posXinArray > maxX || (posXinArray == maxX && posXonTile > 0f)) {
            posXinArray = maxX;
            posXonTile = 0f;
        }

        if (posYinArray < 0 || (posYinArray == 0 && posYonTile < 0f)) {
            posYinArray = 0;
            posYonTile = 0f;
        } else if (posYinArray > maxY || (posYinArray == maxY && posYonTile > 0f)) {
            posYinArray = maxY;
            posYonTile = 0f;
        }

        // generate new fields if necessary
        for (int i = -50; i <= 50; i++) {
            for (int j = -50; j <= 50; j++) {
                if (posXinArray + i < 0 || posYinArray + j < 0 || maxX <= posXinArray + i || maxY <= posYinArray + j) {
                    continue;
                }
                wGenerator.generateTile(posXinArray + i, posYinArray + j);
            }
        }
    }

    /**
     * to be done
     * 
     * @param item to be done
     * @return boolean to be done
     */
    public static boolean addItemToInventory(Item item) {
        if (inventory.containsKey(item)) {
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
     * 
     * @return int to be done
     */
    public int getPosX() {
        return posXinArray;
    }

    /**
     * to be done
     * 
     * @return int to be done
     */
    public int getPosY() {
        return posYinArray;
    }

    /**
     * to be done
     * 
     * @return float to be done
     */
    public float getOffsetX() {
        return posXonTile;
    }

    /**
     * to be done
     * 
     * @return float to be done
     */
    public float getOffsetY() {
        return posYonTile;
    }

    /**
     * to be done
     * 
     * @return char to be done
     */
    public char getDirection() {
        return movementDirection;
    }

    /**
     * to be done
     */
    private class Tuple {
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
        public boolean equals(Object obj) {
            Tuple other = (Tuple) obj;
            if (a == other.getA() && b == other.getB()) {
                return true;
            }
            return false;
        }
    }

    /**
     * to be done
     * @return boolean to be done
     */
    public boolean saveWorld() {
        return true;
    }
}
