package com.codeforall.online.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Thread responsible for reading messages from the server
 * and printing them to the console.
 */
public class ReaderThread implements Runnable {

    private final Client client;
    private final Socket socket;

    /**
     * Creates a new reader thread for a given client and socket.
     *
     * @param client the client instance
     * @param socket the socket connected to the server
     */
    public ReaderThread(Client client, Socket socket) {
        this.client = client;
        this.socket = socket;
    }

    /**
     * Continuously reads messages from the server while the client is running.
     * Stops when the server sends a shutdown message or the connection ends.
     */
    @Override
    public void run() {

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            while (client.isRunning() && (line = reader.readLine()) != null) {
                System.out.println(line);

                if (line.startsWith("Server is shutting down")) {
                    break;
                }
            }

        } catch (IOException e) {
            if (client.isRunning()) {
                System.out.println("Connection closed by server");
            }
        } finally {
            client.shutdown();
        }
    }
}
