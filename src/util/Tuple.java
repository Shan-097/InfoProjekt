package util;

/**
 * A tuple with two elements.<br>
 * Used for storing coordinates of a 2D space and comparing them.
 */
public class Tuple {
    /**
     * The first value in the tuple
     */
    private int a;
    /**
     * The second value in the tuple
     */
    private int b;

    /**
     * The constructor for a tuple. Sets both values.
     * 
     * @param pA The first value
     * @param pB The second value
     */
    public Tuple(int pA, int pB) {
        a = pA;
        b = pB;
    }

    /**
     * Returns the first value of the tuple.
     * 
     * @return The first value
     */
    public int getA() {
        return a;
    }

    /**
     * Returns the second value of the tuple.
     * 
     * @return The second value
     */
    public int getB() {
        return b;
    }

    /**
     * Returns wheter or not two tuple are the same.<br>
     * Overrides the equals method of the class Object.
     * 
     * @param obj The other object (tuple) this one is to be compared to.
     */
    @Override
    public boolean equals(Object obj) {
        Tuple other = (Tuple) obj;
        if (a == other.getA() && b == other.getB()) {
            return true;
        }
        return false;
    }
}