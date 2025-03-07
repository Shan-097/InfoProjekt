package game;

import org.json.JSONObject;

/**
 * The Field class wraps the two classes Building and Resource together.<br>
 * Every Object of type Field represents the state of a field of the map.
 */
public class Field {
    /**
     * The Building on the tile.
     */
    private Building building;

    /**
     * The resource vein of the tile.<br>
     * null represents not generated yet.
     */
    private Resource resource;

    /**
     * The constructor for a field if both the resource and building are known.
     * 
     * @param pBuilding The building on the new field.
     * @param pResource The resource vein of the new field.
     */
    public Field(Building pBuilding, Resource pResource) {
        building = pBuilding;
        resource = pResource;
    }

    /**
     * The constructor for a field when only the resource is known.
     * 
     * @param pResource The resource vein of the new field.
     */
    public Field(Resource pResource) {
        resource = pResource;
    }

    /**
     * The constructor for a field when only the resource id is known.
     * 
     * @param pResourceID The id of the resource of the vein of the new field.
     */
    public Field(int pResourceID) {
        resource = Resource.getResourceWithID(pResourceID);
    }

    /**
     * to be done
     * 
     * @return Building to be done
     */
    public Building getBuilding() {
        return building;
    }

    /**
     * to be done
     * 
     * @return Resource to be done
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * to be done
     * 
     * @return int to be done
     */
    public int getResourceID() {
        return resource.getResourceID();
    }

    /**
     * to be done
     * 
     * @param pBuilding to be done
     */
    public void setBuilding(Building pBuilding) {
        building = pBuilding;
    }

    /**
     * to be done
     * 
     * @return to be done
     */
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("building", building);
        jsonObject.put("resource", resource);
        return jsonObject;
    }
}
