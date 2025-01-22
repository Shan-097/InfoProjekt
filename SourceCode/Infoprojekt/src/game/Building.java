package game;

import java.util.HashMap;

/**
 * to be done
 */
public abstract class Building {
    private byte rotation;

    /**
     * to be done
     */
    public Building(){
        rotation = 0;
    }

    
    /** 
     * to be done
     * @return byte to be done
     */
    public byte getRotation(){
        return rotation;
    }

    /**
     * to be done
     * @return to be done
     */
    public abstract HashMap<Integer, Integer> getCost();

    /**
     * to be done
     * @return to be done
     */
    public abstract byte[] getInputDirections();
    
    /**
     * to be done
     * @return to be done
     */
    public abstract byte[] getOutputDirections();
    
    /** 
     * to be done
     * @param pRotation to be done
     */
    public void setRotation(byte pRotation){
        rotation = pRotation;
    }
}
