package game;

import java.util.HashMap;

/**
 * 
 */
public class Smelter extends Building {
    private static HashMap<Integer, Integer> cost;
    private static byte[] inputDirections;
    private static byte[] outputDirections;

    
    /** 
     * @param args
     */
    public static void main(String[] args) {
        cost = new HashMap<Integer, Integer>(4);
        cost.put(1, 1);
        cost.put(2, 1);
        cost.put(3, 1);
        cost.put(4, 1);
        inputDirections = new byte[]{0};
        outputDirections = new byte[]{2};
    }

    /**
     * 
     */
    public Smelter(){
        super();
    }

    
    /** 
     * @return HashMap<Integer, Integer>
     */
    @Override
    public HashMap<Integer, Integer> getCost() {
        return cost;
    }
    
    /** 
     * @return byte[]
     */
    @Override
    public byte[] getInputDirections() {
        return inputDirections;
    }
    
    /** 
     * @return byte[]
     */
    @Override
    public byte[] getOutputDirections() {
        return outputDirections;
    }
}
