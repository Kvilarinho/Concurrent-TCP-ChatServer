package com.codeforall.online.chatserver.exceptions;

/**
 * Thrown when a client attempts to execute a command
 * that requires permissions they do not have.
 */
public class UnauthorizedCommandException extends RuntimeException {

    /**
     * Creates a new exception indicating lack of authorization.
     *
     * @param message explanation of the authorization failure
     */
    public UnauthorizedCommandException(String message) {
        super(message);
    }
}
