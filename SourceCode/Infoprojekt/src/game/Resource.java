package game;

import java.util.HashMap;

public class Resource {
    private static final HashMap<Integer, String> iDMap = new HashMap<Integer, String>(5);
    private final int resourceID;

    public static void main(String[] args) {
        iDMap.put(0, "NoRessource");
        iDMap.put(1, "Stone");
        iDMap.put(2, "Copper");
        iDMap.put(3, "Iron");
        iDMap.put(4, "Gold");
    }

    public Resource(int pResourceID){
        resourceID = pResourceID;
    }

    public int getResourceID(){
        return resourceID;
    }
}
