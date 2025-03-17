package util;

/**
 * A tuple with two elements.<br>
 * Used for storing coordinates of a 2D space and comparing them.
 */
public class Tuple {
    private int a;
    private int b;

    public Tuple(int pA, int pB) {
        a = pA;
        b = pB;
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    @Override
    public boolean equals(Object obj) {
        Tuple other = (Tuple) obj;
        if (a == other.getA() && b == other.getB()) {
            return true;
        }
        return false;
    }
}