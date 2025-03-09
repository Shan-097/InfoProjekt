package game;

import java.util.HashMap;

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
        COST.put(Item.getItemWithID(0), 0);
        COST.put(Item.getItemWithID(1), 0);
        COST.put(Item.getItemWithID(2), 0);
        COST.put(Item.getItemWithID(3), 0);
        COST.put(Item.getItemWithID(4), 0);
        COST.put(Item.getItemWithID(5), 0);
        COST.put(Item.getItemWithID(6), 0);
        COST.put(Item.getItemWithID(7), 0);
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
        return COST;
    }

    /**
     * Returns the input directions of this building.
     * 
     * @return The input directions.
     */
    public byte[] getInputDirections() {
        return INPUT_DIRECTIONS;
    }

    /**
     * Returns the output directions of this building.
     * 
     * @return The output directions.
     */
    public byte[] getOutputDirections() {
        return OUTPUT_DIRECTIONS;
    }

    /**
     * Sets the item that this extractor should extract from the given resource.
     * 
     * @param pResource The resource of the item that is to be extracted.
     */
    public void setResourceToBeExtracted(Resource pResource) {
        itemToBeExtracted = Item.getItemFromResource(pResource);
    }
}
