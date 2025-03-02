package game;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Abstract class Building defines the necessary methods and variables of a
 * building.<br>
 * The method limiting a behaviors of a subclass should be overwritten by it.
 */
public abstract class Building {
    /**
     * The rotation of the building.<br>
     * Rotation is in {0, 1, 2, 3}.
     */
    private byte rotation;

    /**
     * The inventory of the building.<br>
     * Always has five items in it (if necessary null). Items are moved one spot
     * each time the items are moved.
     */
    private LinkedList<Item> content;

    /**
     * Basic constructor of Building for super() calls.<br>
     * Initalizes the rotation and inventory of the building.
     */
    public Building() {
        rotation = 0;
        content = new LinkedList<Item>();
    }

    /**
     * Adds an item to the inventory.<br>
     * Returns whether or not the operation was sucessful. The operation is defined
     * as successfull, if and only if the last item in the inventory is null or the
     * one to be added is null.
     * 
     * @param item The item to be added to the inventory.
     * @return True if and only if the last item in the inventory or the one to be
     *         added is null. And false otherwise.
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
     * Moves the first item of this building to the given building.<br>
     * Additionally executes the building specific method on the first item in the
     * inventory before trying to move it to the next building.<br>
     * Tries to add the modified first item to the next building. If this is
     * successful finally remove the item from this buildings inventory and fill it
     * up with null.
     * 
     * @param otherBuilding The building the item should be transferred to.
     */
    public void moveItemToNextBuilding(Building otherBuilding) {
        content.addFirst(executeFunction(content.pollFirst()));
        if (otherBuilding.addItem(content.getFirst())) {
            content.removeFirst();
            content.addLast(null);
        }
    }

    /**
     * Rotates the builing by adding 1 to the rotation and making sure that the
     * result is valid.
     */
    public void rotate() {
        this.setRotation((byte) ((getRotation() + 1) % 4));
    }

    /**
     * Returns the rotation of this building.
     * 
     * @return The rotation.
     */
    public byte getRotation() {
        return rotation;
    }

    /**
     * The building specific method. Has to be implemented by subclasses to specifiy
     * the behavior of this building on items passed through it.<br>
     * Takes one item and returns the processed variant.
     * 
     * @param item The item to be modified.
     * @return The modified item.
     */
    protected abstract Item executeFunction(Item item);

    /**
     * Returns the cost of the building.<br>
     * It has to be abstract because the cost of a building is expected to be final
     * and static. It therefor can't be specified in Building. So that this method
     * can't be implemented in Building.
     * 
     * @return The cost of the building.
     */
    public abstract HashMap<Item, Integer> getCost();

    /**
     * Returns the input directions of the building.<br>
     * It has to be abstract because the input directions of a building are expected
     * to be at least static. It therefor can't be specified in Building. So that
     * this method can't be implemented in Building.
     * 
     * @return The input directions of this building. It may be 0, 1, 2, 3 or 4
     *         values.
     */
    public abstract byte[] getInputDirections();

    /**
     * Returns the output directions of the building.<br>
     * It has to be abstract because the output directions of a building are
     * expected
     * to be static in most cases. It therefor can't be specified in Building. So
     * that
     * this method can't be implemented in Building.
     * 
     * @return The output directions of this building. It may be 0, 1, 2, 3 or 4
     *         values.
     */
    public abstract byte[] getOutputDirections();

    /**
     * The set method for rotation.<br>
     * It should only be used inside of subclasses from Building to set their own
     * rotation. Otherwise unexpected behavior may occure.
     * 
     * @param pRotation The new rotation.
     */
    public void setRotation(byte pRotation) {
        rotation = pRotation;
    }
}
