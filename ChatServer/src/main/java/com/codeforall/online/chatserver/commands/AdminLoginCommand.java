package com.codeforall.online.chatserver.commands;

import com.codeforall.online.chatserver.ClientHandler;
import com.codeforall.online.chatserver.exceptions.InvalidCommandArgumentsException;

/**
 * Handles the /admin command, allowing a client to authenticate
 * as an administrator using a password.
 */
public class AdminLoginCommand implements Commands {

    /**
     * Validates the admin password and grants admin privileges
     * to the client if the password is correct.
     *
     * @param fullCommand the full command line typed by the client
     * @param handler     the client executing the command
     * @throws InvalidCommandArgumentsException if the password is missing
     */
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

