package game;

import java.util.HashMap;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

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
        for (int i = 0; i < 8; i++) {
            try {
                COST.put(Item.getItemWithID(i), 0);
            } catch (Exception e) {
            }
        }
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
     * Constructor for ConveyorBelt that initializes the object with saved values.
     * 
     * @param rotation         The rotation of the conveyor belt.
     * @param content          The inventory of the conveyor belt.
     * @param outputDirections The output directions of the conveyor belt.
     */
    public ConveyorBelt(int rotation, LinkedList<Item> content, byte[] outputDirections) {
        super(rotation, content); // Call the parent constructor with rotation and content
        this.outputDirections = outputDirections; // Set the output directions
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
        return outputDirections;
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
     * to be done
     * 
     * @return to be done
     */
    @Override
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rotation", rotation);
        jsonObject.put("content", new JSONArray(content));
        jsonObject.put("type", "conveyorBelt");

        jsonObject.put("inputDirections", new JSONArray(INPUT_DIRECTIONS));
        jsonObject.put("outputDirections", new JSONArray(outputDirections));

        JSONObject costObject = new JSONObject();
        for (Item item : COST.keySet()) {
            costObject.put(String.valueOf(item.getItemID()), COST.get(item));
        }
        jsonObject.put("cost", costObject);

        return jsonObject;
    }
}
