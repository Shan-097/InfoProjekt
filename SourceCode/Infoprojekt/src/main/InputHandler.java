package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;

/**
 * The class of the Input handler implementing Keylistener.
 * Every Object has a collection of all keys with wether or not they are
 * pressed.
 */
public class InputHandler implements KeyListener {
    /**
     * to be done
     */
    private HashMap<Character, Boolean> keysPressed;

    private HashSet<Character> keysToIgnoreUntilReleased;

    /**
     * Standard constructor of the InnputHandler.
     * Only instantiates the collection.
     */
    public InputHandler() {
        keysPressed = new HashMap<Character, Boolean>(55);
        keysToIgnoreUntilReleased = new HashSet<Character>(1);
    }

    /**
     * Is not used but has to be implemented
     * 
     * @param e The KeyEvent
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Sets the boolean wheter the key is typed or not to true or adds the entry to
     * the collection.
     * 
     * @param e The registered KeyEvent
     */
    @Override
    public void keyPressed(KeyEvent e) {
        char key = e.getKeyChar();
        //'\u001B' esc, '\u0020' space, '\u007F' delete
        if ((!Character.isLetterOrDigit(key)) && "\u001B\u0020\u007F".indexOf(key) == -1) {
            return;
        }
        if (keysToIgnoreUntilReleased.contains(key)) {
            return;
        }
        if (keysPressed.containsKey(key)) {
            keysPressed.replace(key, true);
        } else {
            keysPressed.put(key, true);
        }
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
        if (keysPressed.containsKey(key)) {
            keysPressed.replace(key, false);
        }
        keysToIgnoreUntilReleased.remove(key);
    }

    /**
     * Returns wheter a character is pressed or not.
     * 
     * @param c The character to be checked
     * @return boolean True if pressed otherwise false
     */
    public boolean keyIsPressed(char c) {
        if (keysPressed.containsKey(c)) {
            return keysPressed.get(c);
        }
        return false;
    }

    /**
     * to be done
     * 
     * @return to be done
     */
    public LinkedList<Character> getPressedCharacters(){
        LinkedList<Character> pressed = new LinkedList<Character>();
        for (Entry<Character, Boolean> entry : keysPressed.entrySet()) {
            if (entry.getValue()) {
                pressed.add(entry.getKey());
            }
        }
        return pressed;
    }

    public void ignoreKeyUntilReleased(Character c){
        if (keysPressed.containsKey(c)) {
            keysPressed.replace(c, false);
        }
        keysToIgnoreUntilReleased.add(c);
    }
}
