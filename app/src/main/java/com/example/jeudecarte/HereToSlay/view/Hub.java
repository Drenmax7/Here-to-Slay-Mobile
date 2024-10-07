package com.example.jeudecarte.HereToSlay.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.jeudecarte.HereToSlay.board.Player;
import com.example.jeudecarte.HereToSlay.network.Client;
import com.example.jeudecarte.HereToSlay.network.Packet;
import com.example.jeudecarte.databinding.HereToSlayHubBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class Hub extends Activity implements View{
    public static Client client;

    public ArrayList<Player> playersList;

    //todo name should be a setting
    public String playerName = "Drenmax";

    public String host;

    @SuppressLint("StaticFieldLeak")
    private HereToSlayHubBinding binding;

    @Override
    public void dataTreatment(Packet packet) {
        if (packet.name.equals("new player")){
            Log.d("affichage debug","packet new player");
            Player player = new Player();
            player.name = packet.playerName;
            playersList.add(player);
            runOnUiThread(this::updateScene);
        }
        else if (packet.name.equals("uuid")){
            Log.d("affichage debug","packet uuid");
            //todo make it so names are unique
            host = packet.host;
            Packet newPacket = new Packet("name");
            newPacket.playerName = playerName;
            client.sendData(newPacket);
        } else if (packet.name.equals("player list")) {
            Log.d("affichage debug","player list");
            playersList = packet.playerList;
            Packet newPacket = new Packet("player list received");
            newPacket.playerName = packet.playerName;
            client.sendData(newPacket);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = HereToSlayHubBinding.inflate(this.getLayoutInflater());
        setContentView(binding.getRoot());

        client.view = this;

        playersList = new ArrayList<>();
        while (playersList.isEmpty()) {}
        updateScene();
    }

    public void updateScene(){
        for (Player player : playersList){
            TextView textView = new TextView(binding.frontLayout.getContext());
            textView.setText(player.name + " " + host);
            textView.setTextSize(18);
            textView.setPadding(10, 10, 10, 10);
            binding.frontLayout.addView(textView);
        }
    }
}
