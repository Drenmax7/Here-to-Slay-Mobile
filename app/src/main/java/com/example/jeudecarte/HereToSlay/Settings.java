package com.example.jeudecarte.HereToSlay;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class regroup all settings for a game as well as personal ones
 * When joining a game all game setting will be changed by the Hub while receiving data
 */
public class Settings {
    //Personal settings

    /**
     * The pseudo of the player
     */
    public static String name = "";

    /**
     * The preferred language of the player
     */
    public static String language = "english";


    //Game settings

    /**
     * Number of player in the game
     */
    //todo put back 4 as basic number
    public static int playerNumber = 12;

    /**
     * Which game rule is used
     *      0 : Normal
     *      1 : 2 leaders each
     *      2 : Teams
     */
    public static int gameRule = 0;


    /**
     * If the game is played with the Warrior & Druid extension
     */
    public static boolean warriorDruid = true;

    /**
     * If the game is played with the Berserker & Necromancer extension
     */
    public static boolean berserkerNecromancer = true;

    /**
     * If the game is played with the Dragon Sorcerer extension
     */
    public static boolean sorcerer = true;

    /**
     * If the game is played with Monster extension
     */
    public static boolean monster = true;

    /**
     * If the game is played with the Leader extension
     */
    public static boolean leader = true;

    /**
     * If the game is played with the Here To Sleigh extension
     */
    public static boolean hereToSleigh = false;


    /**
     * How many action points per turn
     */
    public static int actionPoint = 3;

    /**
     * How many card can one have at the end of his turn
     * -1 for no limits
     */
    public static int cardLimit = -1;

    /**
     * How many monster does one have to slay before winning
     */
    public static int monsterGoal = 3;

    /**
     * How many heroes does one have to posses in order to win
     */
    public static int heroCount = 8;

    /**
     * How many heroes of the same class can be counted while counting for the win
     * Cap of 1 mean all 8 heroes must be of different classes
     */
    public static int categoryCap = 1;

    /**
     * How many times can one change someone (including himself) leader in the hub
     */
    public static int forcedReroll = 1;


    //Methods

    /**
     * Reset all game parameters to their original state
     */
    public static void resetSettings(){
        playerNumber = 4;
        gameRule = 0;

        warriorDruid = true;
        berserkerNecromancer = true;
        sorcerer = true;
        monster = true;
        leader = true;
        hereToSleigh = false;

        actionPoint = 3;
        cardLimit = -1;
        monsterGoal = 3;
        heroCount = 8;
        categoryCap = 1;

        forcedReroll = 1;
    }

    /**
     * Export all game parameters as a Json object
     *
     * @return Json object containing all game parameters
     * @throws JSONException if somehow trash has been put as values of parameters
     */
    public static JSONObject exportParameter() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("playerNumber", playerNumber);
        json.put("gameRule", gameRule);

        json.put("warriorDruid", warriorDruid);
        json.put("berserkerNecromancer", berserkerNecromancer);
        json.put("sorcerer", sorcerer);
        json.put("monster", monster);
        json.put("leader", leader);
        json.put("hereToSleigh", hereToSleigh);

        json.put("actionPoint", actionPoint);
        json.put("cardLimit", cardLimit);
        json.put("monsterGoal", monsterGoal);
        json.put("heroCount", heroCount);
        json.put("categoryCap", categoryCap);

        json.put("forcedReroll", forcedReroll);

        return json;
    }

    /**
     * Import all game parameters from a Json object
     * MUST contain all parameters !
     *
     * @param json The json object containing ALL parameters
     * @throws JSONException if the json object does not contains ALL parameter
     */
    public static void importParameter(JSONObject json) throws JSONException {
        playerNumber = json.getInt("playerNumber");
        gameRule = json.getInt("gameRule");

        warriorDruid = json.getBoolean("warriorDruid");
        berserkerNecromancer = json.getBoolean("berserkerNecromancer");
        sorcerer = json.getBoolean("sorcerer");
        monster = json.getBoolean("monster");
        leader = json.getBoolean("leader");
        hereToSleigh = json.getBoolean("hereToSleigh");

        actionPoint = json.getInt("actionPoint");
        cardLimit = json.getInt("cardLimit");
        monsterGoal = json.getInt("monsterGoal");
        heroCount = json.getInt("heroCount");
        categoryCap = json.getInt("categoryCap");

        forcedReroll = json.getInt("forcedReroll");
    }

    public static boolean checkExtensionActive(String extensionName){
        switch (extensionName){
            case "normal":
                return true;
            case "warriors and druids":
                return warriorDruid;
            case "berserkers and necromancers":
                return berserkerNecromancer;
            case "dragon sorcerer":
                return sorcerer;
            case "here to sleigh":
                return hereToSleigh;
            case "leader":
                return leader;
            case "monster":
                return monster;
            default:
                Exception exception = new Exception(extensionName + " is not a valid extension name");
                throw new RuntimeException(exception);
        }
    }
}
