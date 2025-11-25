package com.codeforall.online.client;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {

    private final String host;
    private final int port;

    private Socket socket;
    private PrintWriter writer;

    private volatile boolean running = true;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

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

    public boolean isRunning() {
        return running;
    }

    public static void main(String[] args ) {
        new Client("localhost", 9001).start();
    }
}
