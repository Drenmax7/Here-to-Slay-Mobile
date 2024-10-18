package com.example.jeudecarte.HereToSlay.controller;

import static com.example.jeudecarte.HereToSlay.InfoDeck.getCategoryList;
import static com.example.jeudecarte.HereToSlay.Utility.generateJson;
import static com.example.jeudecarte.HereToSlay.InfoDeck.getRandomName;
import static com.example.jeudecarte.HereToSlay.view.HereToSlay.GENERIC;

import android.util.Log;

import com.example.jeudecarte.HereToSlay.Settings;
import com.example.jeudecarte.HereToSlay.board.Player;
import com.example.jeudecarte.HereToSlay.card.Leader;
import com.example.jeudecarte.HereToSlay.network.Client;
import com.example.jeudecarte.HereToSlay.stack.LeaderStack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * The controller that handle information about the player that has connect to the hub
 */
public class HubController implements Controller{
    //Attributes

    /**
     * The tag showed in the logcat console
     */
    private static final String TAG = GENERIC + "HUB_CONTROLLER";

    /**
     * The host client that receive and send data from and to the server
     */
    private final Client server;

    /**
     * The list of the player connected to the hub
     */
    private final ArrayList<Player> playersList;

    private final LeaderStack leaderDeck;


    //Constructors

    /**
     * Create a controller with the specified host client
     *
     * @param server The host client
     */
    public HubController(Client server){
        this.server = server;
        playersList = new ArrayList<>();

        for (int i = 0; i < 10; i++){
            Player player = new Player();
            player.name = getValidName("");
            playersList.add(player);
        }

        ArrayList<String> leaderList = getCategoryList(new String[]{"leader"});
        leaderDeck = new LeaderStack(leaderList);
    }


    //Methods

    /**
     * Handle packets, may send back an other packet
     * The data received are used to update the controller
     *
     * @param json the json object that act as a packet that the host client received from the server
     */
    @Override
    public void dataTreatment(JSONObject json) {

        try {
            //if the packet is not for the server, ignore it
            if (!json.getString("target").equals("server")) return;

            switch (json.getString("name")) {
                case("requested name"):
                    packetRequestedName(json);
                    break;
                case("name received"):
                    packetNameReceived(json);
                    break;
                case("player list received"):
                    packetPlayerListReceived(json);
                    break;
                case("settings received"):
                    packetSettingsReceived(json);
                    break;

                default:
                    Log.d(TAG,"unknown packet name : " + json.getString("name"));
            }
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Check if the name inside the packet is already assigned to a player and change it
     * if it is the case.
     * Then send the name via a new packet
     *
     * @param json the json object that act as a packet that the client received from the server
     */
    private void packetRequestedName(JSONObject json) throws JSONException {
        //check if the name requested is available and change it if not
        Log.d(TAG, "packet name");
        Log.d(TAG, "player name " + json.getString("value"));
        //todo replace string names by constant

        String attributedName = json.getString("value");
        //check if the name is available, and change it if not
        attributedName = getValidName(attributedName);

        JSONObject newJson = generateJson("attributed name", attributedName, "player");
        server.sendData(newJson, json.getString("uuid"));
    }

    /**
     * Generate a packet containing the list of all parameters and send it
     *
     * @param json the json object that act as a packet that the client received from the server
     */
    private void packetNameReceived(JSONObject json) throws JSONException {
        Log.d(TAG, "name received");

        JSONObject parameters = Settings.exportParameter();

        JSONObject newJson = generateJson("settings",parameters,"player");
        server.sendData(newJson, json.getString("uuid"));
    }

    /**
     * Generate a packet containing the list of all players (the sender is not in it yet) and send it
     *
     * @param json the json object that act as a packet that the client received from the server
     */
    private void packetSettingsReceived(JSONObject json) throws JSONException {
        Log.d(TAG, "settings received");
        //send to the new player the list of player that have logged in before him

        JSONArray playerListArray = new JSONArray();
        for (Player player : playersList){
            playerListArray.put(player.convertJson());
        }

        JSONObject newJson = generateJson("player list",playerListArray,"player");
        server.sendData(newJson, json.getString("uuid"));
    }

    /**
     * Create a new player to represent the sender and add it to the list
     * Then send a packet to all players (including the sender) to notify them of the arrival of
     * a new player
     *
     * @param json the json object that act as a packet that the client received from the server
     */
    private void packetPlayerListReceived(JSONObject json) throws JSONException {
        Log.d(TAG, "player list received");

        //add the new player to the player list
        Player player = new Player();
        player.name = json.getString("value");
        player.uuid = json.getString("uuid");
        playersList.add(player);

        //inform everyone of the newcomer
        JSONObject newJson = generateJson("new player", player.convertJson(), "all");
        server.sendData(newJson);

        checkHubFull();
    }

    /**
     * Check if the hub is full and give everyone a leader if it is the case
     */
    private void checkHubFull(){
        Log.d(TAG, "check hub full");
        if (playersList.size() != Settings.playerNumber) return;

        //if the hub is full

        for (Player player : playersList){
            Leader leaderCard = leaderDeck.draw();
            player.leader = leaderCard;
            Log.d(TAG, leaderCard.getName());
            try {
                JSONObject json = new JSONObject();
                json.put("name", player.name);
                json.put("card", leaderCard.convertJson());

                JSONObject json2 = generateJson("new leader", json, "all");
                server.sendData(json2);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Check if the specified name is already used by a player and change it if it is the case
     *
     * @param name The name to check
     * @return a unique name
     */
    private String getValidName(String name){
        if (name.isEmpty()) name = getRandomName();

        boolean valid = true;
        for (Player player : playersList){
            if (player.name.equals(name)) {
                valid = false;
                break;
            }
        }

        if (valid) return name;
        else{
            name = getRandomName();
            return getValidName(name);
        }
    }
}
