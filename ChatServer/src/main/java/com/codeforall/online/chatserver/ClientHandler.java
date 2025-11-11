package com.codeforall.online.chatserver;

import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private ChatServer server;
    private Socket clientSocket;


    public ClientHandler(ChatServer server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

    }
}
