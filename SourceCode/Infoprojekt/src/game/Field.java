package game;

public class Field {
    private Building building;
    private Resource resource;

    public Field(Building pBuilding, Resource pResource) {
        building = pBuilding;
        resource = pResource;
    }

    public Field(Resource pResource) {
        resource = pResource;
    }

    public Field(int pResourceID) {
        resource = Resource.getResourceObjectByID(pResourceID);
    }

    public Building getBuilding() {
        return building;
    }

    public Resource getResource() {
        return resource;
    }

    public int getResourceID() {
        return resource.getResourceID();
    }

    public void setBuilding(Building pBuilding) {
        building = pBuilding;
    }
}
