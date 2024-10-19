package com.example.jeudecarte.HereToSlay.view;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.example.jeudecarte.HereToSlay.InfoDeck.getDrawableByName;
import static com.example.jeudecarte.HereToSlay.Utility.generateJson;
import static com.example.jeudecarte.HereToSlay.Utility.setLocale;
import static com.example.jeudecarte.HereToSlay.view.HereToSlay.GENERIC;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.example.jeudecarte.HereToSlay.ZoomableImageView;
import com.example.jeudecarte.HereToSlay.Settings;
import com.example.jeudecarte.HereToSlay.board.Player;
import com.example.jeudecarte.HereToSlay.card.Leader;
import com.example.jeudecarte.HereToSlay.controller.HubController;
import com.example.jeudecarte.HereToSlay.network.Client;
import com.example.jeudecarte.R;
import com.example.jeudecarte.databinding.HereToSlayHubBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/**
 * The view on which basic information on player waiting for the game will be displayed
 * Its the activity that start after clicking on the join button or the save button of Parameter
 */
public class Hub extends Activity implements View{
    //Attributes

    /**
     * How many player in a single page
     */
    private final int ROW_LENGTH = 3;

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

    /**
     * The page the player is currently looking at
     */
    private int currentPage = 0;


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
        setLocale(this, Settings.language);

        super.onCreate(savedInstanceState);

        binding = HereToSlayHubBinding.inflate(this.getLayoutInflater());
        setContentView(binding.getRoot());

        if (isHost){
            startServer();
        }

        setArrows();
        setLayoutWidth();
        setZoom();
        setReroll();

        playersList = new ArrayList<>();
        client.view = this;
    }

    /**
     * Set the width of the layout so the images are shown correctly
     */
    private void setLayoutWidth(){
        int width = getWindowManager().getDefaultDisplay().getWidth();
        Log.d(TAG, "width : " + width);
        //2400*1017
        //2560*1536
        ViewGroup.LayoutParams layoutParams = binding.slot1.getLayoutParams();
        layoutParams.width = width/4;
        binding.slot1.setLayoutParams(layoutParams);
        binding.slot2.setLayoutParams(layoutParams);
        binding.slot3.setLayoutParams(layoutParams);
    }

    /**
     * Initialize the behavior of the left and right buttons
     */
    private void setArrows(){
        binding.arrowLeft.setOnClickListener(v -> {
            //if its not the first page, enable right button and change page
            if (currentPage > 0) {
                currentPage--;
                binding.arrowRight.setVisibility(VISIBLE);
                binding.arrowRightLayout.setBackgroundColor(0xFFFFFFFF);

                //if its first page disable button
                if (currentPage == 0) {
                    binding.arrowLeft.setVisibility(INVISIBLE);
                    binding.arrowLeftLayout.setBackgroundColor(0x00FFFFFF);
                }

                updateScene();
            }
        });

        binding.arrowRight.setOnClickListener(v -> {
            int lastPage = (playersList.size()-1)/ROW_LENGTH;

            //if its not the last page, enable left button and change page
            if (currentPage < lastPage) {
                currentPage++;
                binding.arrowLeft.setVisibility(VISIBLE);
                binding.arrowLeftLayout.setBackgroundColor(0xFFFFFFFF);

                //if its last page disable button
                if (currentPage == lastPage) {
                    binding.arrowRight.setVisibility(INVISIBLE);
                    binding.arrowRightLayout.setBackgroundColor(0x00FFFFFF);
                }

                updateScene();
            }
        });
    }

    /**
     * Initialize the zoom functionality
     */
    private void setZoom(){
        binding.zoom.setVisibility(INVISIBLE);

        binding.heroImage1.setZoomText(binding.zoom);
        binding.heroImage2.setZoomText(binding.zoom);
        binding.heroImage3.setZoomText(binding.zoom);
    }

    /**
     * Initialize the behavior of the reroll buttons
     */
    private void setReroll(){
        //the controller must check the player still have reroll use
        binding.rerollButton1.setOnClickListener(generateRerollFunction(0));
        binding.rerollButton2.setOnClickListener(generateRerollFunction(1));
        binding.rerollButton3.setOnClickListener(generateRerollFunction(2));
    }

    /**
     * Generate an OnClickListener for the reroll buttons
     *
     * @param position The position of the reroll button
     */
    private OnClickListener generateRerollFunction(int position){
        return v -> {
            Log.d(TAG, "reroll button" + position);
            Player player = getPlayerByName(playerName);
            if (player == null) {
                throw new RuntimeException(new Exception("player name : " + playerName + " is not valid"));
            }

            if (player.rerollLeft > 0){
                player.rerollLeft--;

                try {
                    Player playerTarget = playersList.get(currentPage*ROW_LENGTH + position);
                    JSONObject value = new JSONObject();
                    value.put("from",playerName);
                    value.put("to",playerTarget.name);
                    JSONObject packet = generateJson("reroll leader", value, "server");
                    new Thread(() -> client.sendData(packet)).start();

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    /**
     * Return the player whose name is specified
     *
     * @param name The name of the player wanted
     *
     * @return The player whose name is specified
     */
    private Player getPlayerByName(String name){
        for (Player player : playersList){
            if (player.name.equals(name)) return player;
        }
        return null;
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
            //if the packet is not for the player, ignore it
            if (!json.getString("target").equals("player") &&
               (!json.getString("target").equals("all"))) {
                return;
            }

            switch (json.getString("name")){
                case "uuid":
                    packetUUID();
                    break;
                case "attributed name":
                    packetAttributedName(json);
                    break;
                case "settings":
                    packetSettings(json);
                    break;
                case "player list":
                    packetPlayerList(json);
                    break;
                case "new player":
                    packetNewPlayer(json);
                    break;
                case "new leader":
                    changeLeader(json);
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
     * Extract the json object from the packet and fill up the setting class with those data
     * Generate a new packet to acknowledge the reception of the setting list
     *
     * @param json the json object that act as a packet that the client received from the server
     */
    private void packetSettings(JSONObject json) throws JSONException {
        Log.d(TAG, "settings");

        Settings.importParameter(json.getJSONObject("value"));

        JSONObject newJson = generateJson("settings received", "", "server");
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
        Log.d(TAG, "packet new player");
        Player player = new Player(json.getJSONObject("value"));
        playersList.add(player);
        runOnUiThread(this::updateScene);
    }

    /**
     * Extract the json object from the packet and change the leader card of the specified player
     *
     * @param json the json object that act as a packet that the client received from the server
     */
    private void changeLeader(JSONObject json) throws JSONException{
        Log.d(TAG, "packet new leader");

        JSONObject value = json.getJSONObject("value");
        String name = value.getString("name");

        Player player = getPlayerByName(name);
        if (player != null) player.leader = new Leader(value.getJSONObject("card"));

        String from = value.getString("from");

        if (!from.equals("server")) {
            String text = from + getResources().getString(R.string.leader_change_message) + name;
            runOnUiThread(() -> Toast.makeText(this, text, Toast.LENGTH_SHORT).show());
        }

        runOnUiThread(this::updateScene);
    }

    /**
     * Update the view such as it accurately represents current information
     */
    private void updateScene(){
        Player playerUser = getPlayerByName(playerName);
        if (playerUser == null) {
            throw new RuntimeException(new Exception("player name : " + playerName + " is not valid"));
        }
        //todo split all that in multiple function
        //update player count
        int size = playersList.size();
        String text = String.format(Locale.FRANCE,"%d/%d",size,Settings.playerNumber);
        binding.playerCount.setText(text);

        for (int i = 0; i < ROW_LENGTH; i++){
            //get player text field
            int resID = getResources().getIdentifier("player_name_"+(i+1), "id", getPackageName());
            TextView pseudoTextView = findViewById(resID);

            //get image view
            resID = getResources().getIdentifier("hero_image_"+(i+1), "id", getPackageName());
            ZoomableImageView leaderImageView = findViewById(resID);

            //get reroll button
            resID = getResources().getIdentifier("reroll_button_"+(i+1), "id", getPackageName());
            Button rerollButton = findViewById(resID);

            //if the slot is occupied or not
            if (currentPage * ROW_LENGTH + i < size) {
                Player player = playersList.get(currentPage * ROW_LENGTH + i);

                //update player name
                pseudoTextView.setVisibility(VISIBLE);
                String pseudo = player.name;
                pseudoTextView.setText(pseudo);

                if (pseudo.equals(playerName)){
                    pseudoTextView.setTextColor(0xFFFF0000);
                }
                else {
                    pseudoTextView.setTextColor(0xFF000000);
                }

                //update leader image
                leaderImageView.setVisibility(VISIBLE);
                Leader leaderCard = player.leader;
                leaderImageView.setImageDrawable(getDrawableByName(leaderCard.getName()));
                leaderImageView.setDescription("description of the " + leaderCard.getName() + " card");

                //update reroll button
                rerollButton.setVisibility(VISIBLE);
                if (playerUser.rerollLeft == 0){
                    rerollButton.setEnabled(false);
                }
            }
            else {
                pseudoTextView.setVisibility(INVISIBLE);
                leaderImageView.setVisibility(INVISIBLE);
                rerollButton.setVisibility(INVISIBLE);
            }
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
        Hub.client = new Client("10.0.2.2", 6666);
        new Thread(Hub.client::connexion).start();

    }
}
