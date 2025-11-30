package com.codeforall.online.chatserver.exceptions;

/**
 * Thrown when a client attempts to interact with a user
 * that does not exist or is not currently connected.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Creates a new exception indicating the missing user.
     *
     * @param message details about the missing user
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
