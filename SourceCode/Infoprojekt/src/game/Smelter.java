package game;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * The smelter is a building that turns passing through items into their semlted
 * variant.
 */
public class Smelter extends Building {
    /**
     * The cost of every smelter.
     */
    private static final HashMap<Item, Integer> COST;

    /**
     * The input directions of every smelter without regards to its rotation.
     */
    private static final byte[] INPUT_DIRECTIONS;

    /**
     * The output directions of every smelter without regards to its rotation.
     */
    private static final byte[] OUTPUT_DIRECTIONS;

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
        OUTPUT_DIRECTIONS = new byte[] { 2 };
    }

    /**
     * The empty constructor of Smelter is only calling the super constructor.
     */
    public Smelter() {
        super();
    }

    /**
     * The constructor of Smelter for cloning an object.
     * 
     * @param rotation The rotation
     * @param inventory The inventory
     */
    private Smelter(byte rotation, LinkedList<Item> inventory) {
        super(rotation, inventory);
    }

    /**
     * As this building smelts items it returns the smelted variant of the given one.<br>
     * null is defined as the smelted item of null.
     * 
     * @param item The item to be smelted
     * @return The smelted item
     */
    public Item executeFunction(Item item) {
        try {
            return Item.getSmeltedItem(item);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Returns the cost of this building.
     * 
     * @return The cost of this building
     */
    public HashMap<Item, Integer> getCost() {
        return COST;
    }

    /**
     * Returns the input directions of this building.
     * 
     * @return The input directions
     */
    public byte[] getInputDirections() {
        return INPUT_DIRECTIONS;
    }

    /**
     * Returns the output directions of this building.
     * 
     * @return The output directions
     */
    public byte[] getOutputDirections() {
        return OUTPUT_DIRECTIONS;
    }
    
    /**
     * Clones the object so that the original can't be modified but the values can
     * still be used.<br>
     * 
     * @return The cloned building
     */
    @Override
    public Building clone(){
        return new Smelter(this.getRotation(), this.getInventory());
    }
}
