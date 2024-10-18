package com.example.jeudecarte.HereToSlay;

import static com.example.jeudecarte.HereToSlay.view.HereToSlay.GENERIC;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * This class is used to get all information from the json file
 * Before getting any information, the importInfoDeck function must be called
 */
public class InfoDeck {
    //Attributes
    /**
     * The tag showed in the logcat console
     */
    private static final String TAG = GENERIC + "INFO_DECK";

    /**
     * The path to the folder of all images
     */
    private static final String PATH = "cards_here_to_slay/";

    /**
     * Name of the json file containing all the info about cards
     */
    private static final String INFO_NAME = PATH + "infoDeck.json";

    /**
     * The json object extract from the json file
     */
    private static JSONObject infoDeck;

    /**
     * The arrayList in which are stored all heroes, leaders and monsters names
     * Initialized by the setNameList method
     */
    private static ArrayList<String> nameList;

    /**
     * List of all image as Drawable
     * The key is the name of the image
     */
    private static Map<String, Drawable> imageList;


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
        setImageList(assetManager);
    }

    /**
     * Get all heroes, leaders and monsters names and put them all in nameList
     * The method should be called before any use of getRandomName method
     */
    private static void setNameList(){
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
     * Fill imageList with the drawable of all the images
     *
     * @param assetManager used to get all image. Can be obtain through the getAssets method in an activity
     */
    private static void setImageList(AssetManager assetManager){
        imageList = new HashMap<>();
        try {
            //iterate trough all extension
            Iterator<String> extensionIterator = infoDeck.keys();
            while(extensionIterator.hasNext()) {
                String extensionName = extensionIterator.next();
                JSONObject extension = infoDeck.getJSONObject(extensionName);

                //iterate through all categories
                Iterator<String> categoryIterator = extension.keys();
                while(categoryIterator.hasNext()) {
                    String categoryName = categoryIterator.next();
                    JSONObject category = extension.getJSONObject(categoryName);

                    //iterate through all names
                    Iterator<String> cardIterator = category.keys();
                    while(cardIterator.hasNext()) {
                        String cardName = cardIterator.next();
                        JSONObject card = category.getJSONObject(cardName);

                        //open image
                        String path = card.getString("path");
                        try (InputStream inputStream = assetManager.open(PATH+path)) {
                            Drawable drawable = Drawable.createFromStream(inputStream, null);
                            imageList.put(cardName,drawable);
                        }
                        catch (IOException e) {
                            Log.d(TAG,"error : " + e.getMessage());
                            throw new RuntimeException(e);
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

    /**
     * Get the drawable of the image by name
     *
     * @param path The name of the image to get the drawable
     *
     * @return the drawable of the image whose name is specified
     */
    public static Drawable getDrawableByName(String path){
        return imageList.get(path);
    }
}
