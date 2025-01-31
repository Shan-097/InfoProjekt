package game;

import java.util.HashMap;
import java.util.InputMismatchException;

public class ConveyorBelt extends Building {
    private static final HashMap<Integer, Integer> COST;
    private byte[] inputDirections;
    private byte[] outputDirections;

    static {
        COST = new HashMap<Integer, Integer>(4);
        COST.put(1, 1);
        COST.put(2, 1);
        COST.put(3, 1);
        COST.put(4, 1);
    }

    public ConveyorBelt() {
        super();
        inputDirections = new byte[] { 0 };
        outputDirections = new byte[] { 2 };
    }

    @Override
    public HashMap<Integer, Integer> getCost() {
        return COST;
    }

    @Override
    public byte[] getInputDirections() {
        return inputDirections;
    }

    @Override
    public byte[] getOutputDirections() {
        return outputDirections;
    }

    public void setInputDirections(byte[] pInputDirections) {
        if (pInputDirections.length != 1 || pInputDirections[0] < 0 || pInputDirections[0] > 4) {
            throw new InputMismatchException();
        }
        inputDirections = pInputDirections;
        outputDirections = new byte[] { (byte) ((2 + pInputDirections[0]) % 4) };
    }

    public void setOutputDirections(byte[] pOutputDirections) {
        if (pOutputDirections.length != 1 || pOutputDirections[0] < 0 || pOutputDirections[0] > 4
                || pOutputDirections[0] == inputDirections[0]) {
            throw new InputMismatchException();
        }
        outputDirections = pOutputDirections;
    }
}
