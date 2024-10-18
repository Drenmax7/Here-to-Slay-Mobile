package com.example.jeudecarte.HereToSlay.card;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class that represents the cursed item cards
 */
public class Cursed extends Item {
    //Constructors

    public Cursed(String name) {
        super(name);
    }

    /**
     * Create cursed card from json object
     *
     * @param json the json object that represents the card
     */
    public Cursed(JSONObject json) throws JSONException {
        super(json);
    }
}
