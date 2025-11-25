package com.codeforall.online.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ReaderThread implements Runnable {

    private final Client client;
    private final Socket socket;

    public ReaderThread(Client client, Socket socket) {
        this.client = client;
        this.socket = socket;
    }

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
