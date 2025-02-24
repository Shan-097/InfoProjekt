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
    // A   D
    // Y S C
    /**
     * to be done
     */
    private char movementDirection = '0';

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

        int maxX = wGenerator.getXLengthMap() - 1;
        int maxY = wGenerator.getYLengthMap() - 1;

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
     */
    public void update() {
        ArrayList<Tuple> startingPoints = getStartingPoints();
    }

    /**
     * to be done
     * 
     * @return ArrayList<Tuple> to be done
     */
    public ArrayList<Tuple> getStartingPoints() {
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
     * Q W E
     * A _ D
     * Y S C
     */
    public void movePlayer(char direction) {
        if ("QWEADYSC".indexOf(direction) == -1) {
            throw new IllegalArgumentException(
                    "The supplied direction is expected to be one of Q, W, E, A, D, Y, S or C.");
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
                if (posXinArray + i < 0 || posYinArray + j < 0 || maxX < posXinArray + i || maxY < posYinArray + j) {
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


    // mithilfe input-handler:
    // wenn '1' --> rufe (1) mit spezieller Maschine auf
    // wenn variable X != null UND 'R' (bzw. 'Esc' oder 'Enter') --> rufe (2) (bzw. (3) oder (4)) auf
    // (1) method: platziere building --> mit z.B. '1' speicher building in variable X (mit Rotation)
    // (2) method: rotate --> mit 'R' Eingabe rotieren
    // (3) method: exit --> mit 'Esc' abbrechen
    // (4) method: confirm --> mit 'Enter' building dort platzieren


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

    public Field getField(int posXinArray, int posYinArray) {
        return wGenerator.getField(posXinArray, posYinArray);
    }

    public int getXLengthMap() {
        return wGenerator.getXLengthMap();
    }

    public int getYLengthMap() {
        return wGenerator.getYLengthMap();
    }
}
