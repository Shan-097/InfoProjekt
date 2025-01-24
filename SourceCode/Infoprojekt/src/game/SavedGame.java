package game;

public class SavedGame {
    private String worldName;
    private int posX;
    private int posY;

    public SavedGame(String worldName, int posX, int posY) {
        this.worldName = worldName;
        this.posX = posX;
        this.posY = posY;
    }
}