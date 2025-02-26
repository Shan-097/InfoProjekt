package game;

import org.json.JSONObject;

/**
 * to be done
 */
public class Field {
    /** 
     * to be done
     */
    private Building building;

    /** 
     * to be done
     */
    private Resource resource;



    /**
     * to be done
     * @param pBuilding to be done
     * @param pResource to be done
     */
    public Field(Building pBuilding, Resource pResource) {
        building = pBuilding;
        resource = pResource;
    }

    /**
     * to be done
     * @param pResource to be done
     */
    public Field(Resource pResource){
        resource = pResource;
    }

    /** 
     * to be done
     * @param pResourceID to be done
     */
    public Field(int pResourceID) {
        resource = Resource.getResourceWithID(pResourceID);
    }


    /** 
     * to be done
     * @return Building to be done
     */
    public Building getBuilding(){
        return building;
    }
    
    /** 
     * to be done
     * @return Resource to be done
     */
    public Resource getResource(){
        return resource;
    }

    /** 
     * to be done
     * @return int to be done
     */
    public int getResourceID() {
        return resource.getResourceID();
    }


    /** 
     * to be done
     * @param pBuilding to be done
     */
    public void setBuilding(Building pBuilding) {
        building = pBuilding;
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("building", building);
        jsonObject.put("resource", resource);
        return jsonObject;
    }
}
