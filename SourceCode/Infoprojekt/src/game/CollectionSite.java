package game;

import java.util.HashMap;

/**
 * 
 */
public class CollectionSite extends Building {
    private static HashMap<Integer, Integer> cost;
    private static byte[] inputDirections;
    private static byte[] outputDirections;

    
    /** 
     * @param args
     */
    public static void main(String[] args) {
        cost = null;
        inputDirections = new byte[]{(byte) 0, (byte) 1, (byte) 3, (byte) 4};
        outputDirections = null;
    }

    /**
     * 
     */
    public CollectionSite(){
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
