package com.example.jeudecarte.HereToSlay.card;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class that represents the normal item cards
 */
public class Good extends Item {
    //Constructors

    public Good(String name) {
        super(name);
    }

    /**
     * Create good item card from json object
     *
     * @param json the json object that represents the card
     */
    public Good(JSONObject json) throws JSONException {
        super(json);
    }
}
