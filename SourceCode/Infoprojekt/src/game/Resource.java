package game;

import java.util.HashMap;

public abstract class Resource {
    private static final HashMap<Integer, Class> resourceOfID = new HashMap<Integer, Class>(5);

    public static void main(String[] args) {
        resourceOfID.put(0, NoResource.class);
        resourceOfID.put(1, Stone.class);
        resourceOfID.put(2, Copper.class);
        resourceOfID.put(3, Iron.class);
        resourceOfID.put(4, Gold.class);
    }

    public abstract int getResourceID();

    public static Resource getResourceObjectByID(int pResourceID) {
        try {
            return (Resource) resourceOfID.get(pResourceID).getConstructor(new Class[0]).newInstance();
        } catch (Exception e) {
            // shouldn't happen
            return null;
        }
    }
}
