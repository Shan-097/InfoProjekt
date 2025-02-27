package game;

import java.util.HashMap;

/**
 * to be done
 */
public class Smelter extends Building {
    /**
     * to be done
     */
    private static final HashMap<Integer, Integer> COST;

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
    static {
        COST = new HashMap<Integer, Integer>(4);
        COST.put(1, 1);
        COST.put(2, 1);
        COST.put(3, 1);
        COST.put(4, 1);
        INPUT_DIRECTIONS = new byte[] { 0 };
        OUTPUT_DIRECTIONS = new byte[] { 2 };
    }

    /**
     * to be done
     */
    public Smelter() {
        super();
    }

    @Override
    public Item executeFunction(Item item) {
        if (item == null) {
            return null;
        }
        return Item.getSmeltedItem(item);
    }

    /**
     * to be done
     * 
     * @return HashMap<Integer, Integer> to be done
     */
    @Override
    public HashMap<Integer, Integer> getCost() {
        return COST;
    }

    /**
     * to be done
     * 
     * @return byte[] to be done
     */
    @Override
    public byte[] getInputDirections() {
        return INPUT_DIRECTIONS;
    }

    /**
     * to be done
     * 
     * @return byte[] to be done
     */
    @Override
    public byte[] getOutputDirections() {
        return OUTPUT_DIRECTIONS;
    }

    /**
     * to be done
     */
    @Override
    public void rotate(){
        this.setRotation((byte)((getRotation() + 1) % 4));
    }
}
