package com.codeforall.online.chatserver.exceptions;

public class InvalidCommandArgumentsException extends RuntimeException {
    public InvalidCommandArgumentsException(String message) {
        super(message);
    }
}
