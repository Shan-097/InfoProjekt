package game;

import java.util.Random;
import java.lang.Math;

public class WorldGenerator {
    private Field[][] map;

    public WorldGenerator() {
        map = new Field[Integer.MAX_VALUE][Integer.MAX_VALUE];
    }

    public void generateTile(int posX, int posY) {
        if (posX < 0 || posY < 0) {
            throw new IndexOutOfBoundsException();
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

    private void chooseTileToBeginFuctionCollapse(int posX, int posY) {
        //still to be implemented
    }

    private void collapseFunctionWave(int posX, int posY) {
        if (map[posX][posY].getResource() != null) {
            return;
        }
        boolean aboveKnown = false;
        boolean belowKnown = false;
        boolean leftKnown = false;
        boolean rightKnown = false;
        byte knownTiles = 0;
        if (posY != 0 && map[posX][posY - 1] != null) {
            knownTiles++;
            aboveKnown = true;
        } else if (posY != map[0].length - 1 && map[posX][posY + 1] != null) {
            knownTiles++;
            belowKnown = true;
        }
        if (posX != 0 && map[posX - 1][posY] != null) {
            knownTiles++;
            leftKnown = true;
        } else if (posX != map.length - 1 && map[posX + 1][posY] != null) {
            knownTiles++;
            rightKnown = true;
        }
        int[] ressourcesOfTiles = new int[knownTiles];
        byte counter = 0;
        if (posY != 0 && map[posX][posY - 1] != null) {
            ressourcesOfTiles[counter] = map[posX][posY - 1].getResourceID();
            counter++;
        } else if (posY != map[0].length - 1 && map[posX][posY + 1] != null) {
            ressourcesOfTiles[counter] = map[posX][posY + 1].getResourceID();
            counter++;
        }
        if (posX != 0 && map[posX - 1][posY] != null) {
            ressourcesOfTiles[counter] = map[posX - 1][posY].getResourceID();
            counter++;
        } else if (posX != map.length - 1 && map[posX + 1][posY] != null) {
            ressourcesOfTiles[counter] = map[posX + 1][posY].getResourceID();
        }

        if (knownTiles == 1 || knownTiles == 0) {
            return;
        } else if (knownTiles == 2) {
            if (ressourcesOfTiles[0] != 0 && ressourcesOfTiles[1] != 0
                    && ressourcesOfTiles[0] == ressourcesOfTiles[1]) {
                map[posX][posY] = new Field(ressourcesOfTiles[0]);
            } else if (ressourcesOfTiles[0] != 0 && ressourcesOfTiles[1] != 0
                    && ressourcesOfTiles[0] != ressourcesOfTiles[1]) {
                map[posX][posY] = new Field(0);
            } else {
                return;
            }
        } else if (knownTiles == 3) {
            if (ressourcesOfTiles[0] != 0 && ressourcesOfTiles[1] != 0 && ressourcesOfTiles[2] != 0) {// kein
                                                                                                      // Hintergrund
                if (ressourcesOfTiles[0] == ressourcesOfTiles[1] && ressourcesOfTiles[1] == ressourcesOfTiles[2]) {
                    map[posX][posY] = new Field(ressourcesOfTiles[0]);
                } else {
                    map[posX][posY] = new Field(0);
                }
            } else if (ressourcesOfTiles[0] == 0 ^ ressourcesOfTiles[1] == 0 ^ ressourcesOfTiles[2] == 0) {// einmal
                                                                                                           // Hintergrund
                if (ressourcesOfTiles[0] == 0) {
                    if (ressourcesOfTiles[1] == ressourcesOfTiles[2]) {
                        map[posX][posY] = new Field(ressourcesOfTiles[1]);
                    } else {
                        map[posX][posY] = new Field(0);
                    }
                } else if (ressourcesOfTiles[1] == 0) {
                    if (ressourcesOfTiles[0] == ressourcesOfTiles[2]) {
                        map[posX][posY] = new Field(ressourcesOfTiles[0]);
                    } else {
                        map[posX][posY] = new Field(0);
                    }
                } else {
                    if (ressourcesOfTiles[0] == ressourcesOfTiles[1]) {
                        map[posX][posY] = new Field(ressourcesOfTiles[0]);
                    } else {
                        map[posX][posY] = new Field(0);
                    }
                }
            } else if (ressourcesOfTiles[0] == 0 && ressourcesOfTiles[1] == 0 && ressourcesOfTiles[2] == 0) {// nur
                                                                                                             // Hintergrund
                return;
            } else {// einmal kein Hintergrund
                return;
            }
        } else if (knownTiles == 4) {
            return;
            // to do
        }

        if (!aboveKnown && posY != 0) {
            collapseFunctionWave(posX, posY - 1);
        }
        if (!belowKnown && posY != map[0].length - 1) {
            collapseFunctionWave(posX, posY + 1);
        }
        if (!leftKnown && posX != 0) {
            collapseFunctionWave(posX - 1, posY);
        }
        if (!rightKnown && posX != map.length - 1) {
            collapseFunctionWave(posX + 1, posY);
        }
    }
}
