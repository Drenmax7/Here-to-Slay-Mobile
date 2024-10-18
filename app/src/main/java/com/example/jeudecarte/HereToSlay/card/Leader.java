package com.example.jeudecarte.HereToSlay.card;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class that represents the leader cards
 */
public class Leader extends Card{
    //Constructors

    public Leader(String name){
        super(name);
    }

    /**
     * Create leader card from json object
     *
     * @param json the json object that represents the card
     */
    public Leader(JSONObject json) throws JSONException {
        super(json);
    }
}
