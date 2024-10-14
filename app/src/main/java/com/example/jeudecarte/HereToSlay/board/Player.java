package com.example.jeudecarte.HereToSlay.board;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.UUID;

public class Player {
    public String name = "";
    public Integer leader = 0;
    public String uuid = "";

    public Player(JSONObject json) throws JSONException {
        name = json.getString("name");
        leader = json.getInt("leader");
        uuid = json.getString("uuid");
    }

    public Player(){

    }

    public JSONObject convertJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("name",name);
        json.put("leader",leader);
        json.put("uuid",uuid);

        return json;
    }
}
