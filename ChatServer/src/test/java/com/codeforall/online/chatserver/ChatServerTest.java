package com.codeforall.online.chatserver;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ChatServerTest {

    // SUT
    private ChatServer chatServer;

    // DOCs
    private ClientHandler client1;
    private ClientHandler client2;

    @Before
    public void setUp() throws Exception {

        chatServer = new ChatServer(9001);

        client1 = mock(ClientHandler.class);
        client2 = mock(ClientHandler.class);

        when(client1.getName()).thenReturn("Alice");
        when(client2.getName()).thenReturn("Bob");

        // injects mocks in private list 'clients' with reflection
        Field clientsField = ChatServer.class.getDeclaredField("clients");
        clientsField.setAccessible(true);

        @SuppressWarnings("unchecked")
        CopyOnWriteArrayList<ClientHandler> clients =
                (CopyOnWriteArrayList<ClientHandler>) clientsField.get(chatServer);

        clients.add(client1);
        clients.add(client2);
    }

    @Test
    public void isValidAdminPasswordShouldReturnTrueForCorrectPassword() {
        assertTrue(chatServer.isValidAdminPassword("supersecret"));
    }

    @Test
    public void isValidAdminPasswordShouldReturnFalseForWrongPassword() {
        assertFalse(chatServer.isValidAdminPassword("wrong"));
    }

    @Test
    public void broadcastShouldSendMessageToAllClients() {

        String message = "Hello everyone";

        chatServer.broadcast(message);

        verify(client1).send(message);
        verify(client2).send(message);
    }

    @Test
    public void whisperShouldSendMessageToCorrectClientAndReturnTrue() {

        boolean result = chatServer.whisper("Alice", "Hi, there", client2);

        assertTrue(result);
        verify(client1).send("Bob (whisper): Hi, there");
        verify(client2, never()).send(anyString());
    }

    @Test
    public void whisperShouldReturnFalseIfClientNotFound() {

        boolean result = chatServer.whisper("Charlie", "Hi", client1);

        assertFalse(result);
        verify(client1, never()).send(anyString());
        verify(client2, never()).send(anyString());
    }

    @Test
    public void listClientsShouldContainAllClientNames() {

        String result = chatServer.listClients();

        assertTrue(result.contains("Alice"));
        assertTrue(result.contains("Bob"));
    }

    @Test
    public void shutdownShouldSetRunningFalseClearClientsAndCallShutdownOnEachClient() throws Exception {

        Field runningField = ChatServer.class.getDeclaredField("running");
        runningField.setAccessible(true);
        runningField.set(chatServer, true);

        chatServer.shutdown();

        assertFalse(chatServer.isRunning());

        Field clientsField = ChatServer.class.getDeclaredField("clients");
        clientsField.setAccessible(true);

        @SuppressWarnings("unchecked")
        CopyOnWriteArrayList<ClientHandler> clients =
                (CopyOnWriteArrayList<ClientHandler>) clientsField.get(chatServer);

        assertTrue(clients.isEmpty());

        verify(client1).shutdownCleanUp();
        verify(client2).shutdownCleanUp();
    }
}

