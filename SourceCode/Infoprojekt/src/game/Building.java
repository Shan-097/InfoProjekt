package game;

import java.util.HashMap;

/**
 * 
 */
public abstract class Building {
    private byte rotation;

    /**
     * 
     */
    public Building(){
        rotation = 0;
    }

    
    /** 
     * @return byte
     */
    public byte getRotation(){
        return rotation;
    }

    /**
     * 
     * @return
     */
    public abstract HashMap<Integer, Integer> getCost();

    /**
     * 
     * @return
     */
    public abstract byte[] getInputDirections();
    
    /**
     * 
     * @return
     */
    public abstract byte[] getOutputDirections();
    
    /** 
     * @param pRotation
     */
    public void setRotation(byte pRotation){
        rotation = pRotation;
    }
}
