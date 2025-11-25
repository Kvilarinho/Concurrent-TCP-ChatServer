package com.codeforall.online.client;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class KeyboardThread implements Runnable {

    private final Client client;
    private final PrintWriter writer;

    public KeyboardThread(Client client, PrintWriter writer) {
        this.client = client;
        this.writer = writer;
    }

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
