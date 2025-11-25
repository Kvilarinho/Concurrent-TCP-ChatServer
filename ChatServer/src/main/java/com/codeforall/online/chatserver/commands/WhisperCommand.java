package com.codeforall.online.chatserver.commands;

import com.codeforall.online.chatserver.ClientHandler;
import com.codeforall.online.chatserver.exceptions.InvalidCommandArgumentsException;
import com.codeforall.online.chatserver.exceptions.UserNotFoundException;

public class WhisperCommand implements Commands {

    @Override
    public void execute(String fullCommand, ClientHandler handler) {
        String[] parts = fullCommand.trim().split("\\s+", 3);  //whisper username message
        if (parts.length < 3) {
            throw new InvalidCommandArgumentsException("Usage: /whisper <username> <message>");
        }

        String user = parts[1];
        String msg = parts[2];
        boolean ok = handler.getServer().whisper(user, msg, handler);
        handler.send(handler.getName() + " (whisper): " + msg);
        if (!ok) {
            throw new UserNotFoundException("User not found");
        }
    }
}
