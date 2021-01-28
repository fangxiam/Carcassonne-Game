package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.List;

/**
 * game controller
 */
public class Carcassonne {
    private static final int MAX_PLAYERS = 5;
    private List<String> players;
    private int curPlayerId;
    private static final int TOTAL_MEEPLES = 7;
    private int[] meepleBoard;
    private int[] scoreBoard;
    private int numOfPlayers;
    private Deck deck;
    private Board board;

    /**
     * carcassonne game constructor by a list of player's names
     * @param players the list of player's names
     */
    public Carcassonne(List<String> players) {
        numOfPlayers = players.size();
        this.players = players;
        if (numOfPlayers < 2 || numOfPlayers > MAX_PLAYERS) {
            throw new IllegalArgumentException("it requires more than two players or less than five players!");
        }
        curPlayerId = 0;
        scoreBoard = new int[numOfPlayers];
        meepleBoard = new int[numOfPlayers];
        for(int i = 0; i < numOfPlayers; i++) {
            scoreBoard[i] = 0;
            meepleBoard[i] = TOTAL_MEEPLES;
        }
        deck = new Deck();
        board = new Board(deck.getStartTile());
    }

    /**
     * get a player's name by player id
     * @param playerId player id
     * @return the player's name, a string
     */
    public String getPlayer(int playerId) {
        return players.get(playerId);
    }

    /**
     * get current player's id
     * @return the id of current player
     */
    public int getCurPlayerId() {
        return curPlayerId;
    }

    /**
     * switch to next player
     */
    public void switchPlayer() {
        curPlayerId =  (curPlayerId + 1) % numOfPlayers;
    }


    /**
     * get current score of a player
     * @param playerId the id of player
     * @return the score of the player
     */
    public int getScore(int playerId) {
        return scoreBoard[playerId];
    }

    /**
     * update the score of a player
     * @param score the score value
     * @param playerId player id
     */
    public void setScore(int score, int playerId) {
        scoreBoard[playerId] = score;
    }

    /**
     * get meeples left of a player
     * @param playerId the id of the player
     * @return the number of meeples left of the player
     */
    public int getMeepleNum(int playerId) {
        return meepleBoard[playerId];
    }

    /**
     * change the number of meeples of a player
     * @param num the number of meeples
     * @param playerId the player id
     */
    public void setMeepleNum(int num, int playerId) {
        meepleBoard[playerId] = num;
    }

    /**
     * get the total number of players
     * @return the total number of players
     */
    public int getNumOfPlayers() {
        return numOfPlayers;
    }


    /**
     * get the deck of current game
     * @return current deck
     */
    public Deck getDeck() {
        return deck;
    }


    /**
     * get the board of current game
     * @return the current board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * get winners of game, who have highest score
     * @return the list of player's id
     */
    public List<Integer> getWinners() {
        List<Integer> winners = new ArrayList<>();
        int max = -1;
        for(int i = 0; i < numOfPlayers; i++) {
            if (scoreBoard[i] >=  max) {
                max = scoreBoard[i];
            }
        }
        for (int i = 0; i < numOfPlayers; i++) {
            if (scoreBoard[i] == max) {
                winners.add(i);
            }
        }
        return winners;
    }
}


