package edu.cmu.cs.cs214.hw4.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TileTest {
    Deck deck;
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
        deck = new Deck();
    }

    @Test
    public void getCode() {
        Tile t1 = deck.drawTile();
        Tile t2 = getTile(t1.getCode());
        assertEquals(t1.getCode(),t2.getCode());
    }

    @Test
    public void hasShield() {
        Tile t1 = getTile("M");
        assertTrue(t1.hasShield());
        Tile t2 = getTile("N");
        assertFalse(t2.hasShield());
    }

    @Test
    public void getIsJoined() {
        Tile t1 = getTile("G");
        assertTrue(t1.getIsJoined());
        Tile t2 = getTile("H");
        assertFalse(t2.getIsJoined());
    }

    @Test
    public void hasMonastery() {
        Tile t1 = getTile("A");
        assertTrue(t1.hasMonastery());
        Tile t2 = getTile("C");
        assertFalse(t2.hasMonastery());
    }

    @Test
    public void getSegments() {
        Tile tile = getTile("A");
        Segment seg1 = tile.getSegments(2);
        assertEquals(Segment.ROAD, seg1);
    }

    @Test
    public void rotate() {
        Tile tile = getTile("D");
        tile.rotate();
        Segment seg1 = tile.getSegments(0);
        Segment seg2 = tile.getSegments(2);
        assertEquals(Segment.FIELD, seg1);
        assertEquals(Segment.CITY, seg2);
    }

    @Test
    public void getInitialDirection() {
        Tile tile = getTile("D");
        assertEquals(0, tile.getInitialDirection());
        tile.rotate();
        tile.rotate();
        assertEquals(2, tile.getInitialDirection());
        tile.rotate();
        tile.rotate();
        assertEquals(0, tile.getInitialDirection());
    }

    @Test
    public void testToString() {
        assertEquals("A", getTile("A").toString());
    }

    @Test
    public void testEquals() {
        Tile t1 = getTile("A");
        Tile t2 = getTile("A");
        Tile t3 = getTile("B");
        assertNotEquals(t1, new Deck());
        assertEquals(t1, t2);
        assertNotEquals(t2, t3);
    }

    @Test
    public void testHashCode() {
        Tile t1 = getTile("A");
        Tile t2 = getTile("A");
        Tile t3 = getTile("B");
        assertEquals(t1.hashCode(), t2.hashCode());
        assertNotEquals(t2.hashCode(), t3.hashCode());
    }
}