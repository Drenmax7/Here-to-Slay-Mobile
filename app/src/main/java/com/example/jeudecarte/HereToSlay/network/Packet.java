package com.example.jeudecarte.HereToSlay.network;

import com.example.jeudecarte.HereToSlay.board.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * The data structure that is sent between the server and clients
 *
 * @see Server
 * @see Client
 */
public class Packet implements Serializable {
    public String name;
    public String playerName;
    public UUID playerUUID;
    public ArrayList<Player> playerList;

    public Packet(String name){
        this.name = name;
    }
}
