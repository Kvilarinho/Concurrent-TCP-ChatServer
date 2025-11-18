package com.codeforall.online.chatserver.commands;

import com.codeforall.online.chatserver.ClientHandler;

public class HelpCommand implements Commands {

    @Override
    public void execute(String fullCommand, ClientHandler handler) {
        handler.send("Available commands: \n" +
                "/quit - leave the chat \n" +
                "/name <newUsername> - change your username \n " +
                "/list - list all connected clients \n" +
                "/whisper <username> <message> - sends a message to a specific user \n +" +
                "/help - show this help message");
    }
}
