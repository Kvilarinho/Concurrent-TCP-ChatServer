package com.codeforall.online.chatserver.commands;

import com.codeforall.online.chatserver.ClientHandler;

/**
 * Represents a chat command that can be executed by a client.
 * Each command receives the full input line and the client handler
 * that issued the command.
 */
public interface Commands {

    /**
     * Executes the command logic.
     *
     * @param fullCommand the full text of the command typed by the client
     * @param handler     the client executing the command
     */
    void execute(String fullCommand, ClientHandler handler);
}
