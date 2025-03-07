package game;

import java.util.ArrayList;
import java.util.HashMap;

import main.InputHandler;

/**
 * to be done
 */
public class GameInputHandler {
    /**
     * to be done
     */
    private InputHandler inputHandler;

    /**
     * to be done
     */
    private HashMap<Character, String> inputMap;
    private HashMap<Character, String> actionMap;

    /**
     * to be done
     */
    private GameController gameController;

    /**
     * to be done
     */
    public GameInputHandler(GameController pGC, InputHandler pIH) {
        gameController = pGC;
        inputHandler = pIH;

        inputMap = new HashMap<Character, String>();
        inputMap.put('w', "moveUp");
        inputMap.put('s', "moveDown");
        inputMap.put('a', "moveLeft");
        inputMap.put('d', "moveRight");
        
        actionMap = new HashMap<Character, String>();
        actionMap.put('1', "placeConveyorBelt");
        actionMap.put('2', "placeExtractor");
        actionMap.put('3', "placeSmelter"); 
        actionMap.put('r', "rotateBuilding");
        actionMap.put((char) 27, "cancelPlacement"); // esc
        actionMap.put('b', "placeBuilding");
        actionMap.put('x', "deleteBuilding");
    }

    public void invokeMethodsFromInput() {
        ArrayList<String> actions = new ArrayList<String>();
        for (Character c : inputMap.keySet()) {
            if (inputHandler.keyIsPressed(c)) {
                actions.add(inputMap.get(c));
            }
        }

        for (Character c : actionMap.keySet()) {
            if (inputHandler.keyWasTyped(c))
                actions.add(actionMap.get(c));
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
            gameController.movePlayer('Q');
        } else if (actions.contains("moveUp") && actions.contains("moveRight")) {
            actions.remove("moveUp");
            actions.remove("moveRight");
            gameController.movePlayer('E');
        } else if (actions.contains("moveDown") && actions.contains("moveLeft")) {
            actions.remove("moveDown");
            actions.remove("moveLeft");
            gameController.movePlayer('Y');
        } else if (actions.contains("moveDown") && actions.contains("moveRight")) {
            actions.remove("moveDown");
            actions.remove("moveRight");
            gameController.movePlayer('C');
        }

        // run everything else
        if (actions.contains("moveUp")) {
            gameController.movePlayer('W');
        }
        if (actions.contains("moveDown")) {
            gameController.movePlayer('S');
        }
        if (actions.contains("moveLeft")) {
            gameController.movePlayer('A');
        }
        if (actions.contains("moveRight")) {
            gameController.movePlayer('D');
        }
        if (actions.contains("placeConveyorBelt")) {
            gameController.chooseBuildingToPlace("ConveyorBelt");
        }
        if (actions.contains("placeExtractor")) {
            gameController.chooseBuildingToPlace("Extractor");
        }
        if (actions.contains("placeSmelter")) {
            gameController.chooseBuildingToPlace("Smelter");
        }
        if (actions.contains("cancelPlacement")) {
            gameController.cancelPlacement();
        }
        if (actions.contains("rotateBuilding")) {
            gameController.rotateBuilding();
        }
        if (actions.contains("placeBuilding")) {
            gameController.placeBuilding();
        }
        if (actions.contains("deleteBuilding")) {
            gameController.removeBuilding();
        }
    }
}
