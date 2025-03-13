package game;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * The extractor is a building for item extraction from resource tiles.
 */
public class Extractor extends Building {
    /**
     * The cost of every extractor.
     */
    private static final HashMap<Item, Integer> COST;

    /**
     * The input directions of every extractor without regards to its rotation.
     */
    private static final byte[] INPUT_DIRECTIONS;

    /**
     * The output directions of every extractor without regards to its rotation.
     */
    private static final byte[] OUTPUT_DIRECTIONS;

    /**
     * The item this extractor is extracting.
     */
    private Item itemToBeExtracted;

    /**
     * The static and final attributes are declared here.
     */
    static {
        COST = new HashMap<Item, Integer>(4);
        for (int i = 0; i < 8; i++) {
            try {
                COST.put(Item.getItemWithID(i), 0);
            } catch (Exception e) {
            }
        }
        INPUT_DIRECTIONS = new byte[0];
        OUTPUT_DIRECTIONS = new byte[] { 2 };
    }

    /**
     * The empty constructor of Extractor is only calling the super constructor.
     */
    public Extractor() {
        super();
    }

    /**
     * The constructor of Extractor for cloning an object.
     * 
     * @param rotation The rotation
     * @param inventory The inventory
     * @throws IllegalArgumentException to be done
     */
    private Extractor(byte rotation, LinkedList<Item> inventory, Item pItemToBeExtracted) throws IllegalArgumentException {
        super(rotation, inventory);
        if (pItemToBeExtracted == null) {
            throw new IllegalArgumentException("The item to be extracted can't be null.");
        }
        itemToBeExtracted = pItemToBeExtracted;
    }

    /**
     * This method creates items as its function and therefor returns the item the
     * extractor is extracting.<br>
     * The parameter is not used but has to exist.
     * 
     * @param item The item the function is called upon.
     * @return The item the extractor is extracting.
     */
    protected Item executeFunction(Item item) {
        return itemToBeExtracted;
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
        return OUTPUT_DIRECTIONS.clone();
    }

    /**
     * Sets the item that this extractor should extract from the given resource.
     * 
     * @param pResource The resource of the item that is to be extracted.
     * @throws IllegalArgumentException to be done
     */
    public void setResourceToBeExtracted(Resource pResource) throws IllegalArgumentException {
        try {
            itemToBeExtracted = Item.getItemFromResource(pResource);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Clones the object so that the original can't be modified but the values can
     * still be used.<br>
     * 
     * @return The cloned building or null if something went wrong.
     */
    @Override
    public Building clone(){
        try {
            return new Extractor(this.getRotation(), this.getInventory(), this.itemToBeExtracted);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
