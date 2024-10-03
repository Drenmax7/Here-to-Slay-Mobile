package com.example.jeudecarte.HereToSlay;

public class Settings {
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
}
