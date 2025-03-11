package game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

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
    private HashSet<Integer> movedIndices;

    /**
     * to be done
     */
    public Building() {
        rotation = 0;
        content = new LinkedList<Item>();
        for (int i = 0; i < 5; i++) {
            content.add(null);
        }
        movedIndices = new HashSet<>(5);
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
            content.addLast(item);
            movedIndices.add(4);
            return true;
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
            movedIndices.clear();
            for (int i = 0; i < 5; i++) {
                movedIndices.add(i);
            }
        } else {
            otherBuilding.moveItemInsideBuilding();
        }
    }

    /**
     * to be done
     */
    public void moveItemInsideBuilding() {
        movedIndices.clear();
        for (int i = 0; i < 5; i++) {
            if (content.getFirst() == null) {
                content.remove(i);
                content.addLast(null);
                for (int j = i + 1; j < 5; j++) {
                    movedIndices.add(j);
                }
                return;
            }
        }
    }

    /**
     * to be done
     */
    public void rotate() {
        this.setRotation((byte) ((getRotation() + 1) % 4));
    }

    /**
     * temp remove later (exists on other branch already)
     */
    public LinkedList<Item> getInventory() {
        return content;
    }

    /**
     * to be done
     * 
     * @return byte to be done
     */
    public byte getRotation() {
        return rotation;
    }

    public HashSet<Integer> getMovedItems() {
        return movedIndices;
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
