package com.example.jeudecarte.HereToSlay.controller;

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
            //todo packet child that take only the information they need
            //todo replace string names by constant
            //send to the new player the list of player that have logged in before him
            Packet newPacket = new Packet("player list");
            packet.playerList = playersList;
            server.sendDataByUUID(newPacket,packet.playerUUID);

            //add the new player to the player list
            Player player = new Player();
            player.name = packet.playerName;
            player.uuid = packet.playerUUID;
            playersList.add(player);

            //inform everyone of the newcomer
            newPacket = new Packet("new player");
            newPacket.playerName = packet.playerName;
            server.sendDataAll(newPacket);

        }
    }
}
