package com.codeforall.online.chatserver.commands;

import com.codeforall.online.chatserver.ChatServer;
import com.codeforall.online.chatserver.ClientHandler;
import com.codeforall.online.chatserver.exceptions.UnauthorizedCommandException;

public class ShutdownCommand implements Commands{
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
