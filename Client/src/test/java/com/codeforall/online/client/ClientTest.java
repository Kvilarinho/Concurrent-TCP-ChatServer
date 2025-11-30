package com.codeforall.online.client;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ClientTest {

    //SUT
    private Client client;

    @Before
    public void setUp() {
        this.client = new Client("localhost", 9001);
    }

    @Test
    public void constructorShouldStartClientAsRunning() {
        assertTrue(client.isRunning());
    }

    @Test
    public void shutdownSetRunningFalse() {
        assertTrue(client.isRunning());

        // when
        client.shutdown();

        // then
        assertFalse(client.isRunning());
    }

    @Test
    public void shutdownCanBeCalledTwiceWithoutCrashing() {

        // when
        client.shutdown();
        client.shutdown();

        assertFalse(client.isRunning());
    }
}
