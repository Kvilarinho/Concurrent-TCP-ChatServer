package com.codeforall.online.chatserver.commands;

import com.codeforall.online.chatserver.ClientHandler;

/**
 * Implements the /help command, sending the list of available
 * commands to the client. If the client is an admin, the
 * /shutdown command is also included.
 */
public class HelpCommand implements Commands {

    /**
     * Sends a help message listing all supported commands.
     *
     * @param fullCommand the full text of the command typed by the client
     * @param handler     the client requesting help
     */
    @Override
    public void execute(String fullCommand, ClientHandler handler) {

        handler.send("Available commands: \n" +
                "/quit - leave the chat \n" +
                "/name <newUsername> - change your username \n" +
                "/list - list all connected clients \n" +
                "/whisper <username> <message> - sends a message to a specific user \n" +
                "/admin - login as admin \n" +
                "/help - show this help message");

        if (handler.isAdmin()) {
            handler.send("/shutdown - shutdown server");
        }
    }
}
