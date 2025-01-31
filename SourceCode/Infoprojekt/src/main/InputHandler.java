package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

/**
 * The class of the Input handler implementing Keylistener.
 * Every Object has a collection of all keys with wether or not they are pressed.
 */
public class InputHandler implements KeyListener {
    private HashMap<Character, Boolean> keysPressed;

    /**
     * Standard constructor of the InnputHandler. 
     * Only instantiates the collection.
     */
    public InputHandler(){
        keysPressed = new HashMap<Character, Boolean>();
    }

    
    /** Is not used but has to be implemented
     * @param e The KeyEvent
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    
    /** Sets the boolean wheter the key is typed or not to true or adds the entry to the collection.
     * @param e The registered KeyEvent
     */
    @Override
    public void keyPressed(KeyEvent e) {
        char key = e.getKeyChar();
        if(keysPressed.containsKey(key)){
            keysPressed.replace(key, true);
        } else {
            keysPressed.put(key, true);
        }
    }

    
    /** Sets the boolean wheter the key is typed or not to false or adds the entry to the collection.
     * @param e The registered KeyEvent
     */
    @Override
    public void keyReleased(KeyEvent e) {
        char key = e.getKeyChar();
        if(keysPressed.containsKey(key)){
            keysPressed.replace(key, false);
        } else {
            keysPressed.put(key, false);
        }
    }
    
    
    /** Returns wheter a character is pressed or not.
     * @param c The character to be checked
     * @return boolean True if pressed otherwise false
     */
    public boolean keyIsPressed(char c){
        if(keysPressed.containsKey(c)){
            return keysPressed.get(c);
        }
        return false;
    }

}
