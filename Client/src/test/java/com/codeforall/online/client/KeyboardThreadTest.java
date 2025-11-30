package com.codeforall.online.client;

import org.junit.Before;
import org.junit.Test;


import java.io.ByteArrayInputStream;
import java.io.PrintWriter;


import static org.mockito.Mockito.*;

public class KeyboardThreadTest {

    // SUT
    private KeyboardThread keyboardThread;

    //DOCs
    private Client mockClient;
    private PrintWriter mockWriter;

    @Before
    public void setUp() {
        mockClient = mock(Client.class);
        mockWriter = mock(PrintWriter.class);

        when(mockClient.isRunning()).thenReturn(true);
    }

    @Test
    public void runShouldSendQuitAndShutdownClient() {

        String fakeInput = "/quit\n";
        System.setIn(new ByteArrayInputStream(fakeInput.getBytes()));

        keyboardThread = new KeyboardThread(mockClient, mockWriter);

        keyboardThread.run();

        verify(mockWriter).println("/quit");
        verify(mockClient).shutdown();
    }

    @Test
    public void runShouldSendNormalMessage() {

        String fakeInput = "hello\n";
        System.setIn(new ByteArrayInputStream(fakeInput.getBytes()));

        keyboardThread = new KeyboardThread(mockClient, mockWriter);

        keyboardThread.run();

        verify(mockWriter).println("hello");
        verify(mockClient, never()).shutdown();
    }
}
