package edu.cmu.cs.cs214.hw4.core;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FeatureTest {

    Carcassonne carcassonne;
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
    Tile curTile1;
    Tile curTile2;
    Tile curTile3;
    Tile curTile4;
    Tile curTile5;
    Tile curTile6;
    Tile curTile7;
    Tile curTile8;
    Tile curTile9;
    Tile curTile10;
    Tile curTile11;
    List<String> players = new ArrayList<>();
    @Before
    public void setUp() throws Exception {
        players.add("Mike");
        players.add("Jerry");
        players.add("Tom");
        players.add("Freya");
        carcassonne = new Carcassonne(players);
        deck = carcassonne.getDeck();
        board = carcassonne.getBoard();
        curTile1 = getTile("A");
        board.placeTile(70, 71, curTile1);
        board.placeMeeple(curTile1, 70, 71,4, 2, meepleBoard);
        curTile2 = getTile("G");
        board.placeTile(70, 72, curTile2);
        board.placeMeeple(curTile2, 70, 72,0, 1, meepleBoard);
        curTile3 = getTile("I");
        curTile3.rotate();
        board.placeTile(69, 72, curTile3);
        board.placeMeeple(curTile3, 69, 72,3, 2, meepleBoard);
        curTile4 = getTile("O");
        board.placeTile(71, 72, curTile4);
        board.placeMeeple(curTile4, 71, 72,2, 3, meepleBoard);
        curTile5 = getTile("V");
        curTile5.rotate();
        curTile5.rotate();
        board.placeTile(72, 71, curTile5);
        board.placeMeeple(curTile5, 72, 71,0, 0, meepleBoard);
        curTile7 = getTile("L");
        board.placeTile(71, 73, curTile7);
        board.placeMeeple(curTile7, 71, 73,0, 1, meepleBoard);
        curTile6 = getTile("V");
        System.out.println(curTile6);
        curTile6.rotate();
        board.placeTile(72, 72, curTile6);
        curTile8 = getTile("F");
        board.placeTile(69, 71, curTile8);
        curTile9 = getTile("N");
        curTile9.rotate();
        curTile9.rotate();
        board.placeTile(69, 70, curTile9);
        curTile10 = getTile("E");
        board.placeTile(70, 70, curTile10);
        curTile11 = getTile("B");
        board.placeTile(71, 70, curTile11);
        board.placeMeeple(curTile11, 71, 70,4, 0, meepleBoard);
    }


    @Test
    public void checkRoadOrCityCompletion() {
        assertTrue(curTile4.getSegments(0).isCompleted(board, curTile4, 0));
        assertTrue(curTile6.getSegments(0).isCompleted(board, curTile6, 0));
    }
    @Test
    public void testIntersectionGenerateNewFeature() {
        assertNotEquals(curTile7.getFeatureCode(0), curTile7.getFeatureCode(3));
    }

    @Test
    public void TestcheckAndScore() {
        board.checkAndScore(curTile4, scoreBoard, meepleBoard);
        board.checkAndScore(curTile7, scoreBoard, meepleBoard);
        board.checkAndScore(curTile10, scoreBoard, meepleBoard);
        board.checkAndScore(curTile11, scoreBoard, meepleBoard);
        assertArrayEquals(new int[]{6, 10, 19, 6}, scoreBoard);
    }
    @Test
    public void TestReturnMeeple() {
        board.checkAndScore(curTile4, scoreBoard, meepleBoard);
        board.checkAndScore(curTile7, scoreBoard, meepleBoard);
        board.checkAndScore(curTile10, scoreBoard, meepleBoard);
        board.checkAndScore(curTile11, scoreBoard, meepleBoard);
        assertArrayEquals(new int[]{6, 6, 7, 7}, meepleBoard);
    }
    @Test
    public void TestFinalScore() {
        board.checkAndScore(curTile4, scoreBoard, meepleBoard);
        board.checkAndScore(curTile7, scoreBoard, meepleBoard);
        board.checkAndScore(curTile10, scoreBoard, meepleBoard);
        board.checkAndScore(curTile11, scoreBoard, meepleBoard);
        board.finalScore(scoreBoard, meepleBoard);
        assertArrayEquals(new int[]{11, 11, 19, 6}, scoreBoard);
    }
    @Test
    public void TestSegmentToString() {
        assertEquals("FIELD", Segment.FIELD.toString());
    }
}