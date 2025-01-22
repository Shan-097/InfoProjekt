package game;

import java.util.HashMap;

/**
 * to be done
 */
public class Resource {
    private static final HashMap<Integer, String> iDMap = new HashMap<Integer, String>(5);
    private final int resourceID;

    
    /** 
     * to be done
     * @param args to be done
     */
    public static void main(String[] args) {
        iDMap.put(0, "NoRessource");
        iDMap.put(1, "Stone");
        iDMap.put(2, "Copper");
        iDMap.put(3, "Iron");
        iDMap.put(4, "Gold");
    }

    /**
     * to be done
     * @param pResourceID to be done
     */
    public Resource(int pResourceID){
        resourceID = pResourceID;
    }

    
    /** 
     * to be done
     * @return int to be done
     */
    public int getResourceID(){
        return resourceID;
    }
}
