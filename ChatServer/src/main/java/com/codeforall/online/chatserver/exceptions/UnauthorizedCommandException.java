package com.codeforall.online.chatserver.exceptions;

public class UnauthorizedCommandException extends RuntimeException {
    public UnauthorizedCommandException(String message) {
        super(message);
    }
}
