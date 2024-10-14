package com.example.jeudecarte.HereToSlay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utilitary {
    public static JSONObject generateJson(String name, String value, String target) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("name",name);
        json.put("value",value);
        json.put("target",target);

        return json;
    }

    public static JSONObject generateJson(String name, JSONArray value, String target) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("name",name);
        json.put("value",value);
        json.put("target",target);

        return json;
    }

    public static JSONObject generateJson(String name, JSONObject value, String target) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("name",name);
        json.put("value",value);
        json.put("target",target);

        return json;
    }
}
