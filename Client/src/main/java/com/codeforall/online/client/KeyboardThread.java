package com.codeforall.online.client;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Thread responsible for reading user input from the keyboard
 * and sending messages to the server.
 */
public class KeyboardThread implements Runnable {

    private final Client client;
    private final PrintWriter writer;

    /**
     * Creates a new keyboard handler for the given client.
     *
     * @param client the client instance
     * @param writer the writer used to send messages to the server
     */
    public KeyboardThread(Client client, PrintWriter writer) {
        this.client = client;
        this.writer = writer;
    }

    /**
     * Reads lines from System.in while the client is running.
     * Sends each line to the server. If "/quit" is entered,
     * the client is shut down and the loop ends.
     */
    @Override
    public void run() {

        try (Scanner keyboard = new Scanner(System.in, StandardCharsets.UTF_8)) {

            while (client.isRunning() && keyboard.hasNextLine()) {

                String line;
                try {
                    line = keyboard.nextLine();
                } catch (IllegalStateException e) {
                    break;
                }

                if (!client.isRunning()) {
                    break;
                }

                if (line.equalsIgnoreCase("/quit")) {
                    writer.println("/quit");
                    client.shutdown();
                    break;
                }

                writer.println(line);
            }
        }
    }
}
