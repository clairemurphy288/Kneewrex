package com.core.kneewrex.tcp;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TcpClient {
    public static final String TAG = "TCP_CLIENT";
    public static final String SERVER_IP = "192.168.4.1";
    public static final String SERVER_PORT = "8080";
    //Message to send to server
    private String serverMessage;
    //Sends Message  Receive notification
    private TcpListener listener = null;
    //While this is true server will continue to run
    private Boolean listen = false;
    //Used to send messages
    private PrintWriter bufferOut;
    //Used to Read Messages from the server;
    private BufferedReader bufferIn;
    private Socket socket;

    public TcpClient(TcpListener listener) {
        this.listener = listener;
    }

    public void sendMessage(final String message) {
        Runnable runnable = () -> {
            if (bufferOut != null) {
                Log.d(TAG, "Sending: " + message);
                bufferOut.println(message);
                bufferOut.flush();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void stopClient() {
        listen = false;
        if (bufferOut != null) {
            bufferOut.flush();
            bufferOut.close();
        }
        try {
            socket.close();
        } catch (IOException e) {
            //e.printStackTrace();
            Log.d(TAG, "Socket: Error", e);
        }
        listener = null;
        bufferOut = null;
        bufferIn = null;
        serverMessage = null;
    }

    public void run() {
        listen = true;

        try {
            InetAddress serverAddress = InetAddress.getByName(SERVER_IP);
            Log.d(TAG, "C: Connecting...");
            socket = new Socket(serverAddress, Integer.parseInt(SERVER_PORT));
            listener.connected(socket.isConnected());
            try {
                bufferOut = new PrintWriter(socket.getOutputStream());
                Log.e(TAG, "C: Sent.");
                bufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                int charsRead = 0;
                //char[] buffer = new char[1024]; //choose your buffer size if you need other than 1024
                char[] buffer = new char[2048]; //choose your buffer size if you need other than 1024

                while (listen) {
                    //listen for connection
                    if (listener != null) {
                        listener.listenConnection(socket.isConnected());
                    }
                    charsRead = bufferIn.read(buffer);
                    serverMessage = new String(buffer).substring(0, charsRead);
                    if (serverMessage != null && listener != null) {
                        listener.messageReceived(serverMessage);
                    }
                    serverMessage = null;
                }
                Log.d(TAG, "S: Received Message: '" + serverMessage + "'");
            } catch (Exception e) {
                Log.d(TAG, "S: Error", e);
            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                socket.close();
            }

        } catch (Exception e) {
            Log.d(TAG, "C: Error", e);
        }
    }


}
