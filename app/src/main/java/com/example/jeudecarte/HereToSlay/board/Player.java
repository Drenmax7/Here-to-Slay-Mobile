package com.example.jeudecarte.HereToSlay.board;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represent a player and contains various information on it
 */
public class Player {
    //Attributes

    /**
     * The player name
     * By default is an empty string and must be changed
     */
    public String name = "";

    /**
     * The leader that the player have been assigned to
     */
    public String leader = "";

    /**
     * The unique UUID of the player, used by the server to identify each player
     */
    public String uuid = "";


    //Constructor

    /**
     * Basic constructor.
     * Do not do anything special
     */
    public Player(){}

    /**
     * Create a Player based on the json object information
     *
     * @param json the json object from which information will be extracted
     */
    public Player(JSONObject json) throws JSONException {
        name = json.getString("name");
        leader = json.getString("leader");
        uuid = json.getString("uuid");
    }


    //Methods

    /**
     * Export all attributes of the player as a json object
     *
     * @return a json object that represents the player
     */
    public JSONObject convertJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("name",name);
        json.put("leader",leader);
        json.put("uuid",uuid);

        return json;
    }
}
