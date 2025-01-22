package game;

/**
 * to be done
 */
public class Field {
    private Building building;
    private Resource resource;

    /**
     * to be done
     * @param pBuilding to be done
     * @param pResource to be done
     */
    public Field(Building pBuilding, Resource pResource){
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
     * @param pBuilding to be done
     */
    public void setBuilding(Building pBuilding){
        building = pBuilding;
    }
}
