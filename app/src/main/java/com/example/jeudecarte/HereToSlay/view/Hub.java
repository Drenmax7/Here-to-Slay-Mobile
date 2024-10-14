package com.example.jeudecarte.HereToSlay.view;

import static com.example.jeudecarte.HereToSlay.Utility.generateJson;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.jeudecarte.HereToSlay.Settings;
import com.example.jeudecarte.HereToSlay.board.Player;
import com.example.jeudecarte.HereToSlay.controller.HubController;
import com.example.jeudecarte.HereToSlay.network.Client;
import com.example.jeudecarte.HereToSlay.network.NsdServiceDiffuser;
import com.example.jeudecarte.databinding.HereToSlayHubBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Hub extends Activity implements View{
    public static boolean isHost = false;

    public static Client client;

    public static Client controllerClient;

    public ArrayList<Player> playersList;

    public String playerName = Settings.name;

    public String playerUUID;

    public String host;

    public int port;

    private NsdServiceDiffuser nsdService;

    public static String TAG = "affichage debug HUB";

    @SuppressLint("StaticFieldLeak")
    private HereToSlayHubBinding binding;

    @Override
    public void dataTreatment(JSONObject json) {
        try {
            if (json.getString("name").equals("uuid")) {
                Log.d(TAG, "packet uuid");

                //playerUUID = json.getString("value");
                //send to server the name the player wants
                JSONObject newJson = generateJson("requested name", playerName, "server");
                client.sendData(newJson);

            }
            else if (json.getString("name").equals("attributed name")){
                Log.d(TAG, "attributed name");

                //acknowledge the name given by the server
                playerName = json.getString("value");
                JSONObject newJson = generateJson("name received", "", "server");
                client.sendData(newJson);

            }
            else if (json.getString("name").equals("player list")) {
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
            else if (json.getString("name").equals("new player")) {
                Log.d(TAG, "packet new player" + json);
                Player player = new Player(json.getJSONObject("value"));
                playersList.add(player);
                runOnUiThread(this::updateScene);
            }

            else {
                Log.d(TAG,"unknown packet name : " + json.getString("name"));
            }

        } catch (JSONException e) {
            //should not occurs as json name and value are always there
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = HereToSlayHubBinding.inflate(this.getLayoutInflater());
        setContentView(binding.getRoot());

        if (isHost){
            startServer();
            //activateDiscovery();
        }

        client.view = this;

        playersList = new ArrayList<>();
    }

    @Override
    protected void onPause() {
        Log.d(TAG,"pause");
        if (nsdService != null) {
            nsdService.tearDown();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(TAG,"resume");
        super.onResume();
        if (nsdService != null) {
            nsdService.registerService(this);
        }
    }

    @Override
    protected void onDestroy() {
        nsdService.tearDown();
        super.onDestroy();
    }

    public void updateScene(){
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

    public void startServer(){
        /*// create the server
        Server server = new Server();
        //new Thread(server::run).start();

        server.controller = new HubController(server);

        //connect to server
        port = 0;
        //use of while in case the thread is slow to assign port
        while (port == 0) {port = server.getPort();};*/
        Hub.controllerClient = new Client("10.0.2.2",6666);
        Hub.controllerClient.isControllerClient = true;

        new Thread(Hub.controllerClient::connexion).start();
        Hub.controllerClient.controller = new HubController(Hub.controllerClient);

        //wait until connexion between controller client and server has been made
        while (Hub.controllerClient.getPlayerUUID() == null) {continue;}

        Hub.client = new Client("10.0.2.2",6666);
        new Thread(Hub.client::connexion).start();
    }

    /*public void activateDiscovery(){
        nsdService = new NsdServiceDiffuser(port);
//        nsdService.registerService(this);
    }*/
}
