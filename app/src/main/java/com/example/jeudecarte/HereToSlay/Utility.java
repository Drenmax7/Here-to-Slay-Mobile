package com.example.jeudecarte.HereToSlay;

import android.content.res.AssetManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * The purpose of this class is to contain useful functions that accomplish simple tasks
 */
public class Utility {
    //Attributes

    /**
     * The arrayList in which are stored all heroes, leaders and monsters names
     * Initialized by the setNameList method
     */
    private static ArrayList<String> nameList;


    //Methods

    /**
     * Create a simple Json object whose values are the arguments
     * Design to be sent as packet
     *
     * @param name The name of the packet
     * @param value The information sent : might be a string or an other json
     * @param target The person the packet should be delivered to :
     *               'server', the controller
     *               'player', the player that sent the previous packet to the controller
     *               'all', every player (not the controller)
     *
     * @return The Json object acting as a packet
     * @throws JSONException if values are too weird
     */
    public static JSONObject generateJson(String name, Object value, String target) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("name",name);
        json.put("value",value);
        json.put("target",target);

        return json;
    }

    /**
     * Get all heroes, leaders and monsters names and put them all in nameList
     * The method should be called before any use of getRandomName method
     *
     * @param assetManager used to get all names. Can be obtain through the getAssets method in an activity
     */
    public static void setNameList(AssetManager assetManager){
        nameList = new ArrayList<>();

        //every folder containing interesting names
        String[] classes = new String[]{
                "bard",
                "berserker",
                "druid",
                "fighter",
                "guardian",
                "necromancer",
                "ranger",
                "sorcerer",
                "thief",
                "warrior",
                "wizard",
                "leader",
                "monster"
        };

        try {
            for (String oneClass : classes){
                String [] names = assetManager.list("cards_here_to_slay/" + oneClass);
                nameList.addAll(Arrays.asList(names));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //names obtain contains .png at the end, the for loop remove it
        for (int i = 0; i < nameList.size(); i++){
            String name = nameList.get(i);
            nameList.set(i,name.substring(0,name.length()-4));
        }
    }

    /**
     * Generate a random name
     * Names are taken from heroes, leaders and monsters names
     * The list must be initialized first via setNameList
     *
     * @return a random name
     */
    public static String getRandomName(){
        Random rand = new Random();
        return nameList.get(rand.nextInt(nameList.size()));
    }
}
