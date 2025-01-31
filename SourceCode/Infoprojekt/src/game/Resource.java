package game;

import java.util.HashMap;

public class Resource {
    private static final HashMap<Integer, Resource> iDMap = new HashMap<Integer, Resource>(5);
    private final int resourceID;

    static {
        iDMap.put(0, new Resource(0)); //no resource
        iDMap.put(1, new Resource(1)); //stone
        iDMap.put(2, new Resource(1)); //copper
        iDMap.put(3, new Resource(1)); //iron
        iDMap.put(4, new Resource(1)); //gold
    }

    private Resource(int pResourceID){
        resourceID = pResourceID;
    }

    public static Resource getResourceWithID(int pResourceID){
        return iDMap.get(pResourceID);
    }

    public int getResourceID(){
        return resourceID;
    }
}
