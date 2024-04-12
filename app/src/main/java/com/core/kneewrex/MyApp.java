package com.core.kneewrex;

import android.app.Application;

import com.core.kneewrex.tcp.TcpClient;
import com.core.kneewrex.tcp.TcpListener;

public class MyApp extends Application {

    private TcpClient client;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the TCP client
        client = new TcpClient(new TcpListener() {
            @Override
            public void connected(boolean status) {
                // Handle connection status
            }

            @Override
            public void listenConnection(boolean status) {
                // Handle connection listening status
            }

            @Override
            public void messageReceived(String message) {
                // Handle received message
            }
        });

        // Start the client
        client.run();
    }

    public TcpClient getTcpClient() {
        return client;
    }
}
