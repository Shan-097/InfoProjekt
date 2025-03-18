package game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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
    private String worldName = "savedWorld";

    /**
     * A temporary Building object storing what the player plans to place next.
     */
    private Building buildingToBePlaced;

    /**
     * Instantiates a new Object of type GameController.<br>
     * Sets for example the starting position of the player.
     * 
     * @param pWorldName to be done
     * @param createNewGame to be done
     * @throws IllegalArgumentException to be done
     */
    public GameController(String pWorldName, boolean createNewGame) throws IllegalArgumentException {
        if (pWorldName == null) {
            throw new IllegalArgumentException("The world name should not be null.");
        }

        worldName = pWorldName;

        inventory = new HashMap<Item, Integer>();
        for (int i = 0; i < 8; i++) {
            try {
                inventory.put(Item.getItemWithID(i), 0);
            } catch (IllegalArgumentException e) {
            }
        }

        try {
            wGenerator = new WorldGenerator(101, 101);
        } catch (IllegalArgumentException e) {
            throw e;
        }

        posXinArray = wGenerator.getXLengthMap() / 2;
        posYinArray = wGenerator.getYLengthMap() / 2;
        posXonTile = 0;
        posYonTile = 0;
        movementDirection = 'S';
    }

    /**
     * to be done
     * 
     * @param pFilePath to be done
     * @throws IllegalArgumentException to be done
     */
    public GameController(String pFilePath) throws IllegalArgumentException {
        if (pFilePath == null) {
            throw new IllegalArgumentException("If the world is loaded from a file the path should not be null.");
        }
    
        try {
            JSONObject savedWorld = readJsonFile(pFilePath);
    
            // Load the worldName
            worldName = savedWorld.getString("worldName");
    
            // Load the players position
            posXinArray = savedWorld.getInt("posX");
            posYinArray = savedWorld.getInt("posY");
            posXonTile = (float) savedWorld.getDouble("offsetX");
            posYonTile = (float) savedWorld.getDouble("offsetY");
    
            // Load the movement direction of the player
            movementDirection = savedWorld.getString("movementDirection").charAt(0);
    
            // Load the player inventory
            inventory = new HashMap<>();
            JSONArray inventoryArray = savedWorld.getJSONArray("inventory");
            for (int i = 0; i < inventoryArray.length(); i++) {
                JSONObject itemObject = inventoryArray.getJSONObject(i);
                Item item = Item.getItemWithID(itemObject.getInt("itemID"));
                int quantity = itemObject.getInt("quantity");
                inventory.put(item, quantity);
            }
    
            // Load the worldMap
            JSONArray worldMap = savedWorld.getJSONArray("worldMap");
            int rows = worldMap.length();
            int cols = worldMap.getJSONArray(0).length();
            Field[][] fieldArray = new Field[rows][cols];
    
            for (int i = 0; i < rows; i++) {
                JSONArray rowArray = worldMap.getJSONArray(i);
                for (int j = 0; j < cols; j++) {
                    if (!rowArray.isNull(j)) {
                        if(rowArray.getJSONObject(j).has("resource") && rowArray.getJSONObject(j).has("building")) {
                            fieldArray[i][j] = new Field(
                                getBuildingFromType(rowArray.getJSONObject(j).getJSONObject("building").getString("type"), rowArray.getJSONObject(j).getJSONObject("building")),
                                Resource.getResourceWithID(rowArray.getJSONObject(j).getJSONObject("resource").getInt("resourceID"))
                            );
                        } else if(rowArray.getJSONObject(j).has("resource")) {
                            fieldArray[i][j] = new Field(
                                rowArray.getJSONObject(j).getJSONObject("resource").getInt("resourceID"));
                        }
                    }
                }
            }
    
            // Initialize the worldGenerator with the loaded fieldArray
            wGenerator = new WorldGenerator(fieldArray);
    
        } catch (Exception e) {
            throw new IllegalArgumentException("Something is wrong with the input file or something went wrong while loading it: " + e.getMessage());
        }
    }

    /**
     * Makes the changes to the world that are directely timed by ticks.<br>
     * E.g. moves the items on the conveyor belts.
     */
    public void update() {
        try {
            moveItems(getStartingPoints());
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Helper method to get the building object from the type and the JSONObject.
     * 
     * @param buildingType The type of the building as a string.
     * @param buildingObject The JSONObject containing the building.
     * @return The building object.
     */
    private Building getBuildingFromType(String buildingType, JSONObject buildingObject) {
        return switch (buildingType) {
            case "collectionSite" -> new CollectionSite(
                buildingObject.getInt("rotation"),
                parseContent(buildingObject.getJSONArray("content"))
            );
            case "conveyorBelt" -> new ConveyorBelt(
                buildingObject.getInt("rotation"),
                parseContent(buildingObject.getJSONArray("content")),
                parseOutputDirections(buildingObject.getJSONArray("outputDirections"))
            );
            case "extractor" -> new Extractor(
                buildingObject.getInt("rotation"),
                parseContent(buildingObject.getJSONArray("content")),
                buildingObject.getInt("itemToBeExtracted")
            );
            case "smelter" -> new Smelter(
                buildingObject.getInt("rotation"),
                parseContent(buildingObject.getJSONArray("content"))
            );
            default -> null;
        };
    }

    /**
     * Helper method to parse the content from a JSONArray.
     * 
     * @param jsonArray The JSONArray containing the content.
     * @return A LinkedList of Items.
     */
    private static LinkedList<Item> parseContent(JSONArray jsonArray) {
        LinkedList<Item> content = new LinkedList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.isNull(i)) {
                content.add(null);
            } else {
                content.add(Item.getItemWithID(jsonArray.getInt(i)));
            }
        }
        return content;
    }

    /**
     * Helper method to parse the output directions from a JSONArray.
     * 
     * @param jsonArray The JSONArray containing the output directions.
     * @return A byte array of output directions.
     */
    private static byte[] parseOutputDirections(JSONArray jsonArray) {
        byte[] outputDirections = new byte[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            outputDirections[i] = (byte) jsonArray.getInt(i);
        }
        return outputDirections;
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
                Building b;
                try {
                    b = wGenerator.getField(i, j).getBuilding();
                } catch (IllegalArgumentException e) {
                    continue;
                }
                if (b != null) {
                    buildingList.add(new Tuple(i, j));
                }
            }
        }

        ArrayList<Tuple> listOfStartingPoints = (ArrayList<Tuple>) buildingList.clone();
        for (Tuple tuple : buildingList) {
            int x = tuple.getA();
            int y = tuple.getB();
            Building b;
            byte[] inputDirectionsOfBuilding;
            byte rotation;
            try {
                b = wGenerator.getField(x, y).getBuilding();
                inputDirectionsOfBuilding = b.getInputDirections();
                rotation = b.getRotation();
            } catch (Exception e) {
                continue;
            }

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

                byte[] outputDirectionsOfOtherBuilding;
                byte rotationOfOtherBuilding;
                try {
                    Building b2 = wGenerator.getField(newX, newY).getBuilding();
                    outputDirectionsOfOtherBuilding = b2.getOutputDirections();
                    rotationOfOtherBuilding = b2.getRotation();
                } catch (Exception e) {
                    continue;
                }

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
     * @throws IllegalArgumentException to be done
     */
    private void moveItems(ArrayList<Tuple> startingPoints) throws IllegalArgumentException {
        if (startingPoints == null) {
            throw new IllegalArgumentException("The list of coordinates is null.");
        }

        for (Tuple point : startingPoints) {
            int x = point.getA();
            int y = point.getB();
            try {
                wGenerator.getField(x, y).getBuilding().moveItemInsideBuilding();
            } catch (Exception e) {
                throw new IllegalArgumentException(
                        "One of the given coordinates is not valid or there is no building at this point.");
            }
        }

        while (true) {
            if (startingPoints.size() == 0) {
                return;
            }
            int x = startingPoints.get(0).getA();
            int y = startingPoints.get(0).getB();
            Building b;
            try {
                b = wGenerator.getField(x, y).getBuilding();
            } catch (Exception e) {
                continue;
            }
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

                Building b2;
                byte[] outputDirectionsOfOtherBuilding;
                byte rotationOfOtherBuilding;
                try {
                    b2 = wGenerator.getField(newX, newY).getBuilding();
                    outputDirectionsOfOtherBuilding = b2.getOutputDirections();
                    rotationOfOtherBuilding = b2.getRotation();
                } catch (Exception e) {
                    continue;
                }

                for (int j = 0; j < outputDirectionsOfOtherBuilding.length; j++) {
                    if ((outputDirectionsOfOtherBuilding[j] + rotationOfOtherBuilding + 2) % 4 == direction) {
                        b2.moveItemToNextBuilding(b);
                        startingPoints.add(new Tuple(newX, newY));
                        break;
                    }
                }
            }
            startingPoints.remove(0);
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
     * @throws IllegalArgumentException to be done
     */
    public void movePlayer(char direction) throws IllegalArgumentException {
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
                try {
                    wGenerator.generateTile(posXinArray + i, posYinArray + j);
                } catch (IllegalArgumentException e) {
                    continue;
                }
            }
        }
    }

    /**
     * Sets the building that the player plans to place next.
     * 
     * @param buildingType The type of building given by a string.
     * @throws IllegalArgumentException to be done
     */
    public void chooseBuildingToPlace(String buildingType) throws IllegalArgumentException {
        if (buildingType == null) {
            throw new IllegalArgumentException("The given builting type is null.");
        }
        switch (buildingType) {
            case "ConveyorBelt":
                buildingToBePlaced = new ConveyorBelt();
                return;

            case "Extractor":
                buildingToBePlaced = new Extractor();
                return;

            case "Smelter":
                buildingToBePlaced = new Smelter();
                return;
        }
        throw new IllegalArgumentException("The given builting type is not known.");
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
        try {
            if (wGenerator.getField(posXinArray, posYinArray).getBuilding() != null) {
                return;
            }
        } catch (Exception e) {
            posXinArray = wGenerator.getXLengthMap() / 2;
            posYinArray = wGenerator.getYLengthMap() / 2;
            posXonTile = 0;
            posYonTile = 0;
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
            Resource resource;
            try {
                resource = wGenerator.getField(posXinArray, posYinArray).getResource();
            } catch (Exception e) {
                posXinArray = wGenerator.getXLengthMap() / 2;
                posYinArray = wGenerator.getYLengthMap() / 2;
                posXonTile = 0;
                posYonTile = 0;
                return;
            }
            if (resource == null || resource.getResourceID() == 0) {
                return;
            }
            Extractor temp = (Extractor) buildingToBePlaced;
            try {
                temp.setResourceToBeExtracted(resource);
            } catch (IllegalArgumentException e) {
                return;
            }
            buildingToBePlaced = temp;
        }

        for (Entry<Item, Integer> cost : buildingToBePlaced.getCost().entrySet()) {
            inventory.replace(cost.getKey(), inventory.get(cost.getKey()) - cost.getValue());
        }

        try {
            wGenerator.placeBuilding(posXinArray, posYinArray, buildingToBePlaced);
        } catch (IllegalArgumentException e) {
            return;
        }

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
        Building b;
        try {
            b = wGenerator.getField(posXinArray, posYinArray).getBuilding();
        } catch (Exception e) {
            posXinArray = wGenerator.getXLengthMap() / 2;
            posYinArray = wGenerator.getYLengthMap() / 2;
            posXonTile = 0;
            posYonTile = 0;
            return;
        }
        if (b != null) {
            try {
                wGenerator.deleteBuilding(posXinArray, posYinArray);
            } catch (Exception e) {
                posXinArray = wGenerator.getXLengthMap() / 2;
                posYinArray = wGenerator.getYLengthMap() / 2;
                posXonTile = 0;
                posYonTile = 0;
                return;
            }
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
     * @throws IllegalArgumentException to be done
     */
    public static boolean addItemToInventory(Item item) throws IllegalArgumentException {
        if (item == null) {
            return true;
        }
        if (!inventory.containsKey(item)) {
            throw new IllegalArgumentException("Unknown item");
        }
        if (inventory.get(item) == Integer.MAX_VALUE) {
            return false;
        }
        inventory.replace(item, inventory.get(item) + 1);
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
     * Returns the building that is planned to be placed next.
     * 
     * @return The building object
     */
    public Building getBuildingToBePlaced() {
        return buildingToBePlaced;
    }

    /**
     * Returns the specified Field object of the map.
     * 
     * @param posXinArray The x coordinate in the map
     * @param posYinArray The y coordinate in the map
     * @return The Field object
     * @throws IllegalArgumentException to be done
     */
    public Field getField(int posXinArray, int posYinArray) throws IllegalArgumentException {
        try {
            return wGenerator.getField(posXinArray, posYinArray);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Returns how many fields are in one line of the map.<br>
     * Aquivalent to the length of the array.
     * 
     * @return The length
     */
    public int getXLengthMap() {
        return wGenerator.getXLengthMap();
    }

    /**
     * Returns how many fields are in one column of the map.<br>
     * Aquivalent to the length of the array.
     * 
     * @return The length
     */
    public int getYLengthMap() {
        return wGenerator.getYLengthMap();
    }

    /**
     * Returns the inventory of the player.
     * 
     * @return The inventory as a HashMap
     */
    public HashMap<Item, Integer> getInventory() {
        return inventory;
    }

    /**
     * Method to save the current world state to a JSON file.
     * 
     * @return Returns whether the save was successful or not
     */
    public boolean saveWorld() {
        try (FileWriter file = new FileWriter("SourceCode/Infoprojekt/saves/" + worldName + ".json")) {
            JSONObject properties = new JSONObject();

            properties.put("worldName", worldName);

            properties.put("posX", String.valueOf(posXinArray));
            properties.put("posY", String.valueOf(posYinArray));

            properties.put("offsetX", posXonTile);
            properties.put("offsetY", posYonTile);

            properties.put("movementDirection", String.valueOf(movementDirection));

            JSONArray inventoryArray = new JSONArray();
            for (Entry<Item, Integer> entry : inventory.entrySet()) {
                JSONObject itemObject = new JSONObject();
                itemObject.put("itemID", entry.getKey().getItemID());
                itemObject.put("quantity", entry.getValue());
                inventoryArray.put(itemObject);
            }
            properties.put("inventory", inventoryArray);

            JSONArray jsonMap = wGenerator.getJSONMap();

            properties.put("worldMap", jsonMap);

            file.write(properties.toString(0));

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method to read a JSON file and return it as a JSONObject.
     * 
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
