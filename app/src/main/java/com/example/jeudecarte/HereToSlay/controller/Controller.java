package com.example.jeudecarte.HereToSlay.controller;

import org.json.JSONObject;

/**
 * Parent of all controller that the host client can interact with
 * Useful to put a controller that can be changed for an other one inside of the host client
 *
 * @see HubController
 * @see GameController
 */
public interface Controller {
    /**
     * The function that will handle the packets received by the host client
     *
     * @param json the json object that act as a packet that the host client received from the server
     */
    void dataTreatment(JSONObject json);
}
