package game;

import java.util.HashMap;

import org.json.JSONObject;

/**
 * The Resource class manages the resources in the game.<br>
 * For every unique resource one object is created. All references to resources
 * with the same id have the same pointer and therefor refer to the same object
 * managed by this class.
 */
public class Resource {
    /**
     * The Collection of resources. Each object can be accessed via its id.
     */
    private static final HashMap<Integer, Resource> ID_MAP = new HashMap<Integer, Resource>(5);

    /**
     * The id of this Resource object.
     */
    private final int resourceID;

    /**
     * The static and final attributes are declared here.
     */
    static {
        ID_MAP.put(0, new Resource(0)); // no resource
        ID_MAP.put(1, new Resource(1)); // stone
        ID_MAP.put(2, new Resource(2)); // copper
        ID_MAP.put(3, new Resource(3)); // iron
        ID_MAP.put(4, new Resource(4)); // gold
    }

    /**
     * The private contructor for Resource objects.
     * 
     * @param pResourceID The id of the new object
     */
    private Resource(int pResourceID) {
        resourceID = pResourceID;
    }

    /**
     * Returns the resource object with the specified id.
     * 
     * @param pResourceID The id of the requested resource
     * @return The resource with the specified id
     * @throws IllegalArgumentException Throws an IllegalArgumentException if the
     *                                  given resource id isn't recognized.
     */
    protected static Resource getResourceWithID(int pResourceID) throws IllegalArgumentException {
        if (!ID_MAP.containsKey(pResourceID)) {
            throw new IllegalArgumentException("The id is not associated with a resource.");
        }
        return ID_MAP.get(pResourceID);
    }

    /**
     * Returns the id of this resource object.
     * 
     * @return The id
     */
    public int getResourceID() {
        return resourceID;
    }

    /**
     * Builds a JSONObject containing this objects resourceID to be saved as part of the worldMap in a JSON file.
     * 
     * @return The JSONObject containing the resourceID.
     */
    protected JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resourceID", resourceID);
        return jsonObject;
    }
}
