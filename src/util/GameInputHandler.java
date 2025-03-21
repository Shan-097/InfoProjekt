package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import game.GameController;

import main.GamePanel;

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
     * The reversed input map of actions with normal input behavior.
     */
    private HashMap<Character, String> reversedInputMapNormal;

    /**
     * The reversed input map of actions that can't be hold.
     */
    private HashMap<Character, String> reversedInputMapNotHoldable;

    /**
     * The reversed input map of actions that can be hold.
     */
    private HashMap<Character, String> reversedInputMapHoldable;

    /**
     * The GameController object of the game.
     */
    private GameController gameController;

    /**
     * The game panel object.
     */
    private GamePanel gamePanel;

    /**
     * The constructor of GameInputHandler sets the gameController object of this
     * instance of the game and the inputHandler of the JPanel. It also loads the
     * inputMap.
     * 
     * @param pGC The gameController object of this instance
     * @param pIH The inputHandler of the JPanel
     * @param pGP The game panel object.
     */
    public GameInputHandler(GameController pGC, GamePanel pGP, InputHandler pIH) {
        gameController = pGC;
        gamePanel = pGP;
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

    /**
     * Invokes the methods of which the keys associated with their action is
     * pressed.
     */
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
        if (actions.contains("decreaseVolume") && actions.contains("increaseVolume")) {
            actions.remove("decreaseVolume");
            actions.remove("increaseVolume");
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
        if (actions.contains("pauseMenu")) {
            gamePanel.showPauseMenu();
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

    /**
     * Returns the key of a given action.
     * 
     * @param s The given action.
     * @return The string of the key.
     */
    public String getKey(String s){
        if (s == null) {
            return "";
        }
        for (Entry<Character, String> entry : reversedInputMapNormal.entrySet()) {
            if (entry.getValue().equals(s)) {
                return charToHumanReadableString(entry.getKey());
            }
        }
        for (Entry<Character, String> entry : reversedInputMapHoldable.entrySet()) {
            if (entry.getValue().equals(s)) {
                return charToHumanReadableString(entry.getKey());
            }
        }
        for (Entry<Character, String> entry : reversedInputMapNotHoldable.entrySet()) {
            if (entry.getValue().equals(s)) {
                return charToHumanReadableString(entry.getKey());
            }
        }
        return "";
    }

    /**
     * Converts the char to a human readable string.<br>
     * Note that not all characters are supported.<br>
     * E.g. '\u001B' to "escape"
     * 
     * @param c The character to be converted
     * @return The string of the given character in human readable form
     */
    private String charToHumanReadableString(char c) {
        // '\u001B' esc, '\u0020' space, '\u007F' delete
        switch (c) {
            case '\u001B':
                return "escape";
            case '\u0020':
                return "space";
            case '\u007F':
                return "delete";
            default:
                return String.valueOf((char) c);
        }
    }
}
