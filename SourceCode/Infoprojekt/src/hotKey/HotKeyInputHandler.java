package hotKey;

import java.util.LinkedList;

import main.InputHandler;

/**
 * to be done
 */
public class HotKeyInputHandler {
    /**
     * to be done
     */
    private InputHandler inputHandler;

    /**
     * to be done
     */
    public HotKeyInputHandler(InputHandler pIH) {
        inputHandler = pIH;
    }

    public Character getPressedChar() {
        LinkedList<Character> pressed = inputHandler.getPressedCharacters();
        if (pressed.size() == 1) {
            return pressed.getFirst();
        }
        return null;
    }
}
