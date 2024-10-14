package com.example.jeudecarte.HereToSlay.controller;

import com.example.jeudecarte.HereToSlay.network.Client;
import com.example.jeudecarte.HereToSlay.network.Packet;
import com.example.jeudecarte.HereToSlay.network.Server;

import org.json.JSONObject;

public class GameController implements Controller{
    private Server server;

    public GameController(Server server){
        this.server = server;
    }

    @Override
    public void dataTreatment(JSONObject json) {

    }
}
