package com.example.jeudecarte.HereToSlay.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
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

    @SuppressLint("StaticFieldLeak")
    private HereToSlayHubBinding binding;

    @Override
    public void dataTreatment(Packet packet) {
        if (packet.name.equals("new player")){
            Player player = new Player();
            player.name = packet.playerName;
            playersList.add(player);
            updateScene();
        }
        else if (packet.name.equals("uuid")){
            //todo make it so names are unique
            Packet newPacket = new Packet("name");
            newPacket.playerName = playerName;
            client.sendData(newPacket);
        } else if (packet.name.equals("player list")) {
            playersList = packet.playerList;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = HereToSlayHubBinding.inflate(this.getLayoutInflater());
        setContentView(binding.getRoot());

        client.view = this;
        client.connexion();
    }

    public void updateScene(){
        for (Player player : playersList){
            TextView textView = new TextView(this);
            textView.setText(player.name);
            binding.frontLayout.addView(textView);
        }
    }
}
