package com.codeforall.online.chatserver;

import com.codeforall.online.chatserver.commands.*;
import com.codeforall.online.chatserver.exceptions.CommandNotFoundException;
import com.codeforall.online.chatserver.exceptions.InvalidCommandArgumentsException;
import com.codeforall.online.chatserver.exceptions.UnauthorizedCommandException;
import com.codeforall.online.chatserver.exceptions.UserNotFoundException;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles the lifecycle and communication of a single connected client.
 * Responsible for reading messages, processing commands and
 * interacting with the ChatServer.
 */
public class ClientHandler implements Runnable {

    private ChatServer server;
    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String name = "Anonymous";
    private final Map<String, Commands> commandsMap = new HashMap<>();

    private boolean admin = false;

    /**
     * Creates a new client handler for the given server and socket.
     *
     * @param server       the chat server instance
     * @param clientSocket the socket connected to the client
     */
    public ClientHandler(ChatServer server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;

        registerCommands();
    }

    /**
     * Registers all available commands for this client.
     */
    private void registerCommands() {
        commandsMap.put("/help", new HelpCommand());
        commandsMap.put("/list", new ListCommand());
        commandsMap.put("/name", new NameCommand());
        commandsMap.put("/whisper", new WhisperCommand());
        commandsMap.put("/admin", new AdminLoginCommand());
        commandsMap.put("/shutdown", new ShutdownCommand());
    }

    /**
     * Main loop for the client: opens streams, asks for a username,
     * processes incoming messages and commands until the client disconnects.
     */
    @Override
    public void run() {

        try {
            openStreams();

            writer.println("Welcome to the chat! Enter your username:");
            checkUsername();

            writer.println("Hello, " + this.name + "!\nYou can start chatting now.\n" +
                    "Please use /quit when you wish to exit the chat and /help to list all the available commands");
            server.broadcast(this.name + " has entered the chat.");

            String message;
            while ((message = reader.readLine()) != null) {

                if (message.equalsIgnoreCase("/quit")) {
                    writer.println("Bye");
                    break;
                }

                if (message.startsWith("/")) {
                    try {
                        handleCommands(message);
                    } catch (CommandNotFoundException |
                             InvalidCommandArgumentsException |
                             UserNotFoundException |
                             UnauthorizedCommandException e) {
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

    /**
     * Cleans up resources and optionally notifies other clients
     * that this client has left the chat.
     *
     * @param notifyOthers if true, broadcasts a leave message
     */
    private void cleanUp(boolean notifyOthers) {
        closeStreams();
        server.removeClient(this);
        if (notifyOthers && server.isRunning()) {
            server.broadcast(this.name + " left the chat");
        }
    }

    /**
     * Cleans up resources and broadcasts that the client left.
     */
    private void cleanUp() {
        cleanUp(true);
    }

    /**
     * Cleans up resources without notifying other clients.
     * Used when the server is shutting down.
     */
    public void shutdownCleanUp() {
        cleanUp(false);
    }

    /**
     * Closes I/O streams and the client socket.
     */
    private void closeStreams() {
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (!clientSocket.isClosed()) clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses and executes a command message.
     *
     * @param message the full command line sent by the client
     */
    private void handleCommands(String message) {
        String trimmed = message.trim();
        String commandToken = trimmed.split("\\s+", 2)[0]; // ex: "/name"
        Commands commands = commandsMap.get(commandToken);

        if (commands != null) {
            commands.execute(message, this);
        } else {
            throw new CommandNotFoundException(commandToken);
        }
    }

    /**
     * Asks the client for a valid username and sets it.
     *
     * @throws IOException if the client disconnects before choosing a username
     */
    private void checkUsername() throws IOException {
        while (name.equalsIgnoreCase("Anonymous")) {

            String maybeName = reader.readLine();

            if (maybeName == null) {
                throw new IOException("Client disconnected before choosing username");
            }

            maybeName = maybeName.trim();

            if (maybeName.length() > 3) {
                this.name = maybeName;
            } else {
                writer.println("The username must be longer than 3 characters, try again");
            }
        }
    }

    /**
     * Opens input and output streams for this client connection.
     *
     * @throws IOException if an error occurs while opening streams
     */
    private void openStreams() throws IOException {
        reader = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
        writer = new PrintWriter(
                new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);
    }

    /**
     * Sends a message to this client.
     *
     * @param message the message to send
     */
    public void send(String message) {
        synchronized (this) {
            if (writer != null) {
                writer.println(message);
            }
        }
    }

    /**
     * Returns the server associated with this client handler.
     *
     * @return the chat server instance
     */
    public ChatServer getServer() {
        return server;
    }

    /**
     * Sets the display name for this client.
     *
     * @param name the new client name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the current display name of this client.
     *
     * @return the client name
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if this client has admin permissions.
     *
     * @return true if admin, false otherwise
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * Sets the admin flag for this client.
     *
     * @param admin true to grant admin permissions, false to revoke
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
