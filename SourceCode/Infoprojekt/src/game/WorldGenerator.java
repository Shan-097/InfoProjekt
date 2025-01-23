package game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.lang.Math;

/**
 * to be done
 */
public class WorldGenerator {
    private Field[][] map;

    /**
     * to be done
     */
    public WorldGenerator() {
        map = new Field[Integer.MAX_VALUE][Integer.MAX_VALUE];
    }

    /**
     * to be done
     * 
     * @param posX to be done
     * @param posY to be done
     */
    public void generateTile(int posX, int posY) {
        if (posX < 0 || posY < 0 || map.length >= posX || map[0].length >= posY) {
            throw new IndexOutOfBoundsException();
        }
        if (map[posX][posY] != null) {
            return;
        }
        chooseTileToBeginFuctionCollapse(posX, posY);
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
     * @param ressources to be done
     * @return to be done
     */
    private int generateRandomTileWeighted(int[] ressources) {
        byte countRes1 = 0, countRes2 = 0, countRes3 = 0, countRes4 = 0;
        for (byte i = 0; i < ressources.length; i++) {
            if (ressources[i] == 1) {
                countRes1++;
            } else if (ressources[i] == 2) {
                countRes2++;
            } else if (ressources[i] == 3) {
                countRes3++;
            } else if (ressources[i] == 4) {
                countRes4++;
            }
        }
        Random rnd = new Random();
        double rand = rnd.nextDouble(1011 + (Math.pow(countRes1, 0.2) + Math.pow(countRes2, 0.2)
                + Math.pow(countRes3, 0.2) + Math.pow(countRes4, 0.2)) * 600);
        if (rand < 1000) {
            return 0;
        } else if (rand < (1003.5 + Math.pow(countRes1, 0.2) * 600)) {
            return 1;
        } else if (rand < (1006.5 + (Math.pow(countRes1, 0.2) + Math.pow(countRes2, 0.2)) * 600)) {
            return 2;
        } else if (rand < (1009
                + (Math.pow(countRes1, 0.2) + Math.pow(countRes2, 0.2) + Math.pow(countRes4, 0.2)) * 600)) {
            return 3;
        }
        return 4;
    }

    /**
     * to be done
     * 
     * @param posX to be done
     * @param posY to be done
     */
    private void chooseTileToBeginFuctionCollapse(int posX, int posY) {
        // still to be implemented
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
}
