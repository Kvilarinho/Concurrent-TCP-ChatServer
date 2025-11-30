package com.codeforall.online.chatserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main server class responsible for accepting client connections,
 * managing the list of connected clients, and broadcasting messages.
 */
public class ChatServer {

    private final int port;
    private final CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

    private ServerSocket serverSocket;
    private volatile boolean running = false;

    private static final String ADMIN_PASSWORD = "supersecret";

    private final ExecutorService clientPool = Executors.newCachedThreadPool();

    /**
     * Creates a new chat server on the given port.
     *
     * @param port the port to listen on
     */
    public ChatServer(int port) {
        this.port = port;
    }

    /**
     * Starts the server loop, accepts incoming clients,
     * and launches a dedicated handler thread for each connection.
     */
    public void init() {
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            System.out.println("Chat server listening on port: " + port);

            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();

                    ClientHandler client = new ClientHandler(this, clientSocket);
                    clients.add(client);

                    clientPool.submit(client);

                    System.out.println(
                            "Client connected: [" + clientSocket.getInetAddress()
                                    + ":" + clientSocket.getPort() + "]");

                } catch (IOException e) {
                    if (!running) {
                        System.out.println("Server stopped.");
                        break;
                    }
                    System.out.println("Error accepting client: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.out.println("Could not start server: " + e.getMessage());
        } finally {

            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                System.out.println("Error closing server socket: " + e.getMessage());
            }

            clientPool.shutdownNow();
            System.out.println("Server fully terminated.");
        }
    }

    /**
     * Sends a message to all connected clients.
     *
     * @param message the message to broadcast
     */
    public void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.send(message);
        }
    }

    /**
     * Removes a disconnected client from the server list.
     *
     * @param client the client to remove
     */
    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    /**
     * Sends a private message to a specific client.
     *
     * @param name    target client name
     * @param message message to send
     * @param from    origin client
     * @return true if the message was delivered, false otherwise
     */
    public boolean whisper(String name, String message, ClientHandler from) {
        String fromName = from.getName();

        for (ClientHandler client : clients) {
            if (client.getName().equalsIgnoreCase(name)) {
                client.send(fromName + " (whisper): " + message);
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a formatted list of all clients currently online.
     *
     * @return list of connected clients
     */
    public String listClients() {
        StringBuilder sb = new StringBuilder("Clients online:\n");
        for (ClientHandler client : clients) {
            sb.append(client.getName()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Validates the admin password.
     *
     * @param password input password
     * @return true if correct, false otherwise
     */
    public boolean isValidAdminPassword(String password) {
        return ADMIN_PASSWORD.equals(password);
    }

    /**
     * Stops the server, disconnects all clients,
     * and releases network resources.
     */
    public void shutdown() {
        running = false;

        for (ClientHandler client : clients) {
            client.shutdownCleanUp();
        }

        clients.clear();

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Error closing server socket: " + e.getMessage());
        }
    }

    /**
     * Checks whether the server is running.
     *
     * @return true if running, false otherwise
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Entry point for starting the ChatServer.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        new ChatServer(9001).init();
    }
}
