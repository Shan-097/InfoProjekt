package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;
import java.util.HashMap;

/**
 * 
 */
public class InputHandler implements KeyListener {
    private Map<Character, Boolean> keysPressed;

    /**
     * 
     */
    public InputHandler(){
        keysPressed = new HashMap<Character, Boolean>();
    }

    
    /** 
     * @param e
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    
    /** 
     * @param e
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

    
    /** 
     * @param e
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
    
    
    /** 
     * @param c
     * @return boolean
     */
    public boolean keyIsPressed(char c){
        if(keysPressed.containsKey(c)){
            return keysPressed.get(c);
        }
        return false;
    }

}
