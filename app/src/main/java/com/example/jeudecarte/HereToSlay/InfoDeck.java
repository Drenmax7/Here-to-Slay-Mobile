package com.example.jeudecarte.HereToSlay;

import static com.example.jeudecarte.HereToSlay.view.HereToSlay.GENERIC;

import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;


public class InfoDeck {
    //Attributes
    private static final String TAG = GENERIC + "INFO_DECK";

    /**
     * Name of the json file containing all the info about cards
     */
    private static final String INFO_NAME = "cards_here_to_slay/infoDeck.json";

    /**
     * The json object extract from the json file
     */
    private static JSONObject infoDeck;

    /**
     * The arrayList in which are stored all heroes, leaders and monsters names
     * Initialized by the setNameList method
     */
    private static ArrayList<String> nameList;


    //Methods


    /**
     * Fill all attributes based on the json file
     *
     * @param assetManager used to get all names. Can be obtain through the getAssets method in an activity
     */
    public static void importInfoDeck(AssetManager assetManager){
        //read the file
        String json = null;
        try {
            InputStream is = assetManager.open(INFO_NAME);
            int size = is.available();
            byte[] buffer = new byte[size];
            int ignored = is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
            infoDeck = new JSONObject(json);
        } catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }

        setNameList();
    }



    /**
     * Get all heroes, leaders and monsters names and put them all in nameList
     * The method should be called before any use of getRandomName method
     */
    public static void setNameList(){
        nameList = new ArrayList<>();

        //every folder containing interesting names
        String[] classes = new String[]{
                "hero",
                "leader",
                "monster"
        };

        try {
            //iterate trough all extension
            Iterator<String> extensionIterator = infoDeck.keys();
            while(extensionIterator.hasNext()) {
                String extensionName = extensionIterator.next();
                JSONObject extension = infoDeck.getJSONObject(extensionName);

                //check if hero, leader and monster categories are present in extension
                for (String categoryName : classes){
                    if (extension.has(categoryName)) {
                        JSONObject category = extension.getJSONObject(categoryName);
                        Iterator<String> nameIterator = category.keys();

                        //get all card name
                        while (nameIterator.hasNext()) {
                            String cardName = nameIterator.next();
                            nameList.add(cardName);
                        }
                    }
                }
            }
        }
        catch (Exception exception){
            throw new RuntimeException(exception);
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
