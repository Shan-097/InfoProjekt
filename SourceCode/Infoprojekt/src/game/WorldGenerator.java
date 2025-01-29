package game;

public class WorldGenerator {
    private Field[][] map;

    public WorldGenerator() {
        map = new Field[1000][1000];
    }

    public void generateTile(int posX, int posY) {
        if (posX < 0 || posY < 0) {
            throw new IndexOutOfBoundsException();
        }
        // implement generation
    }

    public Field[][] getMap() {
        return map;
    }

}
