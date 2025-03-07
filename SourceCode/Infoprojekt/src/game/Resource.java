package game;

import java.util.HashMap;

import org.json.JSONObject;

/**
 * to be done
 */
public class Resource {
    /**
     * to be done
     */
    private static final HashMap<Integer, Resource> ID_MAP = new HashMap<Integer, Resource>(5);

    /**
     * to be done
     */
    private final int resourceID;

    /**
     * to be done
     */
    static {
        ID_MAP.put(0, new Resource(0)); // no resource
        ID_MAP.put(1, new Resource(1)); // stone
        ID_MAP.put(2, new Resource(2)); // copper
        ID_MAP.put(3, new Resource(3)); // iron
        ID_MAP.put(4, new Resource(4)); // gold
    }

    /**
     * to be done
     * 
     * @param pResourceID to be done
     */
    protected Resource(int pResourceID) {
        resourceID = pResourceID;
    }

    /**
     * to be done
     * 
     * @param pResourceID to be done
     * @return Resource to be done
     */
    public static Resource getResourceWithID(int pResourceID) {
        return ID_MAP.get(pResourceID);
    }

    /**
     * to be done
     * 
     * @return int to be done
     */
    public int getResourceID() {
        return resourceID;
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resourceID", resourceID);
        return jsonObject;
    }
}
