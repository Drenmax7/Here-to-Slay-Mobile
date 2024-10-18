package com.example.jeudecarte.HereToSlay.card;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The class from which all cards inherit
 */
abstract class Card {
    //Attributes

    /**
     * The name of the card
     */
    String name;


    //Constructors

    /**
     * Initialize all attributes
     *
     * @param name The name of the card
     */
    Card(String name){
        this.name = name;
    }

    /**
     * Initialise a card from a json object
     *
     * @param json The json object representing the card
     */
    Card(JSONObject json) throws JSONException {
        name = json.getString("name");
    }


    //Methods

    /**
     * Convert the card to a json object
     *
     * @return A json object representing the card
     */
    public JSONObject convertJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("name",name);

        return json;
    }
}
