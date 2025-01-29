package game;

import java.util.HashMap;

public class Smelter extends Building {
    private static final HashMap<Integer, Integer> COST;
    private static final byte[] INPUT_DIRECTIONS;
    private static final byte[] OUTPUT_DIRECTIONS;

    static {
        COST = new HashMap<Integer, Integer>(4);
        COST.put(1, 1);
        COST.put(2, 1);
        COST.put(3, 1);
        COST.put(4, 1);
        INPUT_DIRECTIONS = new byte[]{0};
        OUTPUT_DIRECTIONS = new byte[]{2};
    }

    public Smelter(){
        super();
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
