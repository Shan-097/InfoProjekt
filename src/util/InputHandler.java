package util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.HashSet;
import java.util.LinkedList;

/**
 * The class of the Input handler implementing Keylistener.<br>
 * Every Object has a collection of all keys with whether or not they are
 * pressed.
 */
public class InputHandler implements KeyListener {
    /**
     * The collection of pressed keys.
     */
    private HashSet<Character> keysDown;

    /**
     * The collection of keys that are to be ignored until they are releases the
     * next time.
     */
    private HashSet<Character> keysToIgnoreUntilReleased;

    /**
     * Standard constructor of the InnputHandler.
     * Only instantiates the collections.
     */
    public InputHandler() {
        keysDown = new HashSet<Character>(55);
        keysToIgnoreUntilReleased = new HashSet<Character>(2);
    }

    /**
     * Adds the char of the KeyEvent to the collection of pressed keys.
     * 
     * @param e The registered KeyEvent
     */
    @Override
    public void keyTyped(KeyEvent e) {
        char key = e.getKeyChar();
        // '\u001B' esc, '\u0020' space, '\u007F' delete
        if ((!Character.isLetterOrDigit(key)) && "+-\u001B\u0020\u007F".indexOf(key) == -1) {
            return;
        }
        if (keysToIgnoreUntilReleased.contains(key)) {
            return;
        }
        if (!keysDown.contains(key)) {
            keysDown.add(key);
        }
    }

    /**
     * Is not used but has to be implemented.
     * 
     * @param e The registered KeyEvent
     */
    @Override
    public void keyPressed(KeyEvent e) {
    }

    /**
     * Sets the boolean wheter the key is typed or not to false or adds the entry to
     * the collection.
     * 
     * @param e The registered KeyEvent
     */
    @Override
    public void keyReleased(KeyEvent e) {
        char key = e.getKeyChar();
        keysDown.remove(key);
        keysToIgnoreUntilReleased.remove(key);
    }

    /**
     * Returns wheter a character is pressed or not.
     * 
     * @param c The character to be checked
     * @return boolean True if pressed otherwise false
     */
    public boolean keyIsPressed(char c) {
        return keysDown.contains(c);
    }

    /**
     * to be done
     * 
     * @param c to be done
     * @return to be done
     */
    public boolean keyIsHold(Character c) {
        if (keysDown.contains(c)) {
            keysDown.remove(c);
            return true;
        }
        return false;
    }

    /**
     * to be done
     * 
     * @param c to be done
     * @return to be done
     */
    public boolean keyIsClicked(Character c) {
        if (keysDown.contains(c)) {
            keysToIgnoreUntilReleased.add(c);
            keysDown.remove(c);
            return true;
        }
        return false;
    }

    /**
     * Returns a collection of pressed characters.
     * 
     * @return The collection of pressed characters
     */
    public LinkedList<Character> getPressedCharacters() {
        LinkedList<Character> pressed = new LinkedList<Character>();
        pressed.addAll(keysDown);
        return pressed;
    }
}
