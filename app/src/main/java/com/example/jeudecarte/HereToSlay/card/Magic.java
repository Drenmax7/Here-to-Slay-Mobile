package com.example.jeudecarte.HereToSlay.card;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class that represent the magic cards
 */
public class Magic extends Turn {
    //Constructors

    public Magic(String name) {
        super(name);
    }

    /**
     * Create magic card from json object
     *
     * @param json the json object that represents the card
     */
    public Magic(JSONObject json) throws JSONException {
        super(json);
    }
}
