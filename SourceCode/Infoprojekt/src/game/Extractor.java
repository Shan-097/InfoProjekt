package game;


import java.util.HashMap;



/**
 * to be done
 */
public class Extractor extends Building {
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
    private Item itemToBeExtracted;

    /** 
     * to be done
     */
    static {
        COST = new HashMap<Integer, Integer>(4);
        COST.put(1, 1);
        COST.put(2, 1);
        COST.put(3, 1);
        COST.put(4, 1);
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
    @Override
    protected Item executeFunction(Item item){
        return itemToBeExtracted;
    }
    
    /** 
     * to be done
     * @return HashMap<Integer, Integer> to be done
     */
    @Override
    public HashMap<Integer, Integer> getCost() {
        return COST;
    }

    /** 
     * to be done
     * @return byte[] to be done
     */
    @Override
    public byte[] getInputDirections() {
        return INPUT_DIRECTIONS;
    }

    /** 
     * to be done
     * @return byte[] to be done
     */
    @Override
    public byte[] getOutputDirections() {
        return OUTPUT_DIRECTIONS;
    }

    public void setResourceToBeExtracted(Resource pResource){
        itemToBeExtracted = Item.getItemFromResource(pResource);
    }
}
