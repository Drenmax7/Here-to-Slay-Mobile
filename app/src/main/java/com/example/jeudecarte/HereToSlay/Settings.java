package com.example.jeudecarte.HereToSlay;

import org.json.JSONException;
import org.json.JSONObject;

public class Settings {
    //Personal settings
    public static String name = "";
    public static String language = "english";


    //Game settings
    public static int playerNumber = 2;
    public static int gameRule = 0;

    public static boolean warriorDruid = true;
    public static boolean berserkerNecromancer = true;
    public static boolean sorcerer = true;
    public static boolean monster = true;
    public static boolean leader = true;
    public static boolean hereToSleigh = false;

    public static int actionPoint = 3;
    public static int cardLimit = -1;
    public static int monsterGoal = 3;
    public static int heroCount = 8;
    public static int categoryCap = 1;

    public static int forcedReroll = 1;

    public static void resetSettings(){
        playerNumber = 2;
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

    // Fonction pour retourner un objet JSON avec les paramètres
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

    // Fonction pour mettre à jour les paramètres à partir d'un objet JSON sans vérifications
    public static void importParameter(JSONObject json) throws JSONException {
        // Affecter chaque paramètre directement
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
}
