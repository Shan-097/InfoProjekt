package game;

public class Field {
    private Building building;
    private Resource resource;

    public Field(Building pBuilding, Resource pResource){
        building = pBuilding;
        resource = pResource;
    }

    public Field(Resource pResource){
        resource = pResource;
    }

    public Building getBuilding(){
        return building;
    }
    public Resource getResource(){
        return resource;
    }
    public void setBuilding(Building pBuilding){
        building = pBuilding;
    }
}
