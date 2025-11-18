package com.codeforall.online.chatserver.commands;

import com.codeforall.online.chatserver.ClientHandler;

public interface Commands {

    void execute(String fullCommand, ClientHandler handler);
}
