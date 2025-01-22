package game;

import java.util.HashMap;

/**
 * to be done
 */
public class CollectionSite extends Building {
    private static HashMap<Integer, Integer> cost;
    private static byte[] inputDirections;
    private static byte[] outputDirections;

    
    /** 
     * to be done
     * @param args to be done
     */
    public static void main(String[] args) {
        cost = null;
        inputDirections = new byte[]{(byte) 0, (byte) 1, (byte) 3, (byte) 4};
        outputDirections = null;
    }

    /**
     * to be done
     */
    public CollectionSite(){
        super();
    }

    
    /** 
     * to be done
     * @return HashMap<Integer, Integer> to be done
     */
    @Override
    public HashMap<Integer, Integer> getCost() {
        return cost;
    }
    
    /** 
     * to be done
     * @return byte[] to be done
     */
    @Override
    public byte[] getInputDirections() {
        return inputDirections;
    }
    
    /** 
     * to be done
     * @return byte[] to be done
     */
    @Override
    public byte[] getOutputDirections() {
        return outputDirections;
    }
}
