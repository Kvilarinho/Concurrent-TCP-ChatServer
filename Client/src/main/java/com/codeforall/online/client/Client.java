package com.codeforall.online.client;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Simple TCP chat client that connects to a server,
 * starts reader and keyboard threads, and manages shutdown.
 */
public class Client {

    private final String host;
    private final int port;

    private Socket socket;
    private PrintWriter writer;

    private volatile boolean running = true;

    /**
     * Creates a new client for a given host and port.
     *
     * @param host the server hostname or IP
     * @param port the server port
     */
    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Starts the client: connects to the server, launches worker threads
     * and waits for the reader thread to finish.
     */
    public void start() {

        try {
            socket = new Socket(host, port);

            writer = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),
                    true);

            ReaderThread readerTask = new ReaderThread(this, socket);
            Thread readerThread = new Thread(readerTask, "Server-Reader");

            KeyboardThread keyboardTask = new KeyboardThread(this, writer);
            Thread keyboardThread = new Thread(keyboardTask, "Keyboard-Input");
            keyboardThread.setDaemon(true);

            readerThread.start();
            keyboardThread.start();

            readerThread.join();

        } catch (IOException e) {
            System.out.println("Could not connect to server: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            shutdown();
        }
    }

    /**
     * Stops the client, closes resources, and prevents new operations.
     * Method is safe to call multiple times.
     */
    public synchronized void shutdown() {

        if (!running) {
            return;
        }

        running = false;

        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Error closing client socket: " + e.getMessage());
        }

        if (writer != null) {
            writer.close();
        }

        System.out.println("Client shutdown complete.");
    }

    /**
     * Checks if the client is still running.
     *
     * @return true if running, false otherwise
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Entry point to start the client application.
     *
     * @param args ignored
     */
    public static void main(String[] args ) {
        new Client("localhost", 9001).start();
    }
}
