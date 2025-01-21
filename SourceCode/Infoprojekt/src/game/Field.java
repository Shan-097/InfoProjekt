package game;

/**
 * 
 */
public class Field {
    private Building building;
    private Resource resource;

    /**
     * 
     * @param pBuilding
     * @param pResource
     */
    public Field(Building pBuilding, Resource pResource){
        building = pBuilding;
        resource = pResource;
    }

    /**
     * 
     * @param pResource
     */
    public Field(Resource pResource){
        resource = pResource;
    }

    
    /** 
     * @return Building
     */
    public Building getBuilding(){
        return building;
    }
    
    /** 
     * @return Resource
     */
    public Resource getResource(){
        return resource;
    }
    
    /** 
     * @param pBuilding
     */
    public void setBuilding(Building pBuilding){
        building = pBuilding;
    }
}
