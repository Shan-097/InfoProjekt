package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import main.InputHandler;
import main.LoadStoreHotKeys;

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
    private HashMap<Character, String> reversedInputMapNormal;

    /**
     * to be done
     */
    private HashMap<Character, String> reversedInputMapNotHoldable;

    /**
     * to be done
     */
    private HashMap<Character, String> reversedInputMapHoldable;

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

        HashMap<String, HashMap<String, Character>> inputMaps = LoadStoreHotKeys.loadHotKeys();

        reversedInputMapNormal = new HashMap<Character, String>();
        for (Entry<String, Character> inputMapping : inputMaps.get("normal").entrySet()) {
            reversedInputMapNormal.put(inputMapping.getValue(), inputMapping.getKey());
        }
        reversedInputMapNotHoldable = new HashMap<Character, String>();
        for (Entry<String, Character> inputMapping : inputMaps.get("not_holdable").entrySet()) {
            reversedInputMapNotHoldable.put(inputMapping.getValue(), inputMapping.getKey());
        }
        reversedInputMapHoldable = new HashMap<Character, String>();
        for (Entry<String, Character> inputMapping : inputMaps.get("holdable").entrySet()) {
            reversedInputMapHoldable.put(inputMapping.getValue(), inputMapping.getKey());
        }
    }

    public void invokeMethodsFromInput() {
        ArrayList<String> actions = new ArrayList<String>();
        for (Character c : reversedInputMapNormal.keySet()) {
            if (inputHandler.keyIsPressed(c)) {
                actions.add(reversedInputMapNormal.get(c));
            }
        }
        for (Character c : reversedInputMapNotHoldable.keySet()) {
            if (inputHandler.keyIsHold(c)) {
                actions.add(reversedInputMapNotHoldable.get(c));
            }
        }
        for (Character c : reversedInputMapHoldable.keySet()) {
            if (inputHandler.keyIsClicked(c)) {
                actions.add(reversedInputMapHoldable.get(c));
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
