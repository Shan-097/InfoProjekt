package game;

import java.util.HashSet;
import java.util.Random;

/**
 * to be done
 */
public class WorldGenerator {
    /**
     * to be done
     */
    private static final double baseProbRes0 = 0.95d;

    /**
     * to be done
     */
    private static final double baseProbRes1 = 0.02d;

    /**
     * to be done
     */
    private static final double baseProbRes2 = 0.015d;

    /**
     * to be done
     */
    private static final double baseProbRes3 = 0.01d;

    /**
     * to be done
     */
    private static final double baseProbRes4 = 0.005d;

    /**
     * to be done
     */
    private Field[][] map;

    /**
     * to be done
     */
    public WorldGenerator() {
        map = new Field[1000][1000];
    }

    /**
     * to be done
     * 
     * @param posX to be done
     * @param posY to be done
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
     * to be done
     * 
     * @param probability to be done
     * @return to be done
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
     * to be done
     * 
     * @param posX to be done
     * @param posY to be done
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
                    probability[0] = baseProbRes0;
                    probability[1] = baseProbRes1;
                    probability[2] = baseProbRes2;
                    probability[3] = baseProbRes3;
                    probability[4] = baseProbRes4;
                    break;

                case 1:
                    probability[0] = 1 - (1 - baseProbRes0) / 2;
                    probability[1] = baseProbRes1 / 2;
                    probability[2] = baseProbRes2 / 2;
                    probability[3] = baseProbRes3 / 2;
                    probability[4] = baseProbRes4 / 2;
                    break;

                case 2:
                    probability[0] = 1 - (1 - baseProbRes0) / 4;
                    probability[1] = baseProbRes1 / 4;
                    probability[2] = baseProbRes2 / 4;
                    probability[3] = baseProbRes3 / 4;
                    probability[4] = baseProbRes4 / 4;
                    break;

                case 3:
                    probability[0] = 1 - (1 - baseProbRes0) / 8;
                    probability[1] = baseProbRes1 / 8;
                    probability[2] = baseProbRes2 / 8;
                    probability[3] = baseProbRes3 / 8;
                    probability[4] = baseProbRes4 / 8;
                    break;

                case 4:
                    probability[0] = 1;
                    break;
            }
        } else {
            double baseProbFoundRes = 0;
            switch (iDofKnownResource) {
                case 1:
                    baseProbFoundRes = baseProbRes1;
                    break;

                case 2:
                    baseProbFoundRes = baseProbRes2;
                    break;

                case 3:
                    baseProbFoundRes = baseProbRes3;
                    break;

                case 4:
                    baseProbFoundRes = baseProbRes4;
                    break;
            }

            switch (countOfKnownTiles - 1) {
                case 0:
                    probability[0] = 1 - 10 * baseProbFoundRes;
                    probability[iDofKnownResource] = 10 * baseProbFoundRes;
                    break;

                case 1:
                    probability[0] = 1 - 6 * baseProbFoundRes;
                    probability[iDofKnownResource] = 8 * baseProbFoundRes;
                    break;

                case 2:
                    probability[0] = 1 - 3 * baseProbFoundRes;
                    probability[iDofKnownResource] = 3 * baseProbFoundRes;
                    break;

                case 3:
                    probability[0] = 1 - baseProbFoundRes;
                    probability[iDofKnownResource] = baseProbFoundRes;
                    break;
            }
        }
        map[posX][posY] = new Field(generateRandomResource(probability));
    }

    /**
     * to be done
     * 
     * @param posX to be done
     * @param posY to be done
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
     * to be done
     * 
     * @return Field to be done
     */
    public Field getField(int posX, int posY) {
        if (posX < 0 || posY < 0 || map.length <= posX || map[0].length <= posY) {
            throw new IndexOutOfBoundsException("Can't generate tile outside of the field.");
        }
        return map[posX][posY];
    }

    /**
     * to be done
     * 
     * @return int to be done
     */
    public int getXLengthMap() {
        return map.length;
    }

    /**
     * to be done
     * 
     * @return int to be done
     */
    public int getYLengthMap() {
        return map[0].length;
    }
}
