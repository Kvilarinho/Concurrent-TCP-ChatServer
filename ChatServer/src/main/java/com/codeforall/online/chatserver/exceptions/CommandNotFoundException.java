package com.codeforall.online.chatserver.exceptions;

public class CommandNotFoundException extends RuntimeException {
    public CommandNotFoundException(String command) {
        super("Command not found: " + command + ". Try /help.");
    }
}
