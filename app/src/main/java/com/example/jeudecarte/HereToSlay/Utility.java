package com.example.jeudecarte.HereToSlay;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

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

    /**
     * Change local language
     *
     * @param context the activity to change the language of
     * @param index the index of the language.
     *              0 for english,
     *              1 for french
     */
    public static void setLocale(Context context, Integer index) {
        ArrayList<String> languageCode = new ArrayList<>(Arrays.asList(
                "en", "fr"
        ));

        String langCode = languageCode.get(index);

        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}
