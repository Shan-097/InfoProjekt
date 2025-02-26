package game;


import java.util.HashMap;
import java.util.LinkedList;

//TODO: Create a delay from moving into a building until it can be removed
//=> Keep the LinkedList always at length 5 and fill it up with null 

/**
 * to be done
 */
public abstract class Building {
    /**
     * to be done
     */
    private byte rotation;

    /**
    * to be done
    */
    private LinkedList<Item> content;



    /**
     * to be done
     */
    public Building() {
        rotation = 0;
        content = new LinkedList<Item>();
    }


    /**
     * to be done
     * @param item to be done
     */
    public boolean addItem(Item item){
        if (content.size() > 4) {
            return false;
        }
        return content.add(item);
    }

    /**
     * to be done
     * @param otherBuilding to be done
     */
    public void moveItemToNextBuilding(Building otherBuilding){
        if (content.size() != 0){
            if(otherBuilding.addItem(content.getFirst())){
                content.removeFirst();
            }
        }
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
    public void setRotation(byte pRotation) {
        rotation = pRotation;
    }
}
