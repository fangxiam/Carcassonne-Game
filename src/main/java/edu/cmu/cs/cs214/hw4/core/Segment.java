package edu.cmu.cs.cs214.hw4.core;

import java.util.List;
import java.util.Set;

/**
 * different segments and score functions
 */
public enum Segment {
    CITY(1) {
        @Override
        boolean isCompleted(Board board, Tile curTile, int segmentIdx) {
            return checkRoadOrCityCompletion(board, curTile, segmentIdx);
        }
        @Override
        int score(Board board, Set<Integer> merged, Set<List<Integer>> allTilesIdx) {
            int score = 0;
            for (List<Integer> idx: allTilesIdx) {
                if(board.getTile(idx.get(0), idx.get(1)).hasShield()) {
                    score++;
                }
            }
            score += allTilesIdx.size();
            score *= Segment.CITY.scoreValue;
            for (Integer featureCode: merged) {
                board.getFeature(featureCode).setIsScored();
            }
            return score;
        }
    },
    ROAD(1){
        @Override
        boolean isCompleted(Board board, Tile curTile, int segmentIdx) {
            return checkRoadOrCityCompletion(board, curTile, segmentIdx);
        }
        @Override
        int score(Board board, Set<Integer> merged, Set<List<Integer>> allTilesIdx) {
            int score = allTilesIdx.size() * Segment.ROAD.scoreValue;
            for (Integer featureCode: merged) {
                board.getFeature(featureCode).setIsScored();
            }
            return score;
        }
    },
    FIELD(0){
        @Override
        boolean isCompleted(Board board, Tile curTile, int segmentIdx) {
            return false;
        }
        @Override
        int score(Board board, Set<Integer> merged, Set<List<Integer>> allTilesIdx) {
            return 0;
        }
    },
    MONASTERY(1){
        @Override
        boolean isCompleted(Board board, Tile curTile, int segmentIdx) {
            int monasterySize = board.getFeature(curTile.getFeatureCode(segmentIdx)).getContainedTiles().size();
            return monasterySize == MONASTERY_SIZE;
        }
        @Override
        int score(Board board, Set<Integer> monaFeature, Set<List<Integer>> allTilesIdx) {
            int score = allTilesIdx.size() * Segment.MONASTERY.scoreValue;
            for (Integer featureCode: monaFeature) {
                board.getFeature(featureCode).setIsScored();
            }

            return score;
        }
    };
    private static final int MONASTERY_SIZE = 9;
    // the score of each tile of different type for final score session
    private int scoreValue;
    Segment(int value){this.scoreValue = value;}

    /**
     * check whether features with specific type is completed, including merged features
     * @param board game board
     * @param curTile current tile
     * @param segmentIdx the segment's edge index you wanna check
     * @return complete or not
     */
    abstract boolean isCompleted(Board board, Tile curTile, int segmentIdx);

    /**
     * score the completed feature(s), including single feature(not merged with other features) and merged features
     * @param board game board
     * @param merged the set of features' code, might include one or more, to be easy usage, I integrate single feature and
     *               merged features into one method by regard single feature as merged one as well, then the set only contains one element.
     * @param allTilesIdx all tiles' coordinates of all tiles of feature(s)
     * @return return the score of current feature(s)
     */
    abstract int score(Board board, Set<Integer> merged, Set<List<Integer>> allTilesIdx);

    /**
     * since in my design, the way to check completion of road and city is the same, to reduce redundant code, I integrate it into
     * one method. I give feature code to four edges respectively, to check whether completed, just make sure that the feature code of all
     * the outer edges(abut with vacant grid) of current feature(s) are different, then it is completed!
     *
     * @param board game board
     * @param curTile current tile
     * @param segmentIdx the segment's edge index you wanna check
     * @return
     */
    boolean checkRoadOrCityCompletion(Board board, Tile curTile, int segmentIdx) {
        Set<Integer> merged = board.mergedSet(curTile, segmentIdx);
        int curSegmentFeatureCode = curTile.getFeatureCode(segmentIdx);
        for (List<Integer> idx: board.getAllTilesIdx(curTile, segmentIdx)) {
            int row = idx.get(0);
            int col = idx.get(1);
            Tile tile = board.getTile(row, col);
            int topFeatureCode = tile.getFeatureCode(Tile.TOP_INDEX);
            int rightFeatureCode = tile.getFeatureCode(Tile.RIGHT_INDEX);
            int downFeatureCode = tile.getFeatureCode(Tile.DOWN_INDEX);
            int leftFeatureCode = tile.getFeatureCode(Tile.LEFT_INDEX);
            if ((row == 0 || (row > 0 && board.getTile(row - 1, col) == null)) &&
                    ((curSegmentFeatureCode == topFeatureCode) || merged.contains(topFeatureCode))) {
                return false;
            }
            if ((col == Board.SIZE - 1 || (col < Board.SIZE - 1 && board.getTile(row, col + 1) == null)) &&
                    ((curSegmentFeatureCode == rightFeatureCode) || merged.contains(rightFeatureCode))) {
                return false;
            }
            if ((row == Board.SIZE || (row < Board.SIZE && board.getTile(row + 1, col) == null)) &&
                    ((curSegmentFeatureCode == downFeatureCode) || merged.contains(downFeatureCode))) {
                return false;
            }
            if ((col == 0 || (col > 0 && board.getTile(row, col - 1) == null)) &&
                    ((curSegmentFeatureCode == leftFeatureCode) || merged.contains(leftFeatureCode))) {
                return false;
            }
        }
        return true;
    }

    /**
     * print the name of segment type
     * @return string of name
     */
    @Override
    public String toString() {
        return this.name();
    }
}