package game;


import java.util.HashMap;



/**
 * to be done
 */
public class Item {
    /**
    * to be done
    */
    private static final HashMap<Integer, Item> ID_MAP = new HashMap<Integer, Item>(5);

    /**
    * to be done
    */
    private final int itemID;


    /** 
     * to be done
     */
    static {
        ID_MAP.put(0, new Item(0)); //brick
        ID_MAP.put(1, new Item(1)); //copper ingot
        ID_MAP.put(2, new Item(2)); //iron ingot
        ID_MAP.put(3, new Item(3)); //gold ingot
        ID_MAP.put(4, new Item(4)); //stone
        ID_MAP.put(5, new Item(5)); //copper ore
        ID_MAP.put(6, new Item(6)); //iron ore
        ID_MAP.put(7, new Item(7)); //gold ore
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
        return ID_MAP.get(pItemID);
    }


    /** 
     * to be done
     * @return int to be done
     */
    public int getItemID(){
        return itemID;
    }
}
