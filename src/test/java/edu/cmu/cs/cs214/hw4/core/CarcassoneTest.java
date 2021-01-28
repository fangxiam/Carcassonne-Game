package edu.cmu.cs.cs214.hw4.core;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CarcassoneTest {
    Carcassonne carcassonne;
    List<String> players = new ArrayList<>();
    private Tile getTile(String code){
        Deck tiles = new Deck();
        Tile tile = tiles.drawTile();
        while(!tile.getCode().equals(code)){
            tile = tiles.drawTile();
        }
        return tile;
    }
    @Before
    public void setUp() throws Exception {
        players.add("Mike");
        players.add("Jerry");
        players.add("Tom");
        players.add("Freya");
        carcassonne = new Carcassonne(players);
    }
    @Test
    public void getDeck() {
        assertNotNull(carcassonne.getDeck());
        assertEquals(71, carcassonne.getDeck().getNumberOfTiles());
    }

    @Test
    public void getBoard() {
        assertNotNull(carcassonne.getBoard());
        assertEquals(getTile("D"), carcassonne.getBoard().getTile(71, 71));
    }
    @Test (expected = IllegalArgumentException.class)
    public void testTooManyPlayers() {
        players.add("Amy");
        players.add("Bob");
        Carcassonne c = new Carcassonne(players);
    }

}