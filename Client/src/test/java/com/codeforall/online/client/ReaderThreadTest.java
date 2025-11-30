package com.codeforall.online.client;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.Socket;

import static org.mockito.Mockito.*;

public class ReaderThreadTest {

    //SUT
    private ReaderThread readerThread;

    //DOCs
    private Client mockClient;
    private Socket mockSocket;

    @Before
    public void setUp() throws IOException {

        // DOCs
        mockClient = mock(Client.class);
        mockSocket = mock(Socket.class);

        when(mockClient.isRunning()).thenReturn(true);

        String message = "Hello\nServer is shutting down\n";
        ByteArrayInputStream fakeInput = new ByteArrayInputStream(message.getBytes());

        when(mockSocket.getInputStream()).thenReturn(fakeInput);

        // SUT
        readerThread = new ReaderThread(mockClient, mockSocket);
    }

    @Test
    public void runShouldCallClientShutdown() {

        readerThread.run();

        verify(mockClient, atLeastOnce()).shutdown();
    }
}
