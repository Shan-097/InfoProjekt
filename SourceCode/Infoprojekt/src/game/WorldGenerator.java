package game;

import java.util.HashSet;
import java.util.Random;

/**
 * The WorldGenerator class is responsible for generating and manipulating the
 * map.
 */
public class WorldGenerator {
    /**
     * Stores the base probabilities of the resources.<br>
     * BASE_PROBABILTIES[i] is the probability of the resource with id i.<br>
     * The sum of all entries in BASE_PROBABILITIES has to be one.
     */
    private static final double[] BASE_PROBABILITIES = new double[] { 0.95d, 0.02d, 0.015d, 0.01d, 0.005d };

    /**
     * The 2D array representing the map.<br>
     * Each entry is an object of type Field storing resources and buildings.
     */
    private Field[][] map;

    /**
     * The Constructor for WorldGenerator.<br>
     * It takes the size of the map as an input and pre generates a little area.
     *
     * @param sizeX The number of tiles in the x dimension.
     * @param sizeY The number of tiles in the y dimension.
     */
    public WorldGenerator(int sizeX, int sizeY) {
        map = new Field[sizeX][sizeY];
        int posX = sizeX / 2;
        int posY = sizeY / 2;

        for (int i = -50; i <= 50; i++) {
            for (int j = -50; j <= 50; j++) {
                if (posX + i < 0 || posY + j < 0 || map.length <= posX + i || map[0].length <= posY + j) {
                    continue;
                }
                this.generateTile(posX + i, posY + j);

                if (i <= 1 && i >= -1 && j <= 1 && j >= -1) {
                    map[posX + i][posY + j].setBuilding(new CollectionSite());
                }
            }
        }
    }

    /**
     * This method generates a specified tile (Field object of the map) and then
     * collapses the wave according to very simple rules (generates other tiles that
     * should only have one value).
     * 
     * @param posX The x coordinate of the tile to be generated.
     * @param posY The y coordinate of the tile to be generated.
     */
    public void generateTile(int posX, int posY) {
        if (posX < 0 || posY < 0 || map.length <= posX || map[0].length <= posY) {
            throw new IndexOutOfBoundsException("Can't generate tile outside of the field.");
        }
        if (map[posX][posY] != null) {
            return;
        }
        chooseResource(posX, posY);
        if (posY != 0) {
            collapseFunctionWave(posX, posY - 1);
        }
        if (posY != map[0].length - 1) {
            collapseFunctionWave(posX, posY + 1);
        }
        if (posX != 0) {
            collapseFunctionWave(posX - 1, posY);
        }
        if (posX != map.length - 1) {
            collapseFunctionWave(posX + 1, posY);
        }
    }

    /**
     * Pseudo randomly selects a resource with given probabilities for the
     * resources.<br>
     * It is expected that 5 probabilities are given and the sum should be one.
     * 
     * @param probability The probabilities for the resources. probability[i] is the
     *                    probability of the resource with id i.
     * @return Returns the id of the chosen resource.
     */
    private int generateRandomResource(double[] probability) {
        // assumes length 5
        double rnd = new Random().nextDouble();
        if (rnd < probability[0]) {
            return 0;
        } else if (rnd < probability[0] + probability[1]) {
            return 1;
        } else if (rnd < probability[0] + probability[1] + probability[2]) {
            return 2;
        } else if (rnd < probability[0] + probability[1] + probability[2] + probability[3]) {
            return 3;
        } else {
            return 4;
        }
    }

    /**
     * Chooses a resource for/ generates a specified tile according to a small set
     * of rules.<br>
     * If there is more then one option the resource is chosen pseudo randomly.
     * 
     * @param posX The x coordinate of the tile to be generated.
     * @param posY The y coordinate of the tile to be generated.
     */
    private void chooseResource(int posX, int posY) {
        if (map[posX][posY] != null) {
            return;
        }
        // it is known that at max one tile can have
        // a resource on it that isn't noResource
        int iDofKnownResource = -1;
        int countOfKnownTiles = 0;
        if (posY != 0 && map[posX][posY - 1] != null) {
            countOfKnownTiles++;
            if (map[posX][posY - 1].getResourceID() != 0) {
                iDofKnownResource = map[posX][posY - 1].getResourceID();
            }
        } else if (posY != map[0].length - 1 && map[posX][posY + 1] != null) {
            countOfKnownTiles++;
            if (map[posX][posY + 1].getResourceID() != 0) {
                iDofKnownResource = map[posX][posY + 1].getResourceID();
            }
        }
        if (posX != 0 && map[posX - 1][posY] != null) {
            countOfKnownTiles++;
            if (map[posX - 1][posY].getResourceID() != 0) {
                iDofKnownResource = map[posX - 1][posY].getResourceID();
            }
        } else if (posX != map.length - 1 && map[posX + 1][posY] != null) {
            countOfKnownTiles++;
            if (map[posX + 1][posY].getResourceID() != 0) {
                iDofKnownResource = map[posX + 1][posY].getResourceID();
            }
        }
        double[] probability = new double[5];
        if (iDofKnownResource == -1) {
            switch (countOfKnownTiles) {
                case 0:
                    probability[0] = BASE_PROBABILITIES[0];
                    probability[1] = BASE_PROBABILITIES[1];
                    probability[2] = BASE_PROBABILITIES[2];
                    probability[3] = BASE_PROBABILITIES[3];
                    probability[4] = BASE_PROBABILITIES[4];
                    break;

                case 1:
                    probability[0] = 1 - (1 - BASE_PROBABILITIES[0]) / 2;
                    probability[1] = BASE_PROBABILITIES[1] / 2;
                    probability[2] = BASE_PROBABILITIES[2] / 2;
                    probability[3] = BASE_PROBABILITIES[3] / 2;
                    probability[4] = BASE_PROBABILITIES[4] / 2;
                    break;

                case 2:
                    probability[0] = 1 - (1 - BASE_PROBABILITIES[0]) / 4;
                    probability[1] = BASE_PROBABILITIES[1] / 4;
                    probability[2] = BASE_PROBABILITIES[2] / 4;
                    probability[3] = BASE_PROBABILITIES[3] / 4;
                    probability[4] = BASE_PROBABILITIES[4] / 4;
                    break;

                case 3:
                    probability[0] = 1 - (1 - BASE_PROBABILITIES[0]) / 8;
                    probability[1] = BASE_PROBABILITIES[1] / 8;
                    probability[2] = BASE_PROBABILITIES[2] / 8;
                    probability[3] = BASE_PROBABILITIES[3] / 8;
                    probability[4] = BASE_PROBABILITIES[4] / 8;
                    break;

                case 4:
                    probability[0] = 1;
                    break;
            }
        } else {
            double baseProbFoundRes = 0;
            switch (iDofKnownResource) {
                case 1:
                    baseProbFoundRes = 2 * BASE_PROBABILITIES[1];
                    break;

                case 2:
                    baseProbFoundRes = 3 * BASE_PROBABILITIES[2];
                    break;

                case 3:
                    baseProbFoundRes = 4 * BASE_PROBABILITIES[3];
                    break;

                case 4:
                    baseProbFoundRes = 5 * BASE_PROBABILITIES[4];
                    break;
            }

            switch (countOfKnownTiles - 1) {
                case 0:
                    probability[0] = 1 - 5 * baseProbFoundRes;
                    probability[iDofKnownResource] = 5 * baseProbFoundRes;
                    break;

                case 1:
                    probability[0] = 1 - 8 * baseProbFoundRes;
                    probability[iDofKnownResource] = 8 * baseProbFoundRes;
                    break;

                case 2:
                    probability[0] = 1 - 10 * baseProbFoundRes;
                    probability[iDofKnownResource] = 10 * baseProbFoundRes;
                    break;

                case 3:
                    probability[0] = 1 - 15 * baseProbFoundRes;
                    probability[iDofKnownResource] = 15 * baseProbFoundRes;
                    break;
            }
        }
        map[posX][posY] = new Field(generateRandomResource(probability));
    }

    /**
     * Generates the resource for a specified tile if there is only one
     * possibility.<br>
     * It recursively calls itself on neighbouring tiles.
     * 
     * @param posX The x coordinate of the tile to be generated.
     * @param posY The y coordinate of the tile to be generated.
     */
    private void collapseFunctionWave(int posX, int posY) {
        if (map[posX][posY] != null) {
            return;
        }
        byte tilesWithResources = 0;
        HashSet<Integer> neighboringResources = new HashSet<Integer>(4);
        if (posY != 0 && map[posX][posY - 1] != null) {
            if (map[posX][posY - 1].getResourceID() != 0) {
                neighboringResources.add(map[posX][posY - 1].getResourceID());
                tilesWithResources++;
            }
        } else if (posY != map[0].length - 1 && map[posX][posY + 1] != null) {
            if (map[posX][posY + 1].getResourceID() != 0) {
                neighboringResources.add(map[posX][posY + 1].getResourceID());
                tilesWithResources++;
            }
        }
        if (posX != 0 && map[posX - 1][posY] != null) {
            if (map[posX - 1][posY].getResourceID() != 0) {
                neighboringResources.add(map[posX - 1][posY].getResourceID());
                tilesWithResources++;
            }
        } else if (posX != map.length - 1 && map[posX + 1][posY] != null) {
            if (map[posX + 1][posY].getResourceID() != 0) {
                neighboringResources.add(map[posX + 1][posY].getResourceID());
                tilesWithResources++;
            }
        }

        if (tilesWithResources < 2) {
            return;
        } else {
            if (neighboringResources.size() == 1) {
                map[posX][posY] = new Field((int) neighboringResources.toArray()[0]);
            } else {
                map[posX][posY] = new Field(0);
            }
        }

        if (posY != 0) {
            collapseFunctionWave(posX, posY - 1);
        }
        if (posY != map[0].length - 1) {
            collapseFunctionWave(posX, posY + 1);
        }
        if (posX != 0) {
            collapseFunctionWave(posX - 1, posY);
        }
        if (posX != map.length - 1) {
            collapseFunctionWave(posX + 1, posY);
        }
    }

    /**
     * Returns a Field specified by its coordinates.
     * 
     * @param posX The x coordinate of the tile.
     * @param posY The y coordinate of the tile.
     * @return The Field object of the tile.
     */
    public Field getField(int posX, int posY) {
        if (posX < 0 || posY < 0 || map.length <= posX || map[0].length <= posY) {
            throw new IndexOutOfBoundsException("Can't generate tile outside of the field.");
        }
        return map[posX][posY];
    }

    /**
     * Returns the map.
     * 
     * @return The map
     */
    public Field[][] getMap() {
        return map;
    }

    /**
     * Returns the width of the map.
     * 
     * @return The width.
     */
    public int getXLengthMap() {
        return map.length;
    }

    /**
     * Returns the height of the map.
     * 
     * @return The height.
     */
    public int getYLengthMap() {
        return map[0].length;
    }

    /**
     * Sets the building of one specified tile.
     * 
     * @param posX The x coordinate of the tile.
     * @param posY The y coordinate of the tile.
     * @param b    The Building to be placed on the tile.
     */
    public void placeBuilding(int posX, int posY, Building b) {
        map[posX][posY].setBuilding(b);
    }

    /**
     * Removes the building on one specified tile.
     * 
     * @param posX The x coordinate of the tile.
     * @param posY The y coordinate of the tile.
     */
    public void deleteBuilding(int posX, int posY) {
        map[posX][posY].setBuilding(null);
    }
}
