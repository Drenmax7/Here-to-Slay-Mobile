package com.example.jeudecarte.HereToSlay.card;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Abstract class that all class representing cards that are an item inherit
 */
public abstract class Item extends Turn{
    //Constructors

    public Item(String name) {
        super(name);
    }

    /**
     * Create item card from json object
     *
     * @param json the json object that represents the card
     */
    public Item(JSONObject json) throws JSONException {
        super(json);
    }
}
