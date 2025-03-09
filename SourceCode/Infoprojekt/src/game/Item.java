package game;

import java.util.HashMap;

/**
 * The Item class manages the items in the game.<br>
 * For every unique item one object is created. All references to items with the
 * same id have the same pointer and therefor refer to the same object managed
 * by this class.
 */
public class Item {
    /**
     * The Collection of items. Each object can be accessed via its id.
     */
    private static final HashMap<Integer, Item> ID_MAP = new HashMap<Integer, Item>(5);

    /**
     * The id of this Item object.
     */
    private final int itemID;

    /**
     * The static and final attributes are declared here.
     */
    static {
        ID_MAP.put(0, new Item(0)); // brick
        ID_MAP.put(1, new Item(1)); // copper ingot
        ID_MAP.put(2, new Item(2)); // iron ingot
        ID_MAP.put(3, new Item(3)); // gold ingot
        ID_MAP.put(4, new Item(4)); // stone
        ID_MAP.put(5, new Item(5)); // copper ore
        ID_MAP.put(6, new Item(6)); // iron ore
        ID_MAP.put(7, new Item(7)); // gold ore
    }

    /**
     * The private contructor for item objects.
     * 
     * @param pItemID The id of the new object
     */
    private Item(int pItemID) {
        itemID = pItemID;
    }

    /**
     * Returns the smelted variant of the specified object.<br>
     * If it is already a smelted item it is returned instead.
     * 
     * @param item The item to be smelted
     * @return The smelted item
     */
    public static Item getSmeltedItem(Item item) {
        switch (item.getItemID()) {
            case 4:
                return ID_MAP.get(0);

            case 5:
                return ID_MAP.get(1);

            case 6:
                return ID_MAP.get(2);

            case 7:
                return ID_MAP.get(3);

            default:
                return item;
        }
    }

    /**
     * Returns the item object with the specified id.
     * 
     * @param pItemID The id of the requested item
     * @return The item with the specified id
     */
    public static Item getItemWithID(int pItemID) {
        return ID_MAP.get(pItemID);
    }

    /**
     * Returns the item of a certain resource.
     * 
     * @param resource The resource the item is requested from
     * @return The item of the specified resource
     */
    public static Item getItemFromResource(Resource resource) {
        switch (resource.getResourceID()) {
            case 1:
                return ID_MAP.get(4);

            case 2:
                return ID_MAP.get(5);

            case 3:
                return ID_MAP.get(6);

            case 4:
                return ID_MAP.get(7);

            default:
                return null;
        }
    }

    /**
     * Returns the id of this Item object.
     * 
     * @return The id
     */
    public int getItemID() {
        return itemID;
    }
}
