package com.example.jeudecarte.HereToSlay.view;

import org.json.JSONObject;

/**
 * Parent of all views that the client can interact with
 * Useful to put a view that can be changed for an other one inside of a client
 *
 * @see Hub
 * @see Game
 */
public interface View {
    /**
     * The function that will handle the packets received by the client
     *
     * @param json the json object that act as a packet that the client received from the server
     */
    void dataTreatment(JSONObject json);
}
