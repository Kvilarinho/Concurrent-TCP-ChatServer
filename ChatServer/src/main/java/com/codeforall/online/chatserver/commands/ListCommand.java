package com.codeforall.online.chatserver.commands;

import com.codeforall.online.chatserver.ClientHandler;

/**
 * Implements the /list command, sending the list of all
 * connected clients to the requesting user.
 */
public class ListCommand implements Commands {

    /**
     * Sends the formatted list of online clients.
     *
     * @param fullCommand the full command text typed by the client
     * @param handler     the client requesting the list
     */
    @Override
    public void execute(String fullCommand, ClientHandler handler) {
        handler.send(handler.getServer().listClients());
    }
}
