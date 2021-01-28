package edu.cmu.cs.cs214.hw4.core;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BoardTest {
    Carcassonne carcassone;
    Deck deck;
    Board board;
    int[] meepleBoard = new int[]{7, 7, 7, 7};
    int[] scoreBoard = new int[]{0, 0, 0, 0};
    private Tile getTile(String code){
        Deck tiles = new Deck();
        Tile tile = tiles.drawTile();
        while(!tile.getCode().equals(code)){
            tile = tiles.drawTile();
        }
        return tile;
    }
    List<String> players = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        players.add("Mike");
        players.add("Jerry");
        players.add("Tom");
        players.add("Freya");
        carcassone = new Carcassonne(players);
        deck = carcassone.getDeck();
        board = carcassone.getBoard();
    }

    @Test
    public void testStartTile() {
        assertEquals(board.getTile(71, 71), getTile("D"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testOutOfBound(){
        Tile t = getTile("U");
        board.placeTile(143, 0, t);
    }
    @Test (expected = IllegalArgumentException.class)
    public void testDiscard() {
        Tile t = getTile("E");
        t.rotate();
        t.rotate();
        t.rotate();
        board.placeTile(71, 72, t);
        Tile t2 = getTile("C");
        board.getLegalPositions(board.getOuterVacancies(), t2);
    }
    @Test
    public void testValidTilePlacementWithRotation() {
        Tile t = getTile("E");
        t.rotate();
        t.rotate();
        t.rotate();
        board.placeTile(71, 72, t);
        assertEquals(board.getTile(71, 72), getTile("E"));
    }
    @Test (expected = IllegalArgumentException.class)
    public void testInvalidTilePlacementWithRotation() {
        Tile t = getTile("E");
        t.rotate();
        t.rotate();
        t.rotate();
        board.placeTile(70, 72, t);
        assertEquals(board.getTile(71, 72), getTile("E"));
    }
    @Test (expected = IllegalArgumentException.class)
    public void testInvalidTilePlacement() {
        Tile t = getTile("E");
        t.rotate();
        t.rotate();
        t.rotate();
        board.placeTile(71, 72, t);
        Tile t2 = getTile("B");
        board.placeTile(72, 71, t2);
    }
    @Test (expected = IllegalArgumentException.class)
    public void testOccupiedTilePlacement() {
        Tile t = getTile("E");
        t.rotate();
        t.rotate();
        t.rotate();
        board.placeTile(71, 72, t);
        Tile t2 = getTile("H");
        board.placeTile(71, 72, t2);
    }
    @Test
    public void testMultipleTilesValidPlacement() {
        Tile curTile1 = getTile("A");
        board.placeTile(70, 71, curTile1);
        Tile curTile2 = getTile("G");
        board.placeTile(70, 72, curTile2);
        Tile curTile3 = getTile("I");
        curTile3.rotate();
        board.placeTile(69, 72, curTile3);
        Tile curTile4 = getTile("O");
        board.placeTile(71, 72, curTile4);
        Tile curTile5 = getTile("V");
        curTile5.rotate();
        curTile5.rotate();
        board.placeTile(72, 71, curTile5);
        Tile curTile7 = getTile("L");
        board.placeTile(71, 73, curTile7);
        Tile curTile6 = getTile("V");
        System.out.println(curTile6);
        curTile6.rotate();
        board.placeTile(72, 72, curTile6);
        assertEquals(board.getTile(70, 71), getTile("A"));
        assertEquals(board.getTile(70, 72), getTile("G"));
        assertEquals(board.getTile(69, 72), getTile("I"));
        assertEquals(board.getTile(71, 72), getTile("O"));
        assertEquals(board.getTile(72, 71), getTile("V"));
        assertEquals(board.getTile(71, 73), getTile("L"));
        assertEquals(board.getTile(72, 72), getTile("V"));
    }
    @Test
    public void testMeeplesPlacement() {
        Tile curTile1 = getTile("A");
        board.placeTile(70, 71, curTile1);
        board.placeMeeple(curTile1, 70, 71,2, 0, meepleBoard);
        Tile curTile2 = getTile("G");
        board.placeTile(70, 72, curTile2);
        board.placeMeeple(curTile2, 70, 72,0, 1, meepleBoard);
        Tile curTile3 = getTile("I");
        curTile3.rotate();
        board.placeTile(69, 72, curTile3);
        board.placeMeeple(curTile3, 69, 72,3, 2, meepleBoard);
        Tile curTile4 = getTile("O");
        board.placeTile(71, 72, curTile4);
        board.placeMeeple(curTile4, 71, 72,2, 3, meepleBoard);
        Tile curTile5 = getTile("V");
        curTile5.rotate();
        curTile5.rotate();
        board.placeTile(72, 71, curTile5);
        Tile curTile7 = getTile("L");
        board.placeTile(71, 73, curTile7);
        board.placeMeeple(curTile7, 71, 73,0, 1, meepleBoard);
        Tile curTile6 = getTile("V");
        System.out.println(curTile6);
        curTile6.rotate();
        board.placeTile(72, 72, curTile6);
        assertArrayEquals(new int[]{6, 5, 6, 6}, meepleBoard);
        assertEquals(0, board.getFeature(0).getMeeple());
        assertEquals(-1, board.getFeature(2).getMeeple());
    }
    @Test
    public void testMeeplePlacementOnIntersection() {
        Tile curTile1 = getTile("A");
        board.placeTile(70, 71, curTile1);
        board.placeMeeple(curTile1, 70, 71,2, 0, meepleBoard);
        Tile curTile2 = getTile("G");
        board.placeTile(70, 72, curTile2);
        board.placeMeeple(curTile2, 70, 72,0, 1, meepleBoard);
        Tile curTile3 = getTile("I");
        curTile3.rotate();
        board.placeTile(69, 72, curTile3);
        board.placeMeeple(curTile3, 69, 72,3, 2, meepleBoard);
        Tile curTile4 = getTile("O");
        board.placeTile(71, 72, curTile4);
        board.placeMeeple(curTile4, 71, 72,2, 3, meepleBoard);
        Tile curTile5 = getTile("V");
        curTile5.rotate();
        curTile5.rotate();
        board.placeTile(72, 71, curTile5);
        Tile curTile7 = getTile("L");
        board.placeTile(71, 73, curTile7);
        board.placeMeeple(curTile7, 71, 73,0, 1, meepleBoard);
        Tile curTile6 = getTile("V");
        System.out.println(curTile6);
        curTile6.rotate();
        board.placeTile(72, 72, curTile6);
        assertNotEquals(1, board.getFeature(curTile7.getFeatureCode(3)).getMeeple());
    }
    @Test (expected = IllegalArgumentException.class)
    public void testInvalidMeeplePlacement() {
        Tile curTile1 = getTile("A");
        board.placeTile(70, 71, curTile1);
        board.placeMeeple(curTile1, 70, 71, 2, 0, meepleBoard);
        Tile curTile2 = getTile("G");
        board.placeTile(70, 72, curTile2);
        board.placeMeeple(curTile2, 70, 72,0, 1, meepleBoard);
        Tile curTile3 = getTile("I");
        curTile3.rotate();
        board.placeTile(69, 72, curTile3);
        board.placeMeeple(curTile3, 69, 72,3, 2, meepleBoard);
        Tile curTile4 = getTile("O");
        board.placeTile(71, 72, curTile4);
        board.placeMeeple(curTile4, 71, 72,0, 3, meepleBoard);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testNotEnoughMeepelsLeft() {
        carcassone = new Carcassonne(players);
        deck = carcassone.getDeck();
        board = carcassone.getBoard();
        Tile curTile1 = getTile("A");
        board.placeTile(70, 71, curTile1);
        board.placeMeeple(curTile1, 70, 71,4, 2, meepleBoard);
        Tile curTile2 = getTile("G");
        board.placeTile(70, 72, curTile2);
        board.placeMeeple(curTile2, 70, 72,0, 2, meepleBoard);
        Tile curTile3 = getTile("I");
        curTile3.rotate();
        board.placeTile(69, 72, curTile3);
        board.placeMeeple(curTile3, 69, 72,3, 2, meepleBoard);
        Tile curTile4 = getTile("O");
        board.placeTile(71, 72, curTile4);
        board.placeMeeple(curTile4, 71, 72,2, 2, meepleBoard);
        Tile curTile5 = getTile("V");
        curTile5.rotate();
        curTile5.rotate();
        board.placeTile(72, 71, curTile5);
        board.placeMeeple(curTile5, 72, 71,0, 2, meepleBoard);
        Tile curTile7 = getTile("L");
        board.placeTile(71, 73, curTile7);
        board.placeMeeple(curTile7, 71, 73,0, 2, meepleBoard);
        Tile curTile6 = getTile("V");
        System.out.println(curTile6);
        curTile6.rotate();
        board.placeTile(72, 72, curTile6);
        Tile curTile8 = getTile("F");
        board.placeTile(69, 71, curTile8);
        Tile curTile9 = getTile("N");
        curTile9.rotate();
        curTile9.rotate();
        board.placeTile(69, 70, curTile9);
        Tile curTile10 = getTile("E");
        board.placeTile(70, 70, curTile10);
        Tile curTile11 = getTile("B");
        board.placeTile(71, 70, curTile11);
        board.placeMeeple(curTile11, 71, 70, 4, 2, meepleBoard);
        Tile curTile12 = getTile("A");
        board.placeTile(71, 69, curTile12);
        board.placeMeeple(curTile12, 71, 69,4, 2, meepleBoard);
    }
    @Test (expected = IllegalArgumentException.class)
    public void testInvalidMeepleplacementOnMonastery() {
        carcassone = new Carcassonne(players);
        deck = carcassone.getDeck();
        board = carcassone.getBoard();
        Tile curTile1 = getTile("A");
        board.placeTile(70, 71, curTile1);
        board.placeMeeple(curTile1, 70, 71,4, 2, meepleBoard);
        Tile curTile2 = getTile("G");
        board.placeTile(70, 72, curTile2);
        board.placeMeeple(curTile2, 70, 72,0, 2, meepleBoard);
        Tile curTile3 = getTile("I");
        curTile3.rotate();
        board.placeTile(69, 72, curTile3);
        board.placeMeeple(curTile3, 69, 72,3, 2, meepleBoard);
        Tile curTile4 = getTile("O");
        board.placeTile(71, 72, curTile4);
        board.placeMeeple(curTile4, 71, 72,2, 2, meepleBoard);
        Tile curTile5 = getTile("V");
        curTile5.rotate();
        curTile5.rotate();
        board.placeTile(72, 71, curTile5);
        board.placeMeeple(curTile5, 72, 71,0, 2, meepleBoard);
        Tile curTile7 = getTile("L");
        board.placeTile(71, 73, curTile7);
        board.placeMeeple(curTile7, 71, 73,0, 2, meepleBoard);
        Tile curTile6 = getTile("V");
        System.out.println(curTile6);
        curTile6.rotate();
        board.placeTile(72, 72, curTile6);
        board.placeMeeple(curTile6, 72, 72,4, 2, meepleBoard);
    }
    @Test (expected = IllegalArgumentException.class)
    public void testInvalidMeepleplacementOnFeatureAlreadyHaveOne() {
        carcassone = new Carcassonne(players);
        deck = carcassone.getDeck();
        board = carcassone.getBoard();
        Tile curTile1 = getTile("A");
        board.placeTile(70, 71, curTile1);
        board.placeMeeple(curTile1, 70, 71,4, 2, meepleBoard);
        Tile curTile2 = getTile("G");
        board.placeTile(70, 72, curTile2);
        board.placeMeeple(curTile2, 70, 72,0, 2, meepleBoard);
        Tile curTile3 = getTile("I");
        curTile3.rotate();
        board.placeTile(69, 72, curTile3);
        board.placeMeeple(curTile3, 69, 72, 3, 2, meepleBoard);
        Tile curTile4 = getTile("O");
        board.placeTile(71, 72, curTile4);
        board.placeMeeple(curTile4, 71, 72,2, 2, meepleBoard);
        Tile curTile5 = getTile("V");
        curTile5.rotate();
        curTile5.rotate();
        board.placeTile(72, 71, curTile5);
        board.placeMeeple(curTile5, 72, 71,0, 2, meepleBoard);
        Tile curTile7 = getTile("L");
        board.placeTile(71, 73, curTile7);
        board.placeMeeple(curTile7, 71, 73,0, 2, meepleBoard);
        Tile curTile6 = getTile("V");
        System.out.println(curTile6);
        curTile6.rotate();
        board.placeTile(72, 72, curTile6);
        board.placeMeeple(curTile6, 72, 72,3, 2, meepleBoard);
    }


}