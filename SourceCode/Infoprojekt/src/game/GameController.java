package game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

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
     * stores worldName in a String attribute
     */
    private String worldName = "testWorld";

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
        try(FileWriter file = new FileWriter("SourceCode/Infoprojekt/saves/" + worldName + ".json")) {
            JSONObject properties = new JSONObject();

            properties.put("worldName", worldName);
            properties.put("posX", String.valueOf(posXinArray));
            properties.put("posY", String.valueOf(posYinArray));

            JSONArray outerArray = new JSONArray();

            for (Field[] row : wGenerator.getMap()) {
                JSONArray innerArray = new JSONArray();
                for (Field field : row) {
                    innerArray.put(field != null ? field.toJSONObject() : JSONObject.NULL);
                }
                outerArray.put(innerArray);
            }

            properties.put("worldMap", outerArray);

            file.write(properties.toString(4));

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    /** 
     * loads all world parameters from the previously saved json file
     * @param filePath FilePath of the saved World
     * @return boolean successfully loaded or not?
     */
    public boolean loadWorld(String filepath) {
        JSONObject savedObject = readJsonFile(filepath);

        worldName = savedObject.getString("worldName");
        posXinArray = Integer.parseInt(savedObject.getString("posX"));
        posYinArray = Integer.parseInt(savedObject.getString("posY"));
        JSONArray worldMap = savedObject.getJSONArray("worldMap");

        System.out.println(worldName);
        System.out.println(posXinArray);
        System.out.println(posYinArray);
        return true;
    }

    
    /** 
     * @param filePath FilePath of the JSON File to read from
     * @return JSONObject Returns JSON Input File as JSONObject
     */
    public static JSONObject readJsonFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            return new JSONObject(content.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
