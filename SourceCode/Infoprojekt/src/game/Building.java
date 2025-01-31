package game;

import java.util.HashMap;
import java.util.LinkedList;

public abstract class Building {
    private byte rotation;
    private LinkedList<Item> content;

    public Building() {
        rotation = 0;
        content = new LinkedList<Item>();
    }

    public boolean addItem(Item item){
        if (content.size() > 4) {
            return false;
        }
        return content.add(item);
    }

    public void moveItemToNextBuilding(Building otherBuilding){
        if (content.size() != 0){
            if(otherBuilding.addItem(content.getFirst())){
                content.removeFirst();
            }
        }
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
