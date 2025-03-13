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
     * to be done
     */
    private static final HashMap<String, Character> STANDARD_INPUT_MAP_HOLDABLE;

    /**
     * to be done
     */
    private static final HashMap<String, Character> STANDARD_INPUT_MAP_NORMAL;

    /**
     * to be done
     */
    private static final HashMap<String, Character> STANDARD_INPUT_MAP_NOT_HOLDABLE;

    /**
     * The static and final attributes are declared here.
     */
    static {
        STANDARD_INPUT_MAP_NORMAL = new HashMap<String, Character>(4);
        STANDARD_INPUT_MAP_NORMAL.put("moveUp", 'w');
        STANDARD_INPUT_MAP_NORMAL.put("moveDown", 's');
        STANDARD_INPUT_MAP_NORMAL.put("moveLeft", 'a');
        STANDARD_INPUT_MAP_NORMAL.put("moveRight", 'd');

        STANDARD_INPUT_MAP_NOT_HOLDABLE = new HashMap<String, Character>(5);
        STANDARD_INPUT_MAP_NOT_HOLDABLE.put("placeConveyorBelt", '1');
        STANDARD_INPUT_MAP_NOT_HOLDABLE.put("placeExtractor", '2');
        STANDARD_INPUT_MAP_NOT_HOLDABLE.put("placeSmelter", '3');
        STANDARD_INPUT_MAP_NOT_HOLDABLE.put("rotateBuilding", 'r');
        STANDARD_INPUT_MAP_NOT_HOLDABLE.put("cancelPlacement", (char) 27);

        STANDARD_INPUT_MAP_HOLDABLE = new HashMap<String, Character>(2);
        STANDARD_INPUT_MAP_HOLDABLE.put("placeBuilding", 'b');
        STANDARD_INPUT_MAP_HOLDABLE.put("deleteBuilding", 'x');
        STANDARD_INPUT_MAP_HOLDABLE.put("increaseVolume", '+');
        STANDARD_INPUT_MAP_HOLDABLE.put("decreaseVolume", '-');
    }

    /**
     * Stores the given input maps.
     * 
     * @return to be done
     * @param iMN  to be done
     * @param iMNH to be done
     * @param iMH  to be done
     */
    public static boolean storeHotKeys(HashMap<String, Character> iMN, HashMap<String, Character> iMNH,
            HashMap<String, Character> iMH) {
        try {
            File directory = new File("./config/");
            if (!directory.isDirectory()) {
                directory.mkdir();
            }
        } catch (SecurityException e) {
            return false;
        }
        try {
            File file = new File("./config/HotKeys.json");
            if (!file.isFile()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            return false;
        }
        try (FileWriter file = new FileWriter("./config/HotKeys.json")) {
            JSONObject hotkeys = new JSONObject();
            JSONObject hotkeysNormal = new JSONObject();
            for (Entry<String, Character> inputMapping : iMN.entrySet()) {
                hotkeysNormal.put(inputMapping.getKey(), inputMapping.getValue());
            }
            hotkeys.put("normal", hotkeysNormal);

            JSONObject hotkeysNotHoldable = new JSONObject();
            for (Entry<String, Character> inputMapping : iMNH.entrySet()) {
                hotkeysNotHoldable.put(inputMapping.getKey(), inputMapping.getValue());
            }
            hotkeys.put("not_holdable", hotkeysNotHoldable);

            JSONObject hotkeysHoldable = new JSONObject();
            for (Entry<String, Character> inputMapping : iMN.entrySet()) {
                hotkeysHoldable.put(inputMapping.getKey(), inputMapping.getValue());
            }
            hotkeys.put("holdable", hotkeysHoldable);
            file.write(hotkeys.toString(0));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Loads and returns the stored input maps or returns the standard input maps if
     * an error occurs.
     * 
     * @return to be done
     */
    public static HashMap<String, HashMap<String, Character>> loadHotKeys() {
        HashMap<String, HashMap<String, Character>> standard = new HashMap<String, HashMap<String, Character>>();
        standard.put("normal", STANDARD_INPUT_MAP_NORMAL);
        standard.put("not_holdable", STANDARD_INPUT_MAP_NOT_HOLDABLE);
        standard.put("holdable", STANDARD_INPUT_MAP_HOLDABLE);

        HashMap<String, Character> unionInputMap = new HashMap<String, Character>();
        unionInputMap.putAll(STANDARD_INPUT_MAP_NORMAL);
        unionInputMap.putAll(STANDARD_INPUT_MAP_NOT_HOLDABLE);
        unionInputMap.putAll(STANDARD_INPUT_MAP_HOLDABLE);

        if (!new File("./config/HotKeys.json").isFile()) {
            return standard;
        }

        HashMap<String, Character> iMN = new HashMap<String, Character>(4);
        HashMap<String, Character> iMNH = new HashMap<String, Character>(5);
        HashMap<String, Character> iMH = new HashMap<String, Character>(2);
        JSONObject hotkeys = GameController.readJsonFile("./config/HotKeys.json");

        for (String key : hotkeys.keySet()) {
            if (key != "normal" && key != "not_holdable" && key != "holdable") {
                return standard;
            }

            try {
                JSONObject innerObject = hotkeys.getJSONObject(key);
                for (String action : innerObject.keySet()) {
                    String value;
                    char pressedKey;
                    try {
                        value = innerObject.getString(action);
                        pressedKey = value.toCharArray()[0];
                    } catch (Exception e) {
                        return standard;
                    }
                    if (value.length() != 1 || (!unionInputMap.containsKey(key)) || iMN.containsValue(pressedKey)
                            || iMNH.containsValue(pressedKey) || iMH.containsValue(pressedKey)) {
                        return standard;
                    }
                    if (key == "normal") {
                        iMN.put(action, pressedKey);
                    }
                    if (key == "not_holdable") {
                        iMNH.put(action, pressedKey);
                    }
                    if (key == "holdable") {
                        iMH.put(action, pressedKey);
                    }
                }
            } catch (Error e) {
                return standard;
            }
        }
        for (String key : unionInputMap.keySet()) {
            if (!(iMN.containsKey(key) ^ iMH.containsKey(key) ^ iMNH.containsKey(key))) {
                return standard;
            }
        }
        HashMap<String, HashMap<String, Character>> returnInputMap = new HashMap<String, HashMap<String, Character>>();
        returnInputMap.put("normal", iMN);
        returnInputMap.put("not_holdable", iMNH);
        returnInputMap.put("holdable", iMH);
        return returnInputMap;
    }
}
