package com.core.kneewrex.tcp;

public interface TcpListener {
    void messageReceived(String message);

    void connected(boolean status);

    void listenConnection(boolean status);
}