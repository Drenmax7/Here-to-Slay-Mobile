package com.example.jeudecarte.HereToSlay.network;


import static com.example.jeudecarte.HereToSlay.view.HereToSlay.GENERIC;

import android.util.Log;

import com.example.jeudecarte.HereToSlay.controller.Controller;
import com.example.jeudecarte.HereToSlay.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Class that permit connexion to the server.
 * Its purpose is to receive information about the game to update the game state on screen
 * It also sends the user inputs to the server for him to proceed the player turn
 */
public class Client {
    //Attributes
    /**
     * The tag showed in the logcat console
     */
    private static final String TAG = GENERIC + "CLIENT";


    /**
     * The view onto which the data will be displayed
     */
    public volatile View view = null;

    /**
     * The controller into which the data will be handled if it is the host client
     */
    public volatile Controller controller = null;

    /**
     * If it is the host client
     */
    public Boolean isControllerClient = false;

    /**
     * The id to transmit with the packet to be identified as the right player by the server
     */
    private String playerUUID;

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
     * Ip address of the server
     */
    private final String hostname;

    /**
     * Port of the server
     */
    private final int port;


    //Constructor

    /**
     * Create a new client object
     *
     */
    public Client(String hostname, int port){
        close = false;
        this.hostname = hostname;
        this.port = port;
    }

    //Getters

    /**
     * playerUUID getter
     *
     * @return playerUUID's value
     */
    public String getPlayerUUID() {
        return playerUUID;
    }


    //Methods

    /**
     * Create a new connexion with the server
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
    private void listen(ObjectInputStream input){
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
                close = true;
                throw new RuntimeException(exception);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }catch (ClassNotFoundException exception){
                if (!close) Log.d(TAG,"Error while receiving data: " + exception.getMessage());
            }
        }
    }

    /**
     * Send the packet into the output stream for the server to receive it
     * The packet will contain the value of playerUUID
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
     * Send the packet into the output stream for the server to receive it
     * The packet will contain the value of the specified uuid
     *
     * @param json the data to send
     * @param uuid the uuid of the targeted player
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