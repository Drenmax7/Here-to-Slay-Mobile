package com.example.jeudecarte.HereToSlay.card;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Abstract class that all class that represent a card that can be played by a player inherit
 */
public abstract class Playable extends Card{
    //Constructors

    public Playable(String name){
        super(name);
    }

    /**
     * Create playable card from json object
     *
     * @param json the json object that represents the card
     */
    public Playable(JSONObject json) throws JSONException {
        super(json);
    }

}
