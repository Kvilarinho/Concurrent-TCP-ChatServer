package com.codeforall.online.chatserver.commands;

import com.codeforall.online.chatserver.ClientHandler;
import com.codeforall.online.chatserver.exceptions.InvalidCommandArgumentsException;

/**
 * Implements the /name command, allowing a client to set a new username.
 * Once changed, the server broadcasts the update to all connected users.
 */
public class NameCommand implements Commands {

    /**
     * Changes the client's name and notifies all other users.
     *
     * @param fullCommand the full text of the command typed by the client
     * @param handler     the client requesting the name change
     * @throws InvalidCommandArgumentsException if the new name is missing
     */
    @Override
    public void execute(String fullCommand, ClientHandler handler) {
        String[] parts = fullCommand.split("\\s+", 2);

        if (parts.length < 2) {
            throw new InvalidCommandArgumentsException("Usage: /name <newName>");
        }

        String oldName = handler.getName();
        String newName = parts[1];

        handler.setName(newName);
        handler.send("Your name is now: " + newName);

        handler.getServer().broadcast(oldName + " is now known as " + handler.getName());
    }
}
