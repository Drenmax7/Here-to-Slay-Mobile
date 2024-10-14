package com.example.jeudecarte.HereToSlay.network;


import android.app.Activity;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import com.example.jeudecarte.HereToSlay.controller.Controller;
import com.example.jeudecarte.HereToSlay.view.Game;
import com.example.jeudecarte.HereToSlay.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Class that permit connexion to the server.
 * Its purpose is to receive information about the game to update the game state on screen
 * It also sends the user inputs to the server for him to proceed the player turn
 *
 * @see Server
 * @see Packet
 */
public class Client {
    //Attributes
    /**
     * The view onto which the data will be displayed
     */
    public View view = null;

    public Controller controller = null;

    public Boolean isControllerClient = false;

    /**
     * The state of the connexion
     */
    private boolean close;

    /**
     * The socket being use on the connexion
     */
    private Socket socket;

    /**
     * The stream on which information can be put to be read by the server
     */
    private ObjectOutputStream output;

    /**
     * The id to transmit with the packet to be identified as the right player
     */
    private String playerUUID;

    private String hostname;

    private int port;

    private static String TAG = "affichage debug CLIENT";

    public String getPlayerUUID() {
        return playerUUID;
    }


    //Constructor

    /**
     * Create a new client object
     *
     */
    public Client(String hostname, int port){
        close = false;
        this.hostname = hostname;
        this.port = port;

//        this.serviceName = serviceName;
//
//        nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
//
//        initializeResolveListener();
//        initializeDiscoveryListener();
    }

    //Methods

    /**
     * Create a new connexion with the server
     *
     * return in the argument if the connexion has been successful
     */
    public void connexion(){
        try {
            //timeout the connexion after 3s
            Log.d(TAG,hostname + " " + port);
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(hostname, port), 3000);

            this.socket = socket;
            this.output = new ObjectOutputStream(socket.getOutputStream());

            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            new Thread(() -> listen(input)).start();

//            etat.add(true);
            return;
        }
        catch (SocketTimeoutException exception){
            Log.d(TAG,"Connexion expired. Invalid address or the server is not reachable");
        }
        catch (UnknownHostException exception) {
            Log.d(TAG,"Server not found: " + exception.getMessage());
        }
        catch (IOException exception) {
            Log.d(TAG,"I/O error: " + exception.getMessage());
        }
//        etat.add(false);
    }

    /**
     * Close the connexion with the server
     */
    public void closeConnexion(){
        try {
            //sendData(new Packet("closing"));
            close = true;
            socket.close();
        } catch (IOException exception) {
            System.err.println("Error while closing the connexion: " + exception.getMessage());
        }
    }

    /**
     * Listen to the input stream and fetch data that are sent on it
     * Then process the data into the view
     * Should be run in a thread
     *
     * @param input the input stream to be listened
     */
    public void listen(ObjectInputStream input){
        while (!close){
            try {
                String receivedObject = (String) input.readObject();
                JSONObject json = new JSONObject(receivedObject);

                if (json.getString("name").equals("closing")) {
                    Log.d(TAG,"Closing connexion on server demand");
                    socket.close();
                    close = true;
                }
                else if (json.getString("name").equals("uuid")){

                    playerUUID = json.getString("value");

                    if (!isControllerClient){
                        //eventually get a view when an other thread change the view
                        while (view == null) {continue;}
                        view.dataTreatment(json);
                    }
                    else {
                        while (controller == null) {continue;}
                        controller.dataTreatment(json);
                    }
                }
                else{
                    if (!isControllerClient){
                        while (view == null) {continue;}
                        view.dataTreatment(json);
                    }
                    else {
                        while (controller == null) {continue;}
                        controller.dataTreatment(json);
                    }
                }
            }
            catch (IOException exception) {
                if (exception.getMessage().equals("Connection reset")) close = true;
                throw new RuntimeException(exception);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }catch (ClassNotFoundException exception){
                if (!close) Log.d(TAG,"Error while receiving data: " + exception.getMessage());
            }
        }
    }

    /**
     * Send the packet onto the output stream for the server to receive it
     *
     * @param json the data to send
     */
    public void sendData(JSONObject json){
        if (output == null) {
            Log.d(TAG,"Cannot send data without properly connected to server first");
            return;
        }

        if (close){
            Log.d(TAG,"Cannot send data if connexion is closed");
            return;
        }

        try {
            json.put("uuid",playerUUID);
            output.writeObject(json.toString());
            output.flush();
        } catch (IOException exception) {
            Log.d(TAG,"I/O error while sending data: " + exception.getMessage());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Send the packet onto the output stream for the server to receive it
     *
     * @param json the data to send
     */
    public void sendData(JSONObject json, String uuid){
        if (output == null) {
            Log.d(TAG,"Cannot send data without properly connected to server first");
            return;
        }

        if (close){
            Log.d(TAG,"Cannot send data if connexion is closed");
            return;
        }

        try {
            json.put("uuid",uuid);
            output.writeObject(json.toString());
            output.flush();
        } catch (IOException exception) {
            Log.d(TAG,"I/O error while sending data: " + exception.getMessage());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Return the connexion state
     *
     * @return the connexion state
     */
    public boolean isConnected() {return !close;}
}