package game;

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

    /**
     * Q W E
     * A _ D
     * Y S C
     */
    private char movementDirection;

    /**
     * to be done
     */
    private WorldGenerator wGenerator;

    /**
     * to be done
     */
    private Building buildingToBePlaced;

    /**
     * Instantiates a new Object of type GameController.<br>
     * Sets for example the starting position of the player.
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
     * Moves the items of for example conveyor belts to the next building.<br>
     * The movement is from the "inside outwards".
     * 
     * @param startingPoints A list of coordinates of the building nothing is moved
     *                       away from.
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
                if (posXinArray + i < 0 || posYinArray + j < 0 || maxX < posXinArray + i || maxY < posYinArray + j) {
                    continue;
                }
                wGenerator.generateTile(posXinArray + i, posYinArray + j);
            }
        }
    }

    /**
     * Sets the building that the player plans to place next.
     * 
     * @param pBuilding The type of building given by a string.
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
        // TODO: Implement bended conveyor belts
        if (buildingToBePlaced != null) {
            buildingToBePlaced.setRotation((byte) ((buildingToBePlaced.getRotation() + 1) % 4));
        }
    }

    /**
     * Finally places the building (if possible) that was chosen to be placed
     * next.<br>
     * Makes also small but necessary changes to some building types.
     */
    public void placeBuilding() {
        if (wGenerator.getField(posXinArray, posYinArray).getBuilding() != null) {
            buildingToBePlaced = null;
            return;
        }
        if (buildingToBePlaced == null) {
            return;
        }

        if (buildingToBePlaced.getClass() == Extractor.class) {
            Resource resource = wGenerator.getField(posXinArray, posYinArray).getResource();
            if (resource.getResourceID() == 0) {
                buildingToBePlaced = null;
                return;
            }
            Extractor temp = (Extractor) buildingToBePlaced;
            temp.setResourceToBeExtracted(resource);
            buildingToBePlaced = temp;
        }

        wGenerator.placeBuilding(posXinArray, posYinArray, buildingToBePlaced);
    }

    /**
     * Adds the given Item to the inventory.
     * 
     * @param item The item to be added to the inventory.
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
     * temp
     */
    public Building getBuilding(int posX, int posY) {
        return wGenerator.getField(posX, posY).getBuilding();
    }

    /**
     * temp
     */
    public Resource getResource(int posX, int posY) {
        return wGenerator.getField(posX, posY).getResource();
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
     * 
     * @return boolean to be done
     */
    public boolean saveWorld() {
        return true;
    }
}
