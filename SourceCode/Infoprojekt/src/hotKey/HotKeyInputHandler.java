package hotKey;

import java.util.LinkedList;

import main.InputHandler;

/**
 * The HotKeyInputHandler is used to get the only pressed char of the input
 * handler at any given moment.
 */
public class HotKeyInputHandler {
    /**
     * The input handler object of the HotKeyInputHandler.
     */
    private InputHandler inputHandler;

    /**
     * The constructor of the HotKeyInputHandler only sets the input handler object.
     */
    public HotKeyInputHandler(InputHandler pIH) {
        inputHandler = pIH;
    }

    /**
     * If exactly one character is pressed it is returned. Otherwise null will be returned.
     * 
     * @return The character or null
     */
    public Character getPressedChar() {
        LinkedList<Character> pressed = inputHandler.getPressedCharacters();
        if (pressed.size() == 1) {
            return pressed.getFirst();
        }
        return null;
    }
}
