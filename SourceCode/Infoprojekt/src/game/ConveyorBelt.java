package game;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * The conveyor belt is a building for item transportation.
 */
public class ConveyorBelt extends Building {
    /**
     * The cost of every conveyor belt.
     */
    private static final HashMap<Item, Integer> COST;

    /**
     * The input directions of every conveyor belt without regards to its rotation.
     */
    private final static byte[] INPUT_DIRECTIONS;

    /**
     * The output directions of every conveyor belt without regards to its rotation
     * as specified in Building but with regards to how it is bended.
     */
    private byte[] outputDirections;

    /**
     * The static and final attributes are declared here.
     */
    static {
        COST = new HashMap<Item, Integer>(4);
        COST.put(Item.getItemWithID(0), 0);
        COST.put(Item.getItemWithID(1), 0);
        COST.put(Item.getItemWithID(2), 0);
        COST.put(Item.getItemWithID(3), 0);
        COST.put(Item.getItemWithID(4), 0);
        COST.put(Item.getItemWithID(5), 0);
        COST.put(Item.getItemWithID(6), 0);
        COST.put(Item.getItemWithID(7), 0);
        INPUT_DIRECTIONS = new byte[] { 0 };
    }

    /**
     * The constructor of ConveyorBelt is calling the super constructor and setting
     * the standard output direction.
     */
    public ConveyorBelt() {
        super();
        outputDirections = new byte[] { 2 };
    }

    /**
     * The constructor of ConveyorBelt for cloning an object.
     * 
     * @param rotation The rotation
     * @param inventory The inventory
     */
    private ConveyorBelt(byte rotation, LinkedList<Item> inventory, byte[] pOutputDirections) {
        super(rotation, inventory);
        outputDirections = pOutputDirections;
    }

    /**
     * As this building type isn't modifying any items it just returns the given
     * item.
     * 
     * @param item The item to be "modified".
     * @return The "modified" item.
     */
    protected Item executeFunction(Item item) {
        return item;
    }

    /**
     * Returns the cost of this building.
     * 
     * @return The cost of this building.
     */
    public HashMap<Item, Integer> getCost() {
        return (HashMap<Item, Integer>) COST.clone();
    }

    /**
     * Returns the input directions of this building.
     * 
     * @return The input directions.
     */
    public byte[] getInputDirections() {
        return INPUT_DIRECTIONS.clone();
    }

    /**
     * Returns the output directions of this building.
     * 
     * @return The output directions.
     */
    public byte[] getOutputDirections() {
        return outputDirections.clone();
    }

    /**
     * Overwrites the rotate method from building to account for the possibility to
     * bend a conveyor belt.
     */
    @Override
    public void rotate() {
        if (outputDirections[0] >= 3) {
            this.setRotation((byte) ((getRotation() + 1) % 4));
            outputDirections[0] = 1;
        } else {
            outputDirections[0]++;
        }
    }

    /**
     * Clones the object so that the original can't be modified but the values can
     * still be used.<br>
     * 
     * @return The cloned building
     */
    @Override
    public Building clone() {
        return new ConveyorBelt(this.getRotation(), this.getInventory(), this.getOutputDirections());
    }
}
