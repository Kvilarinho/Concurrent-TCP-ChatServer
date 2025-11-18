package com.codeforall.online.chatserver.commands;

import com.codeforall.online.chatserver.ClientHandler;

public class ListCommand implements Commands {

    @Override
    public void execute(String fullCommand, ClientHandler handler) {
        handler.send(handler.getServer().listClients());
    }
}
