package game;

import java.util.HashMap;

/**
 * to be done
 */
public class Extractor extends Building {
    private static HashMap<Integer, Integer> cost;
    private static byte[] inputDirections;
    private static byte[] outputDirections;

    
    /** 
     * to be done
     * @param args to be done
     */
    public static void main(String[] args) {
        cost = new HashMap<Integer, Integer>(4);
        cost.put(1, 1);
        cost.put(2, 1);
        cost.put(3, 1);
        cost.put(4, 1);
        inputDirections = null;
        outputDirections = new byte[]{2};
    }

    /**
     * to be done
     */
    public Extractor(){
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
