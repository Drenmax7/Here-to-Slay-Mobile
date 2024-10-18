package com.example.jeudecarte.HereToSlay.card;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Abstract class that all class that represents a card that can be played exclusively during ones turn inherit
 */
public abstract class Turn extends Playable{
    //Constructors

    public Turn(String name){
        super(name);
    }

    /**
     * Create turn card from json object
     *
     * @param json the json object that represents the card
     */
    public Turn(JSONObject json) throws JSONException {
        super(json);
    }
}
