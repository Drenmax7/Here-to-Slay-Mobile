package com.example.jeudecarte.HereToSlay.card;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Abstract class that all class representing cards that can be played outside of ones turn inherit
 */
public abstract class Anytime extends Playable{
    //Constructors

    public Anytime(String name){
        super(name);
    }

    /**
     * Create anytime card from json object
     *
     * @param json the json object that represents the card
     */
    public Anytime(JSONObject json) throws JSONException {
        super(json);
    }
}
