package game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * to be done
 */
public abstract class Building {
    /**
     * to be done
     */
    private byte rotation;

    /**
     * to be done
     */
    private LinkedList<Item> content;

    /**
     * to be done
     */
    public Building() {
        rotation = 0;
        //content = new LinkedList<Item>(List.of(null,null,null,null,null));
        content = new LinkedList<Item>();
        for (int i = 0; i < 5; i++) 
            content.add(null);
    }

    /**
     * to be done
     * 
     * @param item to be done
     */
    public boolean addItem(Item item) {
        if (item == null) {
            return true;
        }
        if (content.getLast() == null) {
            content.removeLast();
            return content.add(item);
        }
        return false;
    }

    /**
     * to be done
     * 
     * @param otherBuilding to be done
     */
    public void moveItemToNextBuilding(Building otherBuilding) {
        content.addFirst(executeFunction(content.pollFirst()));
        if (otherBuilding.addItem(content.getFirst())) {
            content.removeFirst();
            content.addLast(null);
        }
    }

    /**
     * to be done
     */
    public void rotate() {
        this.setRotation((byte) ((getRotation() + 1) % 4));
    }

    /**
     * to be done
     * 
     * @return byte to be done
     */
    public byte getRotation() {
        return rotation;
    }

    /**
     * to be done
     * 
     * @param item to be done
     * @return Item to be done
     */
    protected abstract Item executeFunction(Item item);

    /**
     * to be done
     * 
     * @return to be done
     */
    public abstract HashMap<Item, Integer> getCost();

    /**
     * to be done
     * 
     * @return to be done
     */
    public abstract byte[] getInputDirections();

    /**
     * to be done
     * 
     * @return to be done
     */
    public abstract byte[] getOutputDirections();

    /**
     * to be done
     * 
     * @param pRotation to be done
     */
    public void setRotation(byte pRotation) {
        rotation = pRotation;
    }
}
