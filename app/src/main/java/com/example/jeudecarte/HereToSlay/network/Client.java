package com.example.jeudecarte.HereToSlay.network;

import android.app.Activity;
import android.util.Log;

import com.example.jeudecarte.HereToSlay.view.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    /**
     * The state of the connexion
     */
    private boolean close;

    /**
     * Ip address of the server
     */
    private final String hostname;

    /**
     * Server's port
     */
    private final int port;

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
    private UUID playerUUID;

    //Constructor

    /**
     * Create a new client object
     *
     * @param hostname ip address of the server
     * @param port server's port
     */
    public Client(String hostname, int port){
        close = false;
        this.hostname = hostname;
        this.port = port;
    }

    //Methods

    /**
     * Create a new connexion with the server
     *
     * return in the argument if the connexion has been successful
     */
    public void connexion(ArrayList<Boolean> etat){
        try {
            //timeout the connexion after 3s
            Log.d("affichage debug",hostname);
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(hostname, port), 3000);

            this.socket = socket;
            this.output = new ObjectOutputStream(socket.getOutputStream());

            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            new Thread(() -> listen(input)).start();

            etat.add(true);
            return;
        }
        catch (SocketTimeoutException exception){
            Log.d("affichage debug","Connexion expired. Invalid address or the server is not reachable");
        }
        catch (UnknownHostException exception) {
            Log.d("affichage debug","Server not found: " + exception.getMessage());
        }
        catch (IOException exception) {
            Log.d("affichage debug","I/O error: " + exception.getMessage());
        }
        etat.add(false);
    }

    /**
     * Close the connexion with the server
     */
    public void closeConnexion(){
        try {
            sendData(new Packet("closing"));
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
                Packet packet = (Packet) input.readObject();

                if (packet.name.equals("closing")) {
                    System.out.println("Closing connexion on server demand");
                    socket.close();
                    close = true;
                }
                else if (packet.name.equals("uuid")){
                    playerUUID = packet.playerUUID;
                    //eventually get a view when an other thread change the view
                    while (view == null){}
                    view.dataTreatment(packet);
                }
                else{
                    view.dataTreatment(packet);
                }
            }
            catch (ClassNotFoundException | IOException exception) {
                if (exception.getMessage().equals("Connection reset")) close = true;
                else if (!close) System.err.println("Error while receiving data: " + exception.getMessage());
            }
        }
    }

    /**
     * Send the packet onto the output stream for the server to receive it
     *
     * @param packet the data to send
     */
    public void sendData(Packet packet){
        if (output == null) {
            System.out.println("Cannot send data without properly connected to server first");
            return;
        }

        if (close){
            System.out.println("Cannot send data if connexion is closed");
            return;
        }

        try {
            packet.playerUUID = playerUUID;
            output.writeObject(packet);
            output.flush();
        } catch (IOException exception) {
            System.out.println("I/O error while sending data: " + exception.getMessage());
        }
    }

    /**
     * Return the connexion state
     *
     * @return the connexion state
     */
    public boolean isConnected() {return !close;}
}