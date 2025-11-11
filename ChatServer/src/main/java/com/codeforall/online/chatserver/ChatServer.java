package com.codeforall.online.chatserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Classe principal responsável por aceitar conexões de clients,
 * manter os clientes conectados numa lista e realizar broadcast
 * ou enviar mensagens privadas.
 */
public class ChatServer {

    private final int port;
    private final CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public ChatServer(int port) {
        this.port = port;
    }

    /**
     * Aguarda conexões dos clients, adiciona-os à lista
     * e inicializa uma nova thread para cada cliente.
     *
     * @throws IOException se ocorrer erro ao criar o ServerSocket
     */
    public void init() throws IOException {

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Chat server listening on port: " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            ClientHandler client = new ClientHandler(this, clientSocket);
            clients.add(client);

            String clientAdress = "[" + clientSocket.getInetAddress().toString() + ":" + clientSocket.getPort() + "]";
            new Thread(client, "Client - " + clientAdress).start();
            System.out.println(Thread.currentThread().getName());

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


    /**
     * Ponto de entrada do ChatServer
     *
     * @param args
     */
    public static void main(String[] args) {

        try {
            new ChatServer(9001).init();
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}
