package edu.cmu.cs.cs214.hw4.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeckTest {
    Deck deck = new Deck();
    private Tile getTile(String code){
        Deck tiles = new Deck();
        Tile tile = tiles.drawTile();
        while(!tile.getCode().equals(code)){
            tile = tiles.drawTile();
        }
        return tile;
    }
    @Test
    public void drawTile() {
        Tile t1 = deck.drawTile();
        Tile t2 = getTile(t1.getCode());
        assertEquals(t1,t2);
    }
    @Test
    public void testDrawNotNull(){
        Tile tile = deck.drawTile();
        assertNotNull(tile);
    }
    @Test
    public void getNumberOfTiles() {
        assertEquals(71, deck.getNumberOfTiles());
        deck.drawTile();
        deck.drawTile();
        assertEquals(69, deck.getNumberOfTiles());
    }
    @Test (expected = IllegalArgumentException.class)
    public void testNoTilesLeft() throws IllegalArgumentException{
        while (deck.getNumberOfTiles() > 0) {
            deck.drawTile();
        }
        deck.drawTile();
    }

    @Test
    public void hasTilesLeft() {
        deck.drawTile();
        assertTrue(deck.hasTilesLeft());
        while (deck.getNumberOfTiles() > 0) {
            deck.drawTile();
        }
        assertFalse(deck.hasTilesLeft());
    }
    @Test
    public void getStartTile() {
        assertEquals(getTile("D"), deck.getStartTile());
    }


}