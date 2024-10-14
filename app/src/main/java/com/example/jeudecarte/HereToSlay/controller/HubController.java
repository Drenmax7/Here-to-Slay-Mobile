package com.example.jeudecarte.HereToSlay.controller;

import static com.example.jeudecarte.HereToSlay.Utility.generateJson;

import android.util.Log;

import com.example.jeudecarte.HereToSlay.board.Player;
import com.example.jeudecarte.HereToSlay.network.Client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HubController implements Controller{
    private Client server;

    private ArrayList<Player> playersList;

    private static String TAG = "affichage debug HUB_CONTROLLER";

    public HubController(Client server){
        this.server = server;
        playersList = new ArrayList<>();
    }

    @Override
    public void dataTreatment(JSONObject json) {

        try {
            if (!json.getString("target").equals("server")) return;

            if (json.getString("name").equals("requested name")) {
                //check if the name requested is available and change it if not
                Log.d(TAG, "packet name");
                Log.d(TAG, "nom du joueur " + json.getString("value"));
                //todo replace string names by constant

                String attributedName = json.getString("value");
                //check if the name is available, and change it if not
                attributedName = getValidName(attributedName);

                JSONObject newJson = generateJson("attributed name", attributedName, "player");
                server.sendData(newJson, json.getString("uuid"));
            }
            else if (json.getString("name").equals("name received")) {
                //send to the new player the list of player that have logged in before him

                JSONArray playerListArray = new JSONArray();
                for (Player player : playersList){
                    playerListArray.put(player.convertJson());
                }

                JSONObject newJson = generateJson("player list",playerListArray,"player");
                server.sendData(newJson, json.getString("uuid"));

            } else if (json.getString("name").equals("player list received")) {
                Log.d(TAG, "player list received");

                //add the new player to the player list
                Player player = new Player();
                player.name = json.getString("value");
                player.uuid = json.getString("uuid");
                playersList.add(player);

                //inform everyone of the newcomer
                JSONObject newJson = generateJson("new player", player.convertJson(), "all");
                server.sendData(newJson);
            }

            else {
                Log.d(TAG,"unknown packet name : " + json.getString("name"));
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public String getValidName(String name){
        boolean valid = true;
        for (Player player : playersList){
            if (player.name.equals(name)) {
                valid = false;
                break;
            }
        }

        if (valid) return name;
        else{
            name += "_";
            return getValidName(name);
        }
    }
}
