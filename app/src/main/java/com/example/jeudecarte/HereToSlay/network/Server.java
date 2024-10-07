package com.example.jeudecarte.HereToSlay.network;


import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import com.example.jeudecarte.HereToSlay.controller.Controller;
import com.example.jeudecarte.HereToSlay.controller.HubController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
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
    private final int port;

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

    //Constructor

    /**
     * Create a new server, listening on the given port
     *
     * @param port the port on which the server is listening
     */
    public Server(int port) {
        this.port = port;
        connectedClient = new HashMap<>();
        clientOutput = new HashMap<>();
        lock = new ReentrantLock();
    }

    //Methods

    /**
     * Start the server, that consist of an infinite loop of accepting incoming connexion
     * Should be run in a thread
     */
    public void run() {
        Log.d("affichage debug","ici");
        try (ServerSocket serverSocket = new ServerSocket(port)) {
//            Log.d("affichage debug",getIPAddress(true));

            this.serverSocket = serverSocket;
            close = false;
            System.out.println("Server is running on port " + port);

            // As long as the server is open, it accepts connections
            while (!close) {
                try {
                    Socket socket = serverSocket.accept();
                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                    System.out.println("New connection from " + socket.getInetAddress().getHostAddress());

                    UUID playerUUID = UUID.randomUUID();

                    connectedClient.put(playerUUID,socket);
                    clientOutput.put(playerUUID,output);

                    Packet packet = new Packet("uuid");
                    packet.playerUUID = playerUUID;
//                    packet.host = getIPAddress(true);
                    sendDataByUUID(packet, playerUUID);

                    new Thread(() -> listen(socket,input,playerUUID)).start();

                } catch (IOException exception) {
                    if (!close) System.err.println("Error while accepting connexion: " + exception.getMessage());
                }
            }
        } catch (IOException exception) {
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
            System.out.println("Server is stopped");
        } catch (IOException exception) {
            System.err.println("Error while stopping the server: " + exception.getMessage());
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
                controller.dataTreatment(receivedObject);
            }
            catch (ClassNotFoundException exception) {
                System.out.println("Error while receiving data: " + exception.getMessage());

            }
            catch (IOException exception){
                try {
                    System.out.println("IO error, socket is connexion is closing" + exception.getMessage());
                    socket.close();
                }
                catch (IOException exception2){
                    System.out.println("Error while closing client connexion" + exception2.getMessage());
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
                System.out.println("I/O error while sending data: " + exception.getMessage());
            }
        }
    }

    /**
     * Send the packet to the client having the specified uuid
     *
     * @param packet the data to send
     * @param playerUUID the id of the player to which the date must be send
     */
    public void sendDataByUUID(Packet packet, UUID playerUUID){
        ObjectOutputStream output = clientOutput.get(playerUUID);
        try {
            output.writeObject(packet);
            output.flush();
        } catch (IOException exception) {
            System.out.println("I/O error while sending data: " + exception.getMessage());
        }
    }
}