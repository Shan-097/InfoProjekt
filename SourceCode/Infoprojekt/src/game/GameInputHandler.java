package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import main.InputHandler;
import main.LoadStoreHotKeys;

/**
 * The GameInputHandler calls the methods that have to be invoked because of
 * player input.
 */
public class GameInputHandler {
    /**
     * The InputHandler object of the GameInputHandler.
     */
    private InputHandler inputHandler;

    /**
     * The Map linking actions and their respective inputs (Characters)
     * together.<br>
     * Every action should be associated with exactily one input. The Map is
     * therefor reversible without losses.
     */
    private HashMap<String, Character> inputMap;

    /**
     * The reversed Map linking actions and their respective inputs (Characters)
     * together.
     */
    private HashMap<Character, String> reversedInputMap;

    /**
     * The GameController object of the game.
     */
    private GameController gameController;

    /**
     * The constructor of GameInputHandler sets the gameController object of this
     * instance of the game and the inputHandler of the JPanel. It also loads the
     * inputMap.
     * 
     * @param pGC The gameController object of this instance
     * @param pIH The inputHandler of the JPanel
     */
    public GameInputHandler(GameController pGC, InputHandler pIH) {
        gameController = pGC;
        inputHandler = pIH;

        inputMap = LoadStoreHotKeys.loadHotKeys();
        reversedInputMap = new HashMap<Character, String>();
        for (Entry<String, Character> inputMapping : inputMap.entrySet()) {
            reversedInputMap.put(inputMapping.getValue(), inputMapping.getKey());
        }
    }

    /**
     * Invokes the methods of which the keys associated with their action is
     * pressed.
     */
    public void invokeMethodsFromInput() {
        ArrayList<String> actions = new ArrayList<String>();
        for (Character c : reversedInputMap.keySet()) {
            if (inputHandler.keyIsPressed(c)) {
                actions.add(reversedInputMap.get(c));
            }
        }

        // check for illegal combinations and remove them
        if (actions.contains("moveUp") && actions.contains("moveDown")) {
            actions.remove("moveUp");
            actions.remove("moveDown");
        }
        if (actions.contains("moveLeft") && actions.contains("moveRight")) {
            actions.remove("moveLeft");
            actions.remove("moveRight");
        }
        if (actions.contains("cancelPlacement") && actions.contains("placeConveyorBelt")) {
            actions.remove("placeConveyorBelt");
        }
        if (actions.contains("cancelPlacement") && actions.contains("placeExtractor")) {
            actions.remove("placeExtractor");
        }
        if (actions.contains("cancelPlacement") && actions.contains("placeSmelter")) {
            actions.remove("placeSmelter");
        }
        if (actions.contains("cancelPlacement") && actions.contains("placeBuilding")) {
            actions.remove("placeBuilding");
        }
        if (actions.contains("deleteBuilding") && actions.contains("placeBuilding")) {
            actions.remove("placeBuilding");
            actions.remove("deleteBuilding");
        }

        // run combinations
        if (actions.contains("moveUp") && actions.contains("moveLeft")) {
            actions.remove("moveUp");
            actions.remove("moveLeft");
            try {
                gameController.movePlayer('Q');
            } catch (IllegalArgumentException e) {
            }
        } else if (actions.contains("moveUp") && actions.contains("moveRight")) {
            actions.remove("moveUp");
            actions.remove("moveRight");
            try {
                gameController.movePlayer('E');
            } catch (IllegalArgumentException e) {
            }
        } else if (actions.contains("moveDown") && actions.contains("moveLeft")) {
            actions.remove("moveDown");
            actions.remove("moveLeft");
            try {
                gameController.movePlayer('Y');
            } catch (IllegalArgumentException e) {
            }
        } else if (actions.contains("moveDown") && actions.contains("moveRight")) {
            actions.remove("moveDown");
            actions.remove("moveRight");
            try {
                gameController.movePlayer('C');
            } catch (IllegalArgumentException e) {
            }
        }

        // run everything else
        if (actions.contains("moveUp")) {
            try {
                gameController.movePlayer('W');
            } catch (IllegalArgumentException e) {
            }
        }
        if (actions.contains("moveDown")) {
            try {
                gameController.movePlayer('S');
            } catch (IllegalArgumentException e) {
            }
        }
        if (actions.contains("moveLeft")) {
            try {
                gameController.movePlayer('A');
            } catch (IllegalArgumentException e) {
            }
        }
        if (actions.contains("moveRight")) {
            try {
                gameController.movePlayer('D');
            } catch (IllegalArgumentException e) {
            }
        }
        if (actions.contains("placeConveyorBelt")) {
            try {
                gameController.chooseBuildingToPlace("ConveyorBelt");
            } catch (IllegalArgumentException e) {
            }
        }
        if (actions.contains("placeExtractor")) {
            try {
                gameController.chooseBuildingToPlace("Extractor");
            } catch (IllegalArgumentException e) {
            }
        }
        if (actions.contains("placeSmelter")) {
            try {
                gameController.chooseBuildingToPlace("Smelter");
            } catch (IllegalArgumentException e) {
            }
        }
        if (actions.contains("cancelPlacement")) {
            gameController.cancelPlacement();
        }
        if (actions.contains("rotateBuilding")) {
            gameController.rotateBuilding();
            inputHandler.ignoreKeyUntilReleased(inputMap.get("rotateBuilding"));
        }
        if (actions.contains("placeBuilding")) {
            gameController.placeBuilding();
        }
        if (actions.contains("deleteBuilding")) {
            gameController.removeBuilding();
        }
    }
}
