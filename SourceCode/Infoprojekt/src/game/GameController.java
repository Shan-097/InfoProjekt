package game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import main.Tuple;

/**
 * The GameController class is the heart of the model.<br>
 * It suplies the controller with the necessary get and set methods and connects
 * the individual components of the model.
 */
public class GameController {
    /**
     * The inventory of the player.<br>
     * Stores for every item how many are in the inventory.
     */
    private static HashMap<Item, Integer> inventory;

    /**
     * The x coordinate of the square the player is standing on.
     */
    private int posXinArray;

    /**
     * The y coordinate of the square the player is standing on.
     */
    private int posYinArray;

    /**
     * The relative x position of the player on the tile they are standing
     * on.<br>
     * The value is in [-0.5, 0.5).
     */
    private float posXonTile;

    /**
     * The relative y position of the player on the tile they are standing
     * on.<br>
     * The value is in [-0.5, 0.5).
     */
    private float posYonTile;

    /**
     * The movement direction of the player.<br>
     * It's value is one of 'Q', 'W', 'E', 'A', 'D', 'Y', 'S' and 'C'. Each encoding
     * one direction.
     */
    private char movementDirection;

    /**
     * The world generator object of the game controller.<br>
     * Stores and handles the map and the generation thereof.
     */
    private WorldGenerator wGenerator;

    /**
     * stores worldName in a String attribute
     */
    private String worldName = "testWorld";

    /**
     * A temporary Building object storing what the player plans to place next.
     */
    private Building buildingToBePlaced;

    /**
     * Instantiates a new Object of type GameController.<br>
     * Sets for example the starting position of the player.
     */
    public GameController() {
        inventory = new HashMap<Item, Integer>();
        inventory.put(Item.getItemWithID(0), 0);
        inventory.put(Item.getItemWithID(1), 0);
        inventory.put(Item.getItemWithID(2), 0);
        inventory.put(Item.getItemWithID(3), 0);
        inventory.put(Item.getItemWithID(4), 0);
        inventory.put(Item.getItemWithID(5), 0);
        inventory.put(Item.getItemWithID(6), 0);
        inventory.put(Item.getItemWithID(7), 0);
        wGenerator = new WorldGenerator(100, 100);

        posXinArray = wGenerator.getXLengthMap() / 2;
        posYinArray = wGenerator.getYLengthMap() / 2;
        posXonTile = 0;
        posYonTile = 0;
        movementDirection = 'S';
    }

    /**
     * Makes the changes to the world that are directely timed by ticks.<br>
     * E.g. moves the items on the conveyor belts.
     */
    public void update() {
        moveItems(getStartingPoints());
    }

    /**
     * "Walks through" the "graph" of the machines and finds the points nothing is
     * moved away from.
     * 
     * @return Returns the list of coordinates the found buildings are standing on.
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
                byte direction;
                if (temp.getBuilding().getClass() == CollectionSite.class) {
                    direction = (byte) (inputDirectionsOfBuilding[i] % 4);
                } else {
                    direction = (byte) ((inputDirectionsOfBuilding[i] + rotation) % 4);
                }
                int newX = x;
                int newY = y;
                switch (direction) {
                    case 0:
                        newY--;
                        break;

                    case 1:
                        newX++;
                        break;

                    case 2:
                        newY++;
                        break;

                    case 3:
                        newX--;
                        break;
                }
                if (newX < 0 || newX >= mapLengthX || newY < 0 || newY >= mapLengthY) {
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
                        listOfStartingPoints.remove(new Tuple(newX, newY));
                    }
                }
            }
        }
        return listOfStartingPoints;
    }

    /**
     * Moves the items of for example conveyor belts to the next building.<br>
     * The movement is from the "inside outwards".
     * 
     * @param startingPoints A list of coordinates of the building nothing is moved
     *                       away from.
     */
    private void moveItems(ArrayList<Tuple> startingPoints) {
        int mapLengthX = wGenerator.getXLengthMap();
        int mapLengthY = wGenerator.getYLengthMap();

        System.out.println();
        for (Tuple point : startingPoints) {
            int x = point.getA();
            int y = point.getB();
            Building b = wGenerator.getField(x, y).getBuilding();
            b.moveItemInsideBuilding();
        }
        System.out.println();

        while (true) {
            if (startingPoints.size() == 0) {
                return;
            }
            int x = startingPoints.getFirst().getA();
            int y = startingPoints.getFirst().getB();
            Building b = wGenerator.getField(x, y).getBuilding();
            byte[] inputDirectionsOfBuilding = b.getInputDirections();
            byte rotation = b.getRotation();
            for (int i = 0; i < inputDirectionsOfBuilding.length; i++) {
                byte direction;
                if (b.getClass() == CollectionSite.class) {
                    direction = (byte) (inputDirectionsOfBuilding[i] % 4);
                } else {
                    direction = (byte) ((inputDirectionsOfBuilding[i] + rotation) % 4);
                }
                int newX = x;
                int newY = y;
                switch (direction) {
                    case 0:
                        newY--;
                        break;

                    case 1:
                        newX++;
                        break;

                    case 2:
                        newY++;
                        break;

                    case 3:
                        newX--;
                        break;
                }
                if (newX < 0 || newX >= mapLengthX || newY < 0 || newY >= mapLengthY) {
                    continue;
                }
                Field temp2 = wGenerator.getField(newX, newY);
                if (temp2 == null || temp2.getBuilding() == null) {
                    continue;
                }
                Building b2 = temp2.getBuilding();
                byte[] outputDirectionsOfOtherBuilding = b2.getOutputDirections();
                byte rotationOfOtherBuilding = b2.getRotation();
                for (int j = 0; j < outputDirectionsOfOtherBuilding.length; j++) {
                    if ((outputDirectionsOfOtherBuilding[j] + rotationOfOtherBuilding + 2) % 4 == direction) {
                        b2.moveItemToNextBuilding(b);
                        startingPoints.addLast(new Tuple(newX, newY));
                        break;
                    }
                }
            }
            startingPoints.removeFirst();
        }
    }

    /**
     * Moves the player in the given direction/ sets the new player position.<br>
     * The characters encoding the movement direction are set as follows:<br>
     * Q W E<br>
     * A _ D<br>
     * Y S C
     * 
     * @param direction The direction the player is moving in.
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
                if (posXinArray + i < 0 || posYinArray + j < 0 || maxX <= posXinArray + i || maxY <= posYinArray + j) {
                    continue;
                }
                wGenerator.generateTile(posXinArray + i, posYinArray + j);
            }
        }
    }

    /**
     * Sets the building that the player plans to place next.
     * 
     * @param buildingType The type of building given by a string.
     */
    public void chooseBuildingToPlace(String buildingType) {
        switch (buildingType) {
            case "ConveyorBelt":
                buildingToBePlaced = new ConveyorBelt();
                break;

            case "Extractor":
                buildingToBePlaced = new Extractor();
                break;

            case "Smelter":
                buildingToBePlaced = new Smelter();
                break;
        }
    }

    /**
     * Removes the chosen building to be placed next.
     */
    public void cancelPlacement() {
        buildingToBePlaced = null;
    }

    /**
     * Rotates the building that is planned to be placed next.
     */
    public void rotateBuilding() {
        if (buildingToBePlaced != null) {
            buildingToBePlaced.rotate();
        }
    }

    /**
     * Finally places the building (if possible) that was chosen to be placed
     * next.<br>
     * Makes also small but necessary changes to some building types.
     */
    public void placeBuilding() {
        if (wGenerator.getField(posXinArray, posYinArray).getBuilding() != null) {
            return;
        }
        if (buildingToBePlaced == null) {
            return;
        }

        for (Entry<Item, Integer> cost : buildingToBePlaced.getCost().entrySet()) {
            if (inventory.get(cost.getKey()) < cost.getValue()) {
                return;
            }
        }

        if (buildingToBePlaced.getClass() == Extractor.class) {
            Resource resource = wGenerator.getField(posXinArray, posYinArray).getResource();
            if (resource.getResourceID() == 0) {
                return;
            }
            Extractor temp = (Extractor) buildingToBePlaced;
            temp.setResourceToBeExtracted(resource);
            buildingToBePlaced = temp;
        }

        for (Entry<Item, Integer> cost : buildingToBePlaced.getCost().entrySet()) {
            inventory.replace(cost.getKey(), inventory.get(cost.getKey()) - cost.getValue());
        }

        wGenerator.placeBuilding(posXinArray, posYinArray, buildingToBePlaced);

        // maybe remove this part
        if (buildingToBePlaced.getClass() == ConveyorBelt.class) {
            ConveyorBelt temp = new ConveyorBelt();
            while (temp.getOutputDirections()[0] != buildingToBePlaced.getOutputDirections()[0]) {
                temp.rotate();
            }
            temp.setRotation(buildingToBePlaced.getRotation());
            buildingToBePlaced = temp;
        } else {
            buildingToBePlaced = null;
        }
    }

    /**
     * Removes the building on the tile the player is standing on.
     */
    public void removeBuilding() {
        Building b = wGenerator.getField(posXinArray, posYinArray).getBuilding();
        if (b != null) {
            wGenerator.deleteBuilding(posXinArray, posYinArray);
            for (Entry<Item, Integer> cost : b.getCost().entrySet()) {
                inventory.replace(cost.getKey(), inventory.get(cost.getKey()) + cost.getValue());
            }
        }
    }

    /**
     * Adds the given item to the inventory.
     * 
     * @param item The item to be added to the inventory.
     * @return Returns wheter the item has been added sucessfully or not
     */
    public static boolean addItemToInventory(Item item) {
        if (item == null) {
            return true;
        }
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
     * Returns the x position of the tile the player is standing on.
     * 
     * @return The x coordinate
     */
    public int getPosX() {
        return posXinArray;
    }

    /**
     * Returns the y position of the tile the player is standing on.
     * 
     * @return The y coordinate
     */
    public int getPosY() {
        return posYinArray;
    }

    /**
     * Returns the relative x position of the player on the tile they are standing
     * on.
     * 
     * @return The relative x position<br>
     *         x is in [-0.5, 0.5)
     */
    public float getOffsetX() {
        return posXonTile;
    }

    /**
     * Returns the relative y position of the player on the tile they are standing
     * on.
     * 
     * @return The relative y position<br>
     *         y is in [-0.5, 0.5)
     */
    public float getOffsetY() {
        return posYonTile;
    }

    /**
     * Returns the movement direction of the player.
     * 
     * @return The movement direction<br>
     *         Q W E<br>
     *         A _ D<br>
     *         Y S C
     */
    public char getDirection() {
        return movementDirection;
    }

    /**
     * to be done
     * 
     * @return to be done
     */
    public Building getBuildingToBePlaced() {
        return buildingToBePlaced;
    }

    /**
     * to be done
     * 
     * @param posXinArray to be done
     * @param posYinArray to be done
     * @return to be done
     */
    public Field getField(int posXinArray, int posYinArray) {
        return wGenerator.getField(posXinArray, posYinArray);
    }

    /**
     * to be done
     * 
     * @return to be done
     */
    public int getXLengthMap() {
        return wGenerator.getXLengthMap();
    }

    /**
     * to be done
     * 
     * @return to be done
     */
    public int getYLengthMap() {
        return wGenerator.getYLengthMap();
    }

    /**
     * to be done
     * 
     * @return to be done
     */
    public HashMap<Item, Integer> getInventory() {
        return inventory;
    }

    /**
     * to be done
     * 
     * @return to be done
     */
    public boolean saveWorld() {
        try (FileWriter file = new FileWriter("SourceCode/Infoprojekt/saves/" + worldName + ".json")) {
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
     * 
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
