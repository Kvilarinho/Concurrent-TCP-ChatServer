package com.codeforall.online.chatserver;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Classe principal responsável por aceitar conexões de clients,
 * manter os clientes conectados numa lista e realizar broadcast
 * ou enviar mensagens privadas.
 */
public class ChatServer {

    private final int port;
    private final CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

    private ServerSocket serverSocket;

    private volatile boolean running = false;

    private static final String ADMIN_PASSWORD = "supersecret";

    private final ExecutorService clientPool = Executors.newCachedThreadPool();
    public ChatServer(int port) {
        this.port = port;
    }

    /**
     * Aguarda conexões dos clients, adiciona-os à lista
     * e inicializa uma nova thread para cada cliente.
     *
     * @throws IOException se ocorrer erro ao criar o ServerSocket
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
                    String clientAddress = "[" + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + "]";
                    clientPool.submit(client);
                    System.out.println("Client connected: " + clientAddress);
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
     * Responsável por enviar mensagem para todos os clientes conectados.
     *
     * @param message mensagem a enviar
     */
    public void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.send(message);
        }
    }

    /**
     * Remove o cliente da lista quando desconecta
     *
     * @param client cliente a remover da lista
     */
    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    /**
     * Envia uma mensagem privada (whisper) para um cliente específico.
     *
     * @param name    nome do cliente de destino
     * @param message mensagem a enviar
     * @param from    cliente que enviou a mensagem
     * @return true se a mensagem foi enviada com sucesso, false caso o cliente não exista
     */
    public boolean whisper(String name, String message, ClientHandler from) {
        String fromClient = from.getName();
        for (ClientHandler client : clients) {
            String clientName = client.getName();
            if (clientName.equalsIgnoreCase(name)) {
                client.send(fromClient + " (whisper): " + message);
                return true;
            }
        }
        return false;
    }


    /**
     * Retorna uma string com a lista de clientes atualmente online.
     *
     * @return lista formatada de clientes conectados
     */
    public String listClients() {
        StringBuilder sb = new StringBuilder("Clients online: \n");
        for (ClientHandler client : clients) {
            sb.append(client.getName() + "\n");
        }
        return sb.toString();
    }

    public boolean isValidAdminPassword(String password) {
        return ADMIN_PASSWORD.equals(password);
    }

    public void shutdown() {
        running = false;

        for (ClientHandler client: clients) {
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

    public boolean isRunning() {
        return running;
    }


    /**
     * Ponto de entrada do ChatServer
     *
     * @param args
     */
    public static void main(String[] args) {
        new ChatServer(9001).init();
    }

}
