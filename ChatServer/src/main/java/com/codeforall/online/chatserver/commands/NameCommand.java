package com.codeforall.online.chatserver.commands;

import com.codeforall.online.chatserver.ClientHandler;
import com.codeforall.online.chatserver.exceptions.InvalidCommandArgumentsException;

public class NameCommand implements Commands {

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

