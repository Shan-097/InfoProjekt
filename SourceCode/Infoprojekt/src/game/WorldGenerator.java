package game;

/**
 * to be done
 */
public class WorldGenerator {
    private Field[][] map;

    /**
     * to be done
     */
    public WorldGenerator(){
        map = new Field[Integer.MAX_VALUE][Integer.MAX_VALUE];
    }


    /** 
     * to be done
     * @param posX to be done
     * @param posY to be done
     */
    public void generateTile(int posX, int posY){
        if(posX < 0 || posY < 0) {
            throw new IndexOutOfBoundsException();
        }
        //implement generation
    }
}
