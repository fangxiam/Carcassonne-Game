package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * a pile of tiles
 */

public class Deck {
    private List<Tile> deck;
    private Tile startTile;

    Deck () {
        parseTiles();
    }

    /**
     * get the number of tiles left
     * @return the number of tiles left
     */
    public int getNumberOfTiles () {
        return deck.size();
    }
    boolean hasTilesLeft() {
        return deck.size() > 0;
    }
    Tile getStartTile() {
        return startTile;
    }


    /**
     * draw a tile randomly from the deck
     * @return the new tile
     */
    public Tile drawTile() {
        if (!hasTilesLeft()) {
            throw new IllegalArgumentException("No tiles left, the game is over!");
        }
        Random rand = new Random();
        int random = rand.nextInt(deck.size());
        Tile curTile = deck.get(random);
        deck.remove(random);
        return curTile;
    }
    private void parseTiles() {
        JSONConfigReader.JSONDeck jsonDeck = JSONConfigReader.parse("src/main/resources/Config.json");
        deck = new ArrayList<>();
        for (int i = 0; i < jsonDeck.tiles.length; i++) {
            JSONConfigReader.JSONTile jsonTile = jsonDeck.tiles[i];
            int sameTileCount = jsonTile.count;
            if (jsonTile.code.equals("D")) {
                startTile = new Tile(jsonTile.code, jsonTile.segments, jsonTile.hasMonastery, jsonTile.isArmed, jsonTile.isJoined);
                sameTileCount--;
            }

            for (int j = 0; j < sameTileCount; j++) {
                Tile tile = new Tile(jsonTile.code, jsonTile.segments, jsonTile.hasMonastery, jsonTile.isArmed, jsonTile.isJoined);
                deck.add(tile);
            }
        }
    }
}
