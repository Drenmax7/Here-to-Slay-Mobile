package com.example.jeudecarte.HereToSlay.view;

import static com.example.jeudecarte.HereToSlay.Utility.generateJson;
import static com.example.jeudecarte.HereToSlay.view.HereToSlay.GENERIC;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.jeudecarte.HereToSlay.Settings;
import com.example.jeudecarte.HereToSlay.board.Player;
import com.example.jeudecarte.HereToSlay.controller.HubController;
import com.example.jeudecarte.HereToSlay.network.Client;
import com.example.jeudecarte.databinding.HereToSlayHubBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * The view on which basic information on player waiting for the game will be displayed
 * Its the activity that start after clicking on the join button or the save button of Parameter
 */
public class Hub extends Activity implements View{
    //Attributes

    /**
     * The tag showed in the logcat console
     */
    private static final String TAG = GENERIC + "HUB";

    /**
     * The variable that list all id of the xml file
     */
    private HereToSlayHubBinding binding;


    /**
     * Indicate if the Hub should start a host client as well as a normal one
     */
    public static boolean isHost = false;

    /**
     * The client that will send and receive data to and from the server
     */
    public static Client client;

    /**
     * The host client that will manage the HubController and send information to all players
     */
    public static Client controllerClient;

    /**
     * Ip address of the server to connect
     */
    public String host;

    /**
     * Port of the server to connect
     */
    public int port;


    //todo delete player that have been disconnected
    /**
     * The list of all players currently waiting in the hub
     */
    private ArrayList<Player> playersList;

    /**
     * The pseudo of the player, useful to know which player he is
     */
    private String playerName = Settings.name;


    //Methods

    /**
     * Function launched when activity is started
     * Eventually create the host client and connect a client to the server if is the host
     * Set the view of the client to himself
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = HereToSlayHubBinding.inflate(this.getLayoutInflater());
        setContentView(binding.getRoot());

        if (isHost){
            startServer();
        }

        playersList = new ArrayList<>();
        client.view = this;
    }

    /**
     * Handle packets, may send back an other packet
     * The data received are used to update the view
     *
     * @param json the json object that act as a packet that the client received from the server
     */
    @Override
    public void dataTreatment(JSONObject json) {
        try {
            switch (json.getString("name")){
                case "uuid":
                    packetUUID();
                    break;
                case "attributed name":
                    packetAttributedName(json);
                    break;
                case "player list":
                    packetPlayerList(json);
                    break;
                case "new player":
                    packetNewPlayer(json);
                    break;
                default:
                    Log.d(TAG,"unknown packet name : " + json.getString("name"));
            }
        } catch (JSONException e) {
            //should not occurs as json name and value are always there
            throw new RuntimeException(e);
        }
    }

    /**
     * Generate a json object to submit a pseudo to the server
     */
    private void packetUUID() throws JSONException {
        Log.d(TAG, "packet uuid");

        //playerUUID = json.getString("value");
        //send to server the name the player wants
        JSONObject newJson = generateJson("requested name", playerName, "server");
        client.sendData(newJson);
    }

    /**
     * Change player name to the one specified in the packet
     * Generate a new packet to acknowledge the reception of the name
     *
     * @param json the json object that act as a packet that the client received from the server
     */
    private void packetAttributedName(JSONObject json) throws JSONException {
        Log.d(TAG, "attributed name");

        //acknowledge the name given by the server
        playerName = json.getString("value");
        JSONObject newJson = generateJson("name received", "", "server");
        client.sendData(newJson);
    }

    /**
     * Extract the json array from the packet and fill up the player list with those data
     * Generate a new packet to acknowledge the reception of the list
     *
     * @param json the json object that act as a packet that the client received from the server
     */
    private void packetPlayerList(JSONObject json) throws JSONException {
        //receive the players list
        Log.d(TAG, "player list");
        JSONArray playerListArray = json.getJSONArray("value");
        for (int i = 0; i < playerListArray.length(); i++){
            JSONObject jsonPlayer = playerListArray.getJSONObject(i);
            playersList.add(new Player(jsonPlayer));
        }

        JSONObject newJson = generateJson("player list received", playerName, "server");
        client.sendData(newJson);
    }

    /**
     * Extract the json object from the packet and create a new player from it
     * Add the player to the player list and update the view
     *
     * @param json the json object that act as a packet that the client received from the server
     */
    private void packetNewPlayer(JSONObject json) throws JSONException {
        Log.d(TAG, "packet new player" + json);
        Player player = new Player(json.getJSONObject("value"));
        playersList.add(player);
        runOnUiThread(this::updateScene);
    }

    /**
     * Update the view such as it accurately represents current information
     */
    private void updateScene(){
        int y = 0;
        for (Player player : playersList){
            TextView textView = new TextView(binding.frontLayout.getContext());
            textView.setText(player.name);
            textView.setTextSize(18);
            textView.setPadding(10, 10, 10, 10);
            textView.setY(y);
            textView.setBackgroundColor(0xFFFFFFFF);
            y += 100;
            binding.frontLayout.addView(textView);
        }
    }

    /**
     * Create the host client and connect a client to the server
     */
    private void startServer(){
        //create the host client
        Hub.controllerClient = new Client("10.0.2.2",6666);
        Hub.controllerClient.isControllerClient = true;

        new Thread(Hub.controllerClient::connexion).start();
        Hub.controllerClient.controller = new HubController(Hub.controllerClient);

        //wait until connexion between controller client and server has been made
        while (Hub.controllerClient.getPlayerUUID() == null) {continue;}

        //also create the client as it has not been by the previous activity
        Hub.client = new Client("10.0.2.2",6666);
        new Thread(Hub.client::connexion).start();
    }
}
