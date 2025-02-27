package game;

import java.util.HashMap;

/**
 * to be done
 */
public class Extractor extends Building {
    /**
     * to be done
     */
    private static final HashMap<Item, Integer> COST;

    /**
     * to be done
     */
    private static final byte[] INPUT_DIRECTIONS;

    /**
     * to be done
     */
    private static final byte[] OUTPUT_DIRECTIONS;

    /**
     * to be done
     */
    private Item itemToBeExtracted;

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
        INPUT_DIRECTIONS = new byte[0];
        OUTPUT_DIRECTIONS = new byte[] { 2 };
    }

    /**
     * to be done
     */
    public Extractor() {
        super();
    }

    /**
     * to be done
     */
    protected Item executeFunction(Item item) {
        return itemToBeExtracted;
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
        return OUTPUT_DIRECTIONS;
    }

    /**
     * to be done
     * 
     * @param pResource to be done
     */
    public void setResourceToBeExtracted(Resource pResource) {
        itemToBeExtracted = Item.getItemFromResource(pResource);
    }
}
