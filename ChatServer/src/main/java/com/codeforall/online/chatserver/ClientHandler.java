package com.codeforall.online.chatserver;

import com.codeforall.online.chatserver.commands.*;
import com.codeforall.online.chatserver.exceptions.CommandNotFoundException;
import com.codeforall.online.chatserver.exceptions.InvalidCommandArgumentsException;
import com.codeforall.online.chatserver.exceptions.UserNotFoundException;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable{

    private ChatServer server;
    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String name = "Anonymous";
    private final Map<String, Commands> commandsMap = new HashMap<>();

    public ClientHandler(ChatServer server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;

        registerCommands();
    }

    private void registerCommands() {
        commandsMap.put("/help", new HelpCommand());
        commandsMap.put("/list", new ListCommand());
        commandsMap.put("/name", new NameCommand());
        commandsMap.put("/whisper", new WhisperCommand());
    }

    @Override
    public void run() {

        try {
            openStreams();

            writer.println("Welcome to the chat! Enter your username:");
            checkUsername();

            writer.println("Hello, " + this.name + "! You can start chatting now." +
                    "Please use /quit when you wish to exit the chat and /help to list all the available commands");
            server.broadcast(this.name + " has entered the chat.");

            String message;
            while ((message = reader.readLine()) != null) {

                if (message.startsWith("/quit")) {
                    break;
                }

                if (message.startsWith("/")) {
                    try {
                        handleCommands(message);
                    } catch (CommandNotFoundException |
                             InvalidCommandArgumentsException |
                             UserNotFoundException e) {
                        send(e.getMessage());
                    }
                    continue;
                }

                server.broadcast(this.name + ": " + message);
            }

        } catch (IOException e) {
            System.out.println("Connection lost with client : " + clientSocket + ": " + e.getMessage());

        } finally {
            cleanUp();
        }
    }

    private void cleanUp() {
        closeStreams();
        server.removeClient(this);
        server.broadcast(this.name + " left the chat");
    }

    private void closeStreams() {
        try {
            clientSocket.close();
            if (reader != null) reader.close();
            if (writer != null) writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleCommands(String message) {
        String trimmed = message.trim();
        String commandToken = trimmed.split("\\s+", 2)[0]; //ex: "/name"
        Commands commands = commandsMap.get(commandToken);

        if (commands != null) {
            commands.execute(message, this);
        } else {
            throw new CommandNotFoundException(commandToken);
        }
    }

    private void checkUsername() throws IOException {
        while (name.equalsIgnoreCase( "Anonymous")) {

            String maybeName = reader.readLine().trim();

            if (maybeName == null) {
                throw new IOException("Client disconnected before choosing username");
            }
            if (maybeName.length() > 3) {
                this.name = maybeName;
            } else {
                writer.println("The username must be longer than 3 characters, try again");
            }
        }
    }

    private void openStreams() throws IOException {
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
        writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);
    }

    public void send(String message) {
        synchronized (this) {
            if (writer != null) {
                writer.println(message);
            }
        }
    }

    public ChatServer getServer() {
        return server;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
