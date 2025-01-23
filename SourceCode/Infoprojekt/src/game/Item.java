package game;

import java.util.HashMap;

public class Item {
    private static final HashMap<Integer, Item> iDMap = new HashMap<Integer, Item>(5);
    private final int itemID;

    public static void main(String[] args) {
        iDMap.put(0, new Item(0)); //brick
        iDMap.put(1, new Item(1)); //copper ingot
        iDMap.put(2, new Item(2)); //iron ingot
        iDMap.put(3, new Item(3)); //gold ingot
        iDMap.put(4, new Item(4)); //stone
        iDMap.put(5, new Item(5)); //copper ore
        iDMap.put(6, new Item(6)); //iron ore
        iDMap.put(7, new Item(7)); //gold ore
    }

    private Item(int pItemID){
        itemID = pItemID;
    }

    public static Item getItemWithID(int pItemID){
        return iDMap.get(pItemID);
    }

    public int getItemID(){
        return itemID;
    }
}
