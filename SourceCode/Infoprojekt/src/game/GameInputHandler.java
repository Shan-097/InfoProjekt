package game;

import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.Clip;

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
        inputMap.put('1', "placeConveyorBelt");
        inputMap.put('2', "placeExtractor");
        inputMap.put('3', "placeSmelter");
        inputMap.put('r', "rotateBuilding");
        inputMap.put((char) 27, "cancelPlacement"); // esc
        inputMap.put('b', "placeBuilding");
        inputMap.put('x', "deleteBuilding");
        inputMap.put('+', "increaseVolume");
        inputMap.put('-', "decreaseVolume");
    }

    public void invokeMethodsFromInput() {
        ArrayList<String> actions = new ArrayList<String>();
        for (Character c : inputMap.keySet()) {
            if (inputHandler.keyIsPressed(c)) {
                actions.add(inputMap.get(c));
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
        if (actions.contains("decreaseVolume") && actions.contains("increaseVolume")) {
            actions.remove("decreaseVolume");
            actions.remove("increaseVolume");
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
        if (actions.contains("increaseVolume")) {
            Music.setVolumeHigher();
        }
        if (actions.contains("decreaseVolume")) { 
            Music.setVolumeLower();

        }
    }
}
