package com.example.jeudecarte.HereToSlay.card;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class that represents the modifier cards
 */
public class Modifier extends Anytime {
    //Constructors

    public Modifier(String name){
        super(name);
    }

    /**
     * Create modifier card from json object
     *
     * @param json the json object that represents the card
     */
    public Modifier(JSONObject json) throws JSONException {
        super(json);
    }
}
