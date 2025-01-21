package game;

import java.util.HashMap;

/**
 * 
 */
public class Item {
    private static final HashMap<Integer, String> iDMap = new HashMap<Integer, String>(5);
    private final int itemID;

    
    /** 
     * @param args
     */
    public static void main(String[] args) {
        iDMap.put(0, "Brick");
        iDMap.put(1, "CopperIngot");
        iDMap.put(2, "IronIngot");
        iDMap.put(3, "GoldIngot");
        iDMap.put(4, "Stone");
        iDMap.put(5, "CopperOre");
        iDMap.put(6, "IronOre");
        iDMap.put(7, "GoldOre");
    }

    /**
     * 
     * @param pItemID
     */
    public Item(int pItemID){
        itemID = pItemID;
    }

    
    /** 
     * @return int
     */
    public int getItemID(){
        return itemID;
    }
}
