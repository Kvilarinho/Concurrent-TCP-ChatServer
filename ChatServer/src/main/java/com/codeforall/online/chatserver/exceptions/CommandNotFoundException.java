package com.codeforall.online.chatserver.exceptions;

/**
 * Thrown when a client attempts to use a command
 * that does not exist or is not registered.
 */
public class CommandNotFoundException extends RuntimeException {

    /**
     * Creates a new exception for an unknown command.
     *
     * @param command the command token that was not recognized
     */
    public CommandNotFoundException(String command) {
        super("Command not found: " + command + ". Try /help.");
    }
}
