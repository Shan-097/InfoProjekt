package game;

public class WorldGenerator {
    private Field[][] map;
    
    public WorldGenerator(){
        map = new Field[Integer.MAX_VALUE][Integer.MAX_VALUE];
    }

    public void generateTile(int posX, int posY){
        if(posX < 0 || posY < 0) {
            throw new IndexOutOfBoundsException();
        }
        //implement generation
    }
}
