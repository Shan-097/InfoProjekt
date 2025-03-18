package game;

import java.util.HashMap;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

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
        for (int i = 0; i < 8; i++) {
            try {
                COST.put(Item.getItemWithID(i), 0);
            } catch (Exception e) {
            }
        }
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
     * Constructor for Smelter that initializes the object with saved values.
     * 
     * @param rotation The rotation of the smelter.
     * @param content  The inventory of the smelter.
     */
    public Smelter(int rotation, LinkedList<Item> content) {
        super(rotation, content);
    }

    /**
     * As this building smelts items it returns the smelted variant of the given
     * one.<br>
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
     * to be done
     * 
     * @return to be done
     */
    @Override
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rotation", rotation);
        jsonObject.put("content", new JSONArray(content));
        jsonObject.put("type", "smelter");

        JSONObject costObject = new JSONObject();
        for (Item item : COST.keySet()) {
            costObject.put(String.valueOf(item.getItemID()), COST.get(item));
        }
        jsonObject.put("cost", costObject);

        return jsonObject;
    }
}
