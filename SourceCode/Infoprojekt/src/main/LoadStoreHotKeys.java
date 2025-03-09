package main;

import java.io.File;
import java.io.FileWriter;

import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONObject;

import game.GameController;

/**
 * The class LoadStoreHotKeys manages loading and storing of the hot keys.
 */
public class LoadStoreHotKeys {
    /**
     * The standard input map is used befor the first modification and if something
     * goes wrong while loading the hot keys.
     */
    private static final HashMap<String, Character> STANDARD_INPUT_MAP;

    /**
     * The static and final attributes are declared here.
     */
    static {
        STANDARD_INPUT_MAP = new HashMap<String, Character>(11);
        STANDARD_INPUT_MAP.put("moveUp", 'w');
        STANDARD_INPUT_MAP.put("moveDown", 's');
        STANDARD_INPUT_MAP.put("moveLeft", 'a');
        STANDARD_INPUT_MAP.put("moveRight", 'd');
        STANDARD_INPUT_MAP.put("placeConveyorBelt", '1');
        STANDARD_INPUT_MAP.put("placeExtractor", '2');
        STANDARD_INPUT_MAP.put("placeSmelter", '3');
        STANDARD_INPUT_MAP.put("rotateBuilding", 'r');
        STANDARD_INPUT_MAP.put("cancelPlacement", (char) 27);
        STANDARD_INPUT_MAP.put("placeBuilding", 'b');
        STANDARD_INPUT_MAP.put("deleteBuilding", 'x');
    }

    /**
     * Stores the given input map.
     * 
     * @param inputMap The input map to be stored
     */
    public static void storeHotKeys(HashMap<String, Character> inputMap) {
        if (!new File("./config/HotKeys.json").isFile()) {

        }
        try (FileWriter file = new FileWriter("./config/HotKeys.json")) {
            JSONObject hotkeys = new JSONObject();
            for (Entry<String, Character> inputMapping : inputMap.entrySet()) {
                hotkeys.put(inputMapping.getKey(), inputMapping.getValue());
            }
            file.write(hotkeys.toString(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads and returns the stored input map or returns the standard input map if an error occurs.
     * 
     * @return The input map
     */
    public static HashMap<String, Character> loadHotKeys() {
        if (!new File("./config/HotKeys.json").isFile()) {
            return STANDARD_INPUT_MAP;
        }
        JSONObject hotkeys = GameController.readJsonFile("./config/HotKeys.json");
        HashMap<String, Character> inputMap = new HashMap<String, Character>(11);
        for (String key : hotkeys.keySet()) {
            String value;
            char pressedKey;
            try {
                value = hotkeys.getString(key);
                pressedKey = value.toCharArray()[0];
            } catch (Exception e) {
                return STANDARD_INPUT_MAP;
            }
            if (value.length() != 1 || (!STANDARD_INPUT_MAP.containsKey(key)) || inputMap.containsValue(pressedKey)) {
                return STANDARD_INPUT_MAP;
            }
            inputMap.put(key, pressedKey);
        }
        for (String key : STANDARD_INPUT_MAP.keySet()) {
            if (!inputMap.containsKey(key)) {
                return STANDARD_INPUT_MAP;
            }
        }
        return inputMap;
    }
}
