package com.codeforall.online.chatserver.commands;

import com.codeforall.online.chatserver.ChatServer;
import com.codeforall.online.chatserver.ClientHandler;
import com.codeforall.online.chatserver.exceptions.UnauthorizedCommandException;

/**
 * Implements the /shutdown command, allowing an administrator
 * to shut down the entire server and disconnect all clients.
 */
public class ShutdownCommand implements Commands {

    /**
     * Shuts down the server if the client is an admin.
     *
     * @param fullCommand the full command text typed by the client
     * @param handler     the client requesting the shutdown
     * @throws UnauthorizedCommandException if the client is not an admin
     */
    @Override
    public void execute(String fullCommand, ClientHandler handler) {
        if (!handler.isAdmin()) {
            throw new UnauthorizedCommandException("You are not allowed to use this command.");
        }

        ChatServer server = handler.getServer();
        handler.send("Shutting down server...");
        server.broadcast("Server is shutting down by admin " + handler.getName() + "...");
        server.shutdown();
    }
}
