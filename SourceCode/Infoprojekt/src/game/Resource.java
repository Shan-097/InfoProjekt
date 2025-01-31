package game;


import java.util.HashMap;



/**
 * to be done
 */
public class Resource {
    /** 
     * to be done
     */
    private static final HashMap<Integer, Resource> iDMap = new HashMap<Integer, Resource>(5);

    /** 
     * to be done
     */
    private final int resourceID;


    /** 
     * to be done
     */
    static {
        iDMap.put(0, new Resource(0)); //no resource
        iDMap.put(1, new Resource(1)); //stone
        iDMap.put(2, new Resource(1)); //copper
        iDMap.put(3, new Resource(1)); //iron
        iDMap.put(4, new Resource(1)); //gold
    }



    /**
     * to be done
     * @param pResourceID to be done
     */
    private Resource(int pResourceID){
        resourceID = pResourceID;
    }


    /**
     * to be done
     * @param pResourceID to be done
     * @return Resource to be done
     */
    public static Resource getResourceWithID(int pResourceID){
        return iDMap.get(pResourceID);
    }


    /** 
     * to be done
     * @return int to be done
     */
    public int getResourceID(){
        return resourceID;
    }
}
