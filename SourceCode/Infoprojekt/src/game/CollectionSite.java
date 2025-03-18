package game;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The collection site is a building that collects the items moved to it and
 * moves them to the players inventory.
 */
public class CollectionSite extends Building {
    /**
     * The input directions of every collection site without regards to its
     * rotation.
     */
    private static final byte[] INPUT_DIRECTIONS;

    /**
     * The output directions of every collection site without regards to its
     * rotation.
     */
    private static final byte[] OUTPUT_DIRECTIONS;

    /**
     * The static and final attributes are declared here.
     */
    static {
        INPUT_DIRECTIONS = new byte[] { (byte) 0, (byte) 1, (byte) 2, (byte) 3 };
        OUTPUT_DIRECTIONS = new byte[0];
    }

    /**
     * The empty constructor of CollectionSite is only calling the super
     * constructor.
     */
    public CollectionSite() {
        super();
    }

    /**
     * Constructor for CollectionSite that initializes the object with saved values.
     * 
     * @param rotation The rotation of the collection site.
     * @param content  The inventory of the collection site.
     */
    public CollectionSite(int rotation, LinkedList<Item> content) {
        super(rotation, content);
    }

    /**
     * Overrides the addItem(Item item) method from Building.<br>
     * Items moved to a collection site are added to the players inventory.
     * 
     * @param item The item being moved to that building.
     * @return Returns true if and only if the item was successfully added to the
     *         players inventory.
     */
    @Override
    public boolean addItem(Item item) {
        try {
            return GameController.addItemToInventory(item);
        } catch (IllegalArgumentException e) {
            return false;
        }
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
     * Returns the cost of this building which is nothing.
     * 
     * @return The "cost" of this building.
     */
    public HashMap<Item, Integer> getCost() {
        return new HashMap<Item, Integer>(0);
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
     * to be done
     * 
     * @return to be done
     */
    @Override
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rotation", rotation);
        jsonObject.put("content", new JSONArray(content));
        jsonObject.put("type", "collectionSite");

        return jsonObject;
    }
}
