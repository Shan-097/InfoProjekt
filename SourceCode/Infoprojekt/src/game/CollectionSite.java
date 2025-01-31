package game;

import java.util.HashMap;

public class CollectionSite extends Building {
    private static final HashMap<Integer, Integer> COST;
    private static final byte[] INPUT_DIRECTIONS;
    private static final byte[] OUTPUT_DIRECTIONS;

    static {
        COST = null;
        INPUT_DIRECTIONS = new byte[]{(byte) 0, (byte) 1, (byte) 3, (byte) 4};
        OUTPUT_DIRECTIONS = new byte[0];
    }

    public CollectionSite(){
        super();
    }

    @Override
    public boolean addItem(Item item){
        return GameController.addItemToInventory(item);
    }

    @Override
    public HashMap<Integer, Integer> getCost() {
        return COST;
    }
    @Override
    public byte[] getInputDirections() {
        return INPUT_DIRECTIONS;
    }
    @Override
    public byte[] getOutputDirections() {
        return OUTPUT_DIRECTIONS;
    }
}
