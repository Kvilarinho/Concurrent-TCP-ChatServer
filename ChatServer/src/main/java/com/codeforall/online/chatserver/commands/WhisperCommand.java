package com.codeforall.online.chatserver.commands;

import com.codeforall.online.chatserver.ClientHandler;
import com.codeforall.online.chatserver.exceptions.InvalidCommandArgumentsException;
import com.codeforall.online.chatserver.exceptions.UserNotFoundException;

/**
 * Implements the /whisper command, allowing a client to send a
 * private message to a specific user.
 */
public class WhisperCommand implements Commands {

    /**
     * Sends a private message to another user. If the target user
     * does not exist, an exception is thrown.
     *
     * @param fullCommand the full command text typed by the client
     * @param handler     the client sending the whisper
     * @throws InvalidCommandArgumentsException if the command is missing arguments
     * @throws UserNotFoundException if the recipient does not exist
     */
    @Override
    public void execute(String fullCommand, ClientHandler handler) {
        String[] parts = fullCommand.trim().split("\\s+", 3);  // whisper username message
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
