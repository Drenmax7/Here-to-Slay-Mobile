package com.example.jeudecarte.HereToSlay.card;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class that represents the mask item cards
 */
public class Mask extends Item {
    //Constructors

    public Mask(String name) {
        super(name);
    }

    /**
     * Create mask card from json object
     *
     * @param json the json object that represents the card
     */
    public Mask(JSONObject json) throws JSONException {
        super(json);
    }
}
