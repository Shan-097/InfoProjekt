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
    private final Resource resource;

    /**
     * The constructor for a field if both the resource and building are known.
     * 
     * @param pBuilding The building on the new field.
     * @param pResource The resource vein of the new field.
     */
    protected Field(Building pBuilding, Resource pResource) {
        building = pBuilding;
        resource = pResource;
    }

    /**
     * The constructor for a field when only the resource id is known.
     * 
     * @param pResourceID The id of the resource of the vein of the new field.
     * @throws IllegalArgumentException throws an IllegalArgumentException
     *                                  if {@link Resource#getResourceWithID(int)} throws one.
     */
    protected Field(int pResourceID) throws IllegalArgumentException {
        try {
            resource = Resource.getResourceWithID(pResourceID);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Returns the building on this field.
     * 
     * @return The building on this field
     */
    public Building getBuilding() {
        return building;
    }

    /**
     * Returns the resource object of the vein of this field.
     * 
     * @return The resource on this field
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * Returns the id of the resource of the vein of this field.
     * 
     * @return The id of the resource
     */
    public int getResourceID() {
        return resource.getResourceID();
    }

    /**
     * Sets the building of the field.
     * 
     * @param pBuilding The new building
     */
    protected void setBuilding(Building pBuilding) {
        building = pBuilding;
    }

    /**
     * to be done
     * 
     * @return to be done
     */
    protected JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();

        if (building != null) {
            jsonObject.put("building", building.toJSONObject());
        }
        if (resource != null) {
            jsonObject.put("resource", resource.toJSONObject());
        }

        return jsonObject;
    }
}
