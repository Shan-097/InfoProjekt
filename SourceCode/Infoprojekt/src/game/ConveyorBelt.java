package game;

import java.util.HashMap;

/**
 * to be done
 */
public class ConveyorBelt extends Building {
    /**
     * to be done
     */
    private static final HashMap<Item, Integer> COST;

    /**
     * to be done
     */
    private final static byte[] INPUT_DIRECTIONS;

    /**
     * to be done
     */
    private byte[] outputDirections;

    /**
     * to be done
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
     * to be done
     */
    public ConveyorBelt() {
        super();
        outputDirections = new byte[] { 2 };
    }

    /**
     * to be done
     * 
     * @param item to be done
     * @return Item to be done
     */
    protected Item executeFunction(Item item) {
        return item;
    }

    /**
     * to be done
     * 
     * @return HashMap<Integer, Integer> to be done
     */
    public HashMap<Item, Integer> getCost() {
        return COST;
    }

    /**
     * to be done
     * 
     * @return byte[] to be done
     */
    public byte[] getInputDirections() {
        return INPUT_DIRECTIONS;
    }

    /**
     * to be done
     * 
     * @return byte[] to be done
     */
    public byte[] getOutputDirections() {
        return outputDirections;
    }

    /**
     * to be done
     */
    @Override
    public void rotate() {
        if (outputDirections[0] == 3) {
            this.setRotation((byte) ((getRotation() + 1) % 4));
            outputDirections[0] = 1;
        } else {
            outputDirections[0]++;
        }
    }
}
