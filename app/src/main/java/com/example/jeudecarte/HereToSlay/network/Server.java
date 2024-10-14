package com.example.jeudecarte.HereToSlay.network;


import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import com.example.jeudecarte.HereToSlay.controller.Controller;
import com.example.jeudecarte.HereToSlay.controller.HubController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class that permit connexion with clients.
 * Its purpose is to send the game state to clients, so they can update their game view
 * It also gets the user inputs to make a game turn
 *
 * @see Client
 * @see Packet
 */
public class Server {
    //Attributes

    /**
     *  The controller that will receive the data from the server
     */
    public Controller controller;

    /**
     * Run until close is set to true
     */
    private boolean close;

    /**
     * The port that is listened by the server
     */
    private int port;

    /**
     * Socket on which incoming connexion are put and that permits the server to accept them
     */
    private ServerSocket serverSocket;

    /**
     * List of the client's socket currently connected
     */
    private final HashMap<UUID,Socket> connectedClient;

    /**
     * List of the client's output stream currently connected onto the server
     */
    private final HashMap<UUID,ObjectOutputStream> clientOutput;

    /**
     * Lock used to prevent multiple thread to delete sockets from the socket's list at the same time
     */
    private final ReentrantLock lock;

    //Getter

    public int getPort() {
        return port;
    }

    //Constructor

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }

    /**
     * Create a new server, listening on the given port
     *
     */
    public Server() {
        connectedClient = new HashMap<>();
        clientOutput = new HashMap<>();
        lock = new ReentrantLock();

//        initializeRegistrationListener();
//        registerService(context);
    }

    //Methods

    /**
     * Start the server, that consist of an infinite loop of accepting incoming connexion
     * Should be run in a thread
     */
    public void run() {

        // Initialize a server socket on the next available port.
        try(ServerSocket serverSocket = new ServerSocket(0)){
            Log.d("affichage debug","server is running on port " + port);
            // Store the chosen port.
            port = serverSocket.getLocalPort();
            Log.d("affichage debug","server is ready to run on port " + port);

            this.serverSocket = serverSocket;
            close = false;

            // As long as the server is open, it accepts connections
            while (!close) {
                try {

                    UUID playerUUID = UUID.randomUUID();

                    Socket socket = serverSocket.accept();
                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

                    Log.d("affichage debug", "New connection from " + socket.getInetAddress().getHostAddress());


                    lock.lock();
                    try {
                        connectedClient.put(playerUUID, socket);
                        clientOutput.put(playerUUID, output);
                    } finally {
                        lock.unlock();
                    }

                    JSONObject json = new JSONObject();
                    json.put("name","uuid");
                    json.put("value", playerUUID);
                    try (OutputStreamWriter out = new OutputStreamWriter(
                            output, StandardCharsets.UTF_8)) {
                        out.write(json.toString());
                    }
                    sendDataByUUID(json, playerUUID);

                    new Thread(() -> listen(socket, input, playerUUID)).start();

                } catch (IOException exception) {
                    if (!close)
                        Log.d("affichage debug", "Error while accepting connexion: " + exception.getMessage());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        catch (IOException exception) {
            throw new RuntimeException("Error while starting the server: " + exception.getMessage(), exception);
        }
    }

    /**
     * Stop the server and close all existing connexion
     */
    public void stop() {
        close = true;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            sendDataAll(new Packet("closing"));
            for (Socket socket : connectedClient.values()){
                socket.close();
            }
            Log.d("affichage debug","Server is stopped");
        } catch (IOException exception) {
            Log.d("affichage debug","Error while stopping the server: " + exception.getMessage());
        }
    }

    /**
     * Listen to the input stream and fetch data that are sent on it
     * Then process the data into the game controller
     *
     * @param input the input stream to be listened
     * @param socket the socket used for the connexion with the client that is sending data on the input
     */
    public void listen(Socket socket, ObjectInputStream input, UUID playerUUID) {
        while (!socket.isClosed()){
            try {
                Packet receivedObject = (Packet) input.readObject();
                //controller.dataTreatment(receivedObject);
            }
            catch (ClassNotFoundException exception) {
                Log.d("affichage debug","Error while receiving data: " + exception.getMessage());

            }
            catch (IOException exception){
                try {
                    Log.d("affichage debug","IO error, socket is connexion is closing" + exception.getMessage());
                    socket.close();
                }
                catch (IOException exception2){
                    Log.d("affichage debug","Error while closing client connexion" + exception2.getMessage());
                }
            }
        }

        lock.lock();
        try {
            connectedClient.remove(playerUUID);
            clientOutput.remove(playerUUID);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Send the packet to every client currently connected
     *
     * @param packet the data to send
     */
    public void sendDataAll(Packet packet){
        //clientOutput.removeIf(Objects::isNull);
        for (ObjectOutputStream output : clientOutput.values()){
            try {
                output.writeObject(packet);
                output.flush();
            } catch (IOException exception) {
                Log.d("affichage debug","I/O error while sending data: " + exception.getMessage());
            }
        }
    }

    /**
     * Send the packet to the client having the specified uuid
     *
     * @param json the data to send
     * @param playerUUID the id of the player to which the date must be send
     */
    public void sendDataByUUID(JSONObject json, UUID playerUUID){
        ObjectOutputStream output = clientOutput.get(playerUUID);
        try {
            output.writeObject(json);
            output.flush();
        } catch (IOException exception) {
            Log.d("affichage debug","I/O error while sending data: " + exception.getMessage());
        }
    }
}