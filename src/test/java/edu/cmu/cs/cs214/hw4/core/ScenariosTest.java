package edu.cmu.cs.cs214.hw4.core;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ScenariosTest {

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
    }
    @Test
    public void testRoadWithLoopCompletion() {
        Tile t1 = getTile("A");
        board.placeTile(70, 71, t1);
        Tile t2 = getTile("V");
        t2.rotate();
        t2.rotate();
        board.placeTile(69, 71, t2);
        Tile t3 = getTile("V");
        t3.rotate();
        board.placeTile(69, 72, t3);
        Tile t4 = getTile("V");
        t4.rotate();
        t4.rotate();
        t4.rotate();
        board.placeTile(68, 71, t4);
        Tile t5 = getTile("V");
        board.placeTile(68, 72, t5);
        System.out.println(t5.getSegments(1));
        assertTrue(t5.getSegments(3).isCompleted(board, t5, 3));
    }

    @Test
    public void testMultiCitySegments() {
        Tile t1 = getTile("A");
        board.placeTile(70, 71, t1);
        board.placeMeeple(t1,70, 71,4,0,meepleBoard);
        Tile t2 = getTile("M");
        t2.rotate();
        t2.rotate();
        board.placeTile(70, 72, t2);
        board.placeMeeple(t2,70, 72,1,1,meepleBoard);
        Tile t3 = getTile("R");
        t3.rotate();
        t3.rotate();
        board.placeTile(70, 73, t3);
        Tile t4 = getTile("B");
        board.placeTile(69, 73, t4);
        board.placeMeeple(t4,69, 73,4,3,meepleBoard);
        Tile t9 = getTile("P");
        t9.rotate();
        t9.rotate();
        board.placeTile(72, 71, t9);
        board.placeMeeple(t9,72, 71,1,0,meepleBoard);
        Tile t6 = getTile("E");
        board.placeTile(73, 71, t6);
        Tile t7 = getTile("H");
        board.placeTile(70, 74, t7);
        board.placeMeeple(t7,70, 74, 1,2,meepleBoard);
        Tile t8 = getTile("Q");
        board.placeTile(71, 73, t8);
        Tile t5 = getTile("R");
        t5.rotate();
        t5.rotate();
        t5.rotate();
        board.placeTile(72, 72, t5);
        Tile t10 = getTile("I");
        t10.rotate();
        board.placeTile(71, 74, t10);
        board.placeMeeple(t10,71, 74,2,1,meepleBoard);
        Tile t11 = getTile("C");
        board.placeTile(71, 72, t11);
        Tile t12 = getTile("E");
        board.placeTile(73, 72, t12);
        assertArrayEquals(new int[]{5,5,6,6}, meepleBoard);
        board.checkAndScore(t12, scoreBoard, meepleBoard);
        assertArrayEquals(new int[]{6,6,6,6}, meepleBoard);
        Tile t13 = getTile("V");
        t13.rotate();
        t13.rotate();
        board.placeTile(69, 71, t13);
        Tile t14 = getTile("V");
        t14.rotate();
        board.placeTile(69, 72, t14);
        Tile t15 = getTile("V");
        t15.rotate();
        t15.rotate();
        t15.rotate();
        board.placeTile(68, 71, t15);
        board.placeMeeple(t15,68, 71,1,2,meepleBoard);
        Tile t16 = getTile("V");
        board.placeTile(68, 72, t16);
        board.checkAndScore(t16, scoreBoard, meepleBoard);
        assertArrayEquals(new int[]{28, 28,4,0}, scoreBoard);
    }
    @Test
    public void testFinalScoreMergedFeatures() {
        Tile t1 = getTile("A");
        board.placeTile(70, 71, t1);
        board.placeMeeple(t1,70, 71,4,0,meepleBoard);
        Tile t2 = getTile("M");
        t2.rotate();
        t2.rotate();
        board.placeTile(70, 72, t2);
        board.placeMeeple(t2,70, 72,1,1,meepleBoard);
        Tile t3 = getTile("R");
        t3.rotate();
        t3.rotate();
        board.placeTile(70, 73, t3);
        Tile t4 = getTile("B");
        board.placeTile(69, 73, t4);
        board.placeMeeple(t4,69, 73,4,3,meepleBoard);
        Tile t9 = getTile("P");
        t9.rotate();
        t9.rotate();
        board.placeTile(72, 71, t9);
        board.placeMeeple(t9,72, 71,1,0,meepleBoard);
        Tile t6 = getTile("E");
        board.placeTile(73, 71, t6);
        Tile t7 = getTile("H");
        board.placeTile(70, 74, t7);
        board.placeMeeple(t7,70, 74,1,2,meepleBoard);
        Tile t8 = getTile("Q");
        board.placeTile(71, 73, t8);
        Tile t5 = getTile("R");
        t5.rotate();
        t5.rotate();
        t5.rotate();
        board.placeTile(72, 72, t5);
        Tile t10 = getTile("I");
        t10.rotate();
        board.placeTile(71, 74, t10);
        board.placeMeeple(t10,71, 74,2,1,meepleBoard);
        Tile t11 = getTile("C");
        board.placeTile(71, 72, t11);
        board.finalScore(scoreBoard, meepleBoard);
        assertArrayEquals(new int[]{17, 14,1,4}, scoreBoard);
    }
    @Test
    public void testAnythingAboutField() {
    }
}
