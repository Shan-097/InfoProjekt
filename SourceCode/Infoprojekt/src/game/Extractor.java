package game;

import java.util.HashMap;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

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
     * Constructor for Extractor that initializes the object with saved values.
     * 
     * @param rotation         The rotation of the extractor.
     * @param content          The inventory of the extractor.
     * @param outputDirections The output directions of the extractor.
     */
    public Extractor(int rotation, LinkedList<Item> content, int itemToBeExtracted) {
        super(rotation, content); // Call the parent constructor with rotation and content
        
        this.itemToBeExtracted = Item.getItemWithID(itemToBeExtracted);
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
     * to be done
     * 
     * @return to be done
     */
    @Override
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rotation", rotation);
        jsonObject.put("content", new JSONArray(content));
        jsonObject.put("type", "extractor");

        jsonObject.put("inputDirections", new JSONArray(INPUT_DIRECTIONS));
        jsonObject.put("outputDirections", new JSONArray(OUTPUT_DIRECTIONS));

        jsonObject.put("itemToBeExtracted", itemToBeExtracted.getItemID());

        JSONObject costObject = new JSONObject();
        for (Item item : COST.keySet()) {
            costObject.put(String.valueOf(item.getItemID()), COST.get(item));
        }
        jsonObject.put("cost", costObject);

        return jsonObject;
    }
}
