package com.codeforall.online.chatserver;

import org.junit.Before;
import org.junit.Test;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.Socket;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ClientHandlerTest {

    // SUT
    private ClientHandler clientHandler;

    // DOCs
    private ChatServer mockServer;
    private Socket mockSocket;
    private PrintWriter mockWriter;

    @Before
    public void setUp() {

        mockServer = mock(ChatServer.class);
        mockSocket = mock(Socket.class);
        mockWriter = mock(PrintWriter.class);

        clientHandler = new ClientHandler(mockServer, mockSocket);
    }

    @Test
    public void sendShouldCallWriterPrintlnWhenWriterIsNotNull() throws Exception {

        Field writerField = ClientHandler.class.getDeclaredField("writer");
        writerField.setAccessible(true);
        writerField.set(clientHandler, mockWriter);

        clientHandler.send("Hello");

        verify(mockWriter).println("Hello");
    }

    @Test
    public void sendShouldNotThrowWhenWriterIsNull() {

        clientHandler.send("Hello");
    }

    @Test
    public void setNameShouldChangeClientName() {
        clientHandler.setName("Katia");

        assertEquals("Katia", clientHandler.getName());
    }

    @Test
    public void setAdminShouldChangeAdminFlag() {

        assertFalse(clientHandler.isAdmin());

        clientHandler.setAdmin(true);

        assertTrue(clientHandler.isAdmin());
    }
}
