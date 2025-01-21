package game;

import java.util.HashMap;
import java.util.InputMismatchException;

/**
 * 
 */
public class ConveyorBelt extends Building {
    private static HashMap<Integer, Integer> cost;
    private byte[] inputDirections;
    private byte[] outputDirections;

    
    /** 
     * @param args
     */
    public static void main(String[] args) {
        cost = new HashMap<Integer, Integer>(4);
        cost.put(1, 1);
        cost.put(2, 1);
        cost.put(3, 1);
        cost.put(4, 1);
    }

    /**
     * 
     */
    public ConveyorBelt(){
        inputDirections = new byte[]{0};
        outputDirections = new byte[]{2};
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
    
    /** 
     * @param pInputDirections
     */
    public void setInputDirections(byte[] pInputDirections){
        if(pInputDirections.length != 1 || pInputDirections[0] < 0 || pInputDirections[0] > 4) {
            throw new InputMismatchException();
        }
        inputDirections = pInputDirections;
        outputDirections = new byte[]{(byte)((2 + pInputDirections[0]) % 4)};
    }
    
    /** 
     * @param pOutputDirections
     */
    public void setOutputDirections(byte[] pOutputDirections){
        if(pOutputDirections.length != 1 || pOutputDirections[0] < 0 || pOutputDirections[0] > 4 || pOutputDirections[0] == inputDirections[0]) {
            throw new InputMismatchException();
        }
        outputDirections = pOutputDirections;
    }
}
