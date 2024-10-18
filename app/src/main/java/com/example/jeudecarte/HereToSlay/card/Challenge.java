package com.example.jeudecarte.HereToSlay.card;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class that represents the challenge cards
 */
public class Challenge extends Anytime {
    //Constructors

    public Challenge(String name){
        super(name);
    }

    /**
     * Create challenge card from json object
     *
     * @param json the json object that represents the card
     */
    public Challenge(JSONObject json) throws JSONException {
        super(json);
    }
}
