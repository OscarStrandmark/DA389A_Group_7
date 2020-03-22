package client;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class GameClientTest {

    @Test
    void throwDice() {
        GameClient gameClient = new GameClient();
        int out = gameClient.throwDice();
        assertEquals(3, out);
    }

    @Test
    void shootDice() {
        GameClient gameClient = new GameClient();
        Boolean out = gameClient.shootDice();
        assertEquals(false, out);

    }

    @Test
    void jumpDice() {
        GameClient gameClient = new GameClient();
        Boolean out = gameClient.jumpDice();
        assertEquals(true, out);
    }
}