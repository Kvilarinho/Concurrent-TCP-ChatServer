package com.codeforall.online.chatserver.exceptions;

/**
 * Thrown when a command is used with missing or invalid arguments.
 */
public class InvalidCommandArgumentsException extends RuntimeException {

    /**
     * Creates a new exception describing the argument error.
     *
     * @param message details about the invalid arguments
     */
    public InvalidCommandArgumentsException(String message) {
        super(message);
    }
}
