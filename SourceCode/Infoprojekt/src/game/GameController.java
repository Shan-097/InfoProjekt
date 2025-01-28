package game;

public class GameController {
    private int posXinArray;
    private int posYinArray;
    private byte posXonTile;//max is 100, min is 0
    private byte posYonTile;//max is 100, min is 0


    public GameController(){
        posXinArray = 50;
        posYinArray = 50;
        posXonTile = 50;
        posYinArray = 50;
    }

    public void update(){

    }

    public int getPosX() {
        return posXinArray;
    }
    public int getPosY() {
        return posYinArray;
    }
    public char getDirection() {
        return 'A';
    }
    public float getOffsetX() {
        return 0;
    }
    public float getOffsetY() {
        return 0;
    }


    // pls: variable to tell me what direction player is moving in
}
