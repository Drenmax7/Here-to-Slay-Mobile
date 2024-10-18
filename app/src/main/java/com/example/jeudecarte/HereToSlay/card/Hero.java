package com.example.jeudecarte.HereToSlay.card;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class that represents the hero cards
 */
public class Hero extends Turn {
    //Constructors

    public Hero(String name) {
        super(name);
    }

    /**
     * Create hero card from json object
     *
     * @param json the json object that represents the card
     */
    public Hero(JSONObject json) throws JSONException {
        super(json);
    }
}
