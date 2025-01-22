package game;

import java.util.HashMap;

public abstract class Building {
    private byte rotation;
    private HashMap<Item, Integer> content;

    public Building() {
        rotation = 0;
    }

    public byte getRotation() {
        return rotation;
    }

    public abstract HashMap<Integer, Integer> getCost();

    public abstract byte[] getInputDirections();

    public abstract byte[] getOutputDirections();

    public void setRotation(byte pRotation) {
        rotation = pRotation;
    }
}
