package game;


import java.util.HashMap;



/**
 * to be done
 */
public class CollectionSite extends Building {
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
        COST = null;
        INPUT_DIRECTIONS = new byte[]{(byte) 0, (byte) 1, (byte) 3, (byte) 4};
        OUTPUT_DIRECTIONS = new byte[0];
    }



    /**
     * to be done
     */
    public CollectionSite(){
        super();
    }


    /** 
     * to be done
     * @return boolean to be done
     */
    @Override
    public boolean addItem(Item item){
        return GameController.addItemToInventory(item);
    }

    /**
     * to be done
     * @param item to be done
     * @return Item to be done
     */
    @Override
    protected Item executeFunction(Item item){
        return item;
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
}
