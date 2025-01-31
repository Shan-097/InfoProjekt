package game;


import java.util.HashMap;



/**
 * to be done
 */
public class Item {
    /**
    * to be done
    */
    private static final HashMap<Integer, Item> iDMap = new HashMap<Integer, Item>(5);

    /**
    * to be done
    */
    private final int itemID;


    /** 
     * to be done
     */
    static {
        iDMap.put(0, new Item(0)); //brick
        iDMap.put(1, new Item(1)); //copper ingot
        iDMap.put(2, new Item(2)); //iron ingot
        iDMap.put(3, new Item(3)); //gold ingot
        iDMap.put(4, new Item(4)); //stone
        iDMap.put(5, new Item(5)); //copper ore
        iDMap.put(6, new Item(6)); //iron ore
        iDMap.put(7, new Item(7)); //gold ore
    }



    /**
     * to be done
     * @param pItemID to be done
     */
    public Item(int pItemID){
        itemID = pItemID;
    }


    /** 
     * to be done
     * @param pItemID to be done
     * @return Item to be done
     */
    public static Item getItemWithID(int pItemID){
        return iDMap.get(pItemID);
    }


    /** 
     * to be done
     * @return int to be done
     */
    public int getItemID(){
        return itemID;
    }
}
