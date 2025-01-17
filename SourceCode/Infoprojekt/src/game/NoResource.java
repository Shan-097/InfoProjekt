package game;

public class NoResource extends Resource{
    private static final int resourceID = 0;

    public NoResource(){
    }

    @Override
    public int getResourceID() {
        return resourceID;
    }
}
