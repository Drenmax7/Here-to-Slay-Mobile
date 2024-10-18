package com.example.jeudecarte.HereToSlay;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * The purpose of this class is to contain useful functions that accomplish simple tasks
 */
public class Utility {
    /**
     * Create a simple Json object whose values are the arguments
     * Design to be sent as packet
     *
     * @param name The name of the packet
     * @param value The information sent : might be a string or an other json
     * @param target The person the packet should be delivered to :
     *               'server', the controller
     *               'player', the player that sent the previous packet to the controller
     *               'all', every player (not the controller)
     *
     * @return The Json object acting as a packet
     */
    public static JSONObject generateJson(String name, Object value, String target) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("name",name);
        json.put("value",value);
        json.put("target",target);

        return json;
    }
}
