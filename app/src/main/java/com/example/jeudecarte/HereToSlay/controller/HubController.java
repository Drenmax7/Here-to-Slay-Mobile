package com.example.jeudecarte.HereToSlay.controller;

import android.util.Log;

import com.example.jeudecarte.HereToSlay.board.Player;
import com.example.jeudecarte.HereToSlay.network.Packet;
import com.example.jeudecarte.HereToSlay.network.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class HubController implements Controller{
    private Server server;

    private ArrayList<Player> playersList;

    public HubController(Server server){
        this.server = server;
        playersList = new ArrayList<>();
    }
    @Override
    public void dataTreatment(Packet packet) {
        if (packet.name.equals("name")) {
            Log.d("affichage debug","packet name");
            //todo packet child that take only the information they need
            //todo replace string names by constant
            //send to the new player the list of player that have logged in before him
            Packet newPacket = new Packet("player list");
            newPacket.playerList = playersList;
            Log.d("affichage debug","nom du joueur " + packet.playerName);
            newPacket.playerName = packet.playerName;
            server.sendDataByUUID(newPacket, packet.playerUUID);
        }
        else if (packet.name.equals("player list received")){
            Log.d("affichage debug","packet list received");
            //add the new player to the player list
            Player player = new Player();
            player.name = packet.playerName;
            player.uuid = packet.playerUUID;
            playersList.add(player);

            //inform everyone of the newcomer
            Packet newPacket = new Packet("new player");
            newPacket.playerName = packet.playerName;
            server.sendDataAll(newPacket);
        }
    }
}
