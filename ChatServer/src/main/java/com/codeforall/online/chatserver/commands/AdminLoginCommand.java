package com.codeforall.online.chatserver.commands;

import com.codeforall.online.chatserver.ClientHandler;
import com.codeforall.online.chatserver.exceptions.InvalidCommandArgumentsException;

public class AdminLoginCommand implements Commands{
    @Override
    public void execute(String fullCommand, ClientHandler handler) {
        String[] parts = fullCommand.split("\\s+", 2);

        if (parts.length < 2) {
            throw new InvalidCommandArgumentsException("Usage: /admin <password>");
        }

        String password = parts[1];

        if (handler.getServer().isValidAdminPassword(password)) {
            handler.setAdmin(true);
            handler.send("You are now an admin.");
        } else {
            handler.send("Invalid admin password.");
        }
    }
}
