package com.example.jeudecarte.HereToSlay.card;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class that represents the monster cards
 */
public class Monster extends Card{
    //Constructors

    public Monster(String name) {
        super(name);
    }

    /**
     * Create monster card from json object
     *
     * @param json the json object that represents the card
     */
    public Monster(JSONObject json) throws JSONException {
        super(json);
    }
}
