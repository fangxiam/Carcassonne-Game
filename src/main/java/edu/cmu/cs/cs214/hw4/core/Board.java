package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Collections;

/**
 * place tile and update features on the board
 */
public class Board {
    static final int SIZE = 143;
    private static final int CENTER_INDEX = 71;
    private static final int DIRECTION = 4;
    private Tile[][] grid = new Tile[SIZE][SIZE];
    // store list of all features
    private List<Feature> featureList = new ArrayList<>();
    private Set<Set<Integer>> mergedFeatureList = new HashSet<>();
    Board(Tile startTile) {
        placeTile(CENTER_INDEX, CENTER_INDEX, startTile);
    }

    Feature getFeature(int featureCode) {
        return featureList.get(featureCode);
    }

    Tile getTile(int row, int col) {
        return grid[row][col];
    }

    /**
     * get the outer vacancies of current board with occupied tiles
     * @return the list of coordinates of vacancies
     */
    public List<Integer[]> getOuterVacancies() {
        List<Integer[]> outerVacancies = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isOuterVacancy(i, j)) {
                    if (grid[i][j] == null) {
                        outerVacancies.add(new Integer[]{i, j});
                    }
                }
            }
        }
        return outerVacancies;
    }
    boolean isOuterVacancy(int row, int col) {
        return (row - 1 >= 0 && grid[row - 1][col] != null) || (col - 1 >= 0 && grid[row][col - 1] != null)
                || (row + 1 < SIZE && grid[row + 1][col] != null) || (col + 1 < SIZE && grid[row][col + 1] != null);
    }

    /**
     * generate all valid placement and valid rotations, including the same placemnet but different placement, the first two elements of array
     * are row and column, the last one is the direction denoted by the index of initial top edge segment
     * @param outerVacancies the outer vacancies of current board
     * @param curTile current tile
     * @return get the list of all valid placement with all valid rotations
     */
    public List<Integer[]> getLegalPositions(List<Integer[]> outerVacancies, Tile curTile) {
        List<Integer[]> legalPositions = new ArrayList<>();
        for (Integer[] coordinate : outerVacancies) {
            int row = coordinate[0];
            int col = coordinate[1];
            Tile northTile = null, eastTile = null, southTile = null, westTile = null;
            if (row - 1 >= 0 && grid[row - 1][col] != null) {
                northTile = grid[row - 1][col];
            }
            if (col + 1 < SIZE && grid[row][col + 1] != null) {
                eastTile = grid[row][col + 1];
            }
            if (row + 1 < SIZE && grid[row + 1][col] != null) {
                southTile = grid[row + 1][col];
            }
            if (col - 1 >= 0 && grid[row][col - 1] != null) {
                westTile = grid[row][col - 1];
            }
            for (int i = 0; i < DIRECTION; i++) {
                boolean isMatched = true;
                if ((northTile != null && !northTile.getSegments(Tile.DOWN_INDEX).equals(curTile.getSegments(Tile.TOP_INDEX))) ||
                        (eastTile != null && !eastTile.getSegments(Tile.LEFT_INDEX).equals(curTile.getSegments(Tile.RIGHT_INDEX))) ||
                        (southTile != null && !southTile.getSegments(Tile.TOP_INDEX).equals(curTile.getSegments(Tile.DOWN_INDEX))) ||
                        (westTile != null && !westTile.getSegments(Tile.RIGHT_INDEX).equals(curTile.getSegments(Tile.LEFT_INDEX)))) {
                    isMatched = false;
                }
                if (isMatched) {
                    legalPositions.add(new Integer[]{row, col, curTile.getInitialDirection()});
                }
                curTile.rotate();
            }
        }
        if (legalPositions.size() == 0) {
            throw new IllegalArgumentException("no legal positions available!");
        }

        return legalPositions;
    }

    /**
     * choose a placement to place current tile, if it is illegal, will throw exception and the player has to try another time. in this method, it will
     * also update all features including monastery
     * @param row row
     * @param col column
     * @param curTile current tile
     */
    public void placeTile(int row, int col, Tile curTile) {
        boolean isValid = false;
        if (row == CENTER_INDEX && col == CENTER_INDEX) {
            isValid = true;
        } else {
            for (Integer[] pos: getLegalPositions(getOuterVacancies(), curTile)) {
                if (pos[0] == row && pos[1] == col && pos[2] == curTile.getInitialDirection()) {
                    isValid = true;
                    break;
                }
            }
        }
        // add to old features or create new feature
        if (isValid) {
            grid[row][col] = curTile;
            updateFeatures(curTile, row, col);
            // Current tile has monastery   add to existing monasteries by scanning surrounding eight tiles
            updateMonasteryFeature(curTile, row, col);
            //merge features   only joined tile need to do merge
            mergeFeatures(curTile, row, col);

        } else {
            throw new IllegalArgumentException("not valid position or rotation!");
        }
    }
    int[] rowColHelper(int row, int col, int index) {
        int boundCondition = -1;
        int newRow = -1;
        int newCol = -1;
        switch (index) {
            case Tile.TOP_INDEX: boundCondition = row - 1; newRow = row - 1; newCol = col; break;
            case Tile.RIGHT_INDEX: boundCondition = col + 1; newRow = row; newCol = col + 1; break;
            case Tile.DOWN_INDEX: boundCondition = row + 1; newRow = row + 1; newCol = col; break;
            case Tile.LEFT_INDEX: boundCondition = col - 1; newRow = row; newCol = col - 1; break;
            default:break;
        }
        return new int[]{boundCondition, newRow, newCol};
    }

    /**
     * update features, including merge features and create new features, once the segment cannot be added to any other feature,
     * then create a new one
     * @param curTile current tile
     * @param row row
     * @param col column
     */
    void updateFeatures(Tile curTile, int row, int col) {
        for (int i = 0; i < DIRECTION; i++) {
            if (rowColHelper(row, col, i)[1] >= 0 && rowColHelper(row, col, i)[2] < SIZE
                    && !curTile.getSegments(i).equals(Segment.FIELD) && curTile.getFeatureCode(i) == -1) {
                if (grid[rowColHelper(row, col, i)[1]][rowColHelper(row, col, i)[2]] != null) {
                    curTile.setFeatureNum(i, grid[rowColHelper(row, col, i)[1]][rowColHelper(row, col, i)[2]].getFeatureCode(getAbutSegmentIdx(i)));
                    featureList.get(curTile.getFeatureCode(i)).addTile(row, col);
                    if (curTile.getIsJoined()) {
                        for (int j = 0; j < DIRECTION; j++) {
                            if (curTile.getSegments(j) == curTile.getSegments(i)) {
                                curTile.setFeatureNum(j, curTile.getFeatureCode(i));
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < DIRECTION; i++) {
            if (!curTile.getSegments(i).equals(Segment.FIELD) && curTile.getFeatureCode(i) == -1) {
                Feature newFeature = new Feature(curTile.getSegments(i));
                newFeature.addTile(row, col);
                updateFeatureList(newFeature);
                curTile.setFeatureNum(i, newFeature.getFeatureCode());
                if (curTile.getIsJoined()) {
                    for (int j = 0; j < DIRECTION; j++) {
                        if (curTile.getSegments(j) == curTile.getSegments(i)) {
                            curTile.setFeatureNum(j, newFeature.getFeatureCode());
                        }
                    }
                }
            }
        }
    }

    void updateMonasteryFeature(Tile curTile, int row, int col) {
        if (curTile.hasMonastery()) {
            Feature newMonastery = new Feature(Segment.MONASTERY);
            newMonastery.addTile(row, col);
            updateFeatureList(newMonastery);
            curTile.setFeatureNum(Tile.MONASTERY_INDEX, newMonastery.getFeatureCode());
            if (row - 1 >= 0) {
                if (grid[row - 1][col] != null) {
                    newMonastery.addTile(row - 1, col);
                }
                if (col - 1 >= 0 && grid[row - 1][col - 1] != null) {
                    newMonastery.addTile(row - 1, col - 1);
                }
                if (col + 1 < SIZE && grid[row - 1][col + 1] != null) {
                    newMonastery.addTile(row - 1, col + 1);
                }
            }
            if (row + 1 < SIZE) {
                if (grid[row + 1][col] != null) {
                    newMonastery.addTile(row + 1, col);
                }
                if (col - 1 >= 0 && grid[row + 1][col - 1] != null) {
                    newMonastery.addTile(row + 1, col - 1);
                }
                if (col + 1 < SIZE && grid[row + 1][col + 1] != null) {
                    newMonastery.addTile(row + 1, col + 1);
                }
            }
            if (col - 1 >= 0 && grid[row][col - 1] != null) {
                newMonastery.addTile(row, col - 1);
            }
            if (col + 1 < SIZE && grid[row][col + 1] != null) {
                newMonastery.addTile(row, col + 1);
            }

        }

        //add to existing monasteries by scanning surrounding eight tiles
        addToMonastaries(row, col);
    }
    void addToMonastaries(int row, int col) {
        if (row - 1 >= 0) {
                addToMonasteriesHelper(row - 1, col, row, col);
            if (col - 1 >= 0) {
                addToMonasteriesHelper(row - 1, col - 1, row, col);
            }
            if (col + 1 < SIZE) {
                addToMonasteriesHelper(row - 1, col + 1, row, col);
            }
        }
        if (row + 1 < SIZE) {
                addToMonasteriesHelper(row + 1, col, row, col);
            if (col - 1 >= 0) {
                addToMonasteriesHelper(row + 1, col - 1, row, col);
            }
            if (col + 1 < SIZE) {
                addToMonasteriesHelper(row + 1, col + 1, row, col);
            }
        }
        if (col - 1 >= 0) {
            addToMonasteriesHelper(row, col - 1, row, col);
        }
        if (col + 1 < SIZE) {
            addToMonasteriesHelper(row, col + 1, row, col);
        }
    }

    void addToMonasteriesHelper(int row, int col, int curRow, int curCol) {
        if (grid[row][col] != null && grid[row][col].hasMonastery()) {
            Feature existedMon = featureList.get(grid[row][col].getFeatureCode(Tile.MONASTERY_INDEX));
            existedMon.addTile(curRow, curCol);
            if(Segment.MONASTERY.isCompleted(this, grid[row][col], Tile.MONASTERY_INDEX) && existedMon.hasMeepleOn()) {
                existedMon.setMonasteryComp();
            }
        }

    }

    void mergeFeatures(Tile curTile, int row, int col) {
        if (curTile.getIsJoined()) {
            for (int i = 0; i < DIRECTION; i++) {
                Set<Integer> mergedSet = new HashSet<>();
                int boundCondition = rowColHelper(row, col, i)[0];
                int newRow = rowColHelper(row, col, i)[1];
                int newCol = rowColHelper(row, col, i)[2];
                if (boundCondition >= 0 && boundCondition < SIZE && grid[newRow][newCol] != null &&
                        !curTile.getSegments(i).equals(Segment.FIELD) && curTile.getSegments(i) == curTile.getSegments(i) &&
                        grid[newRow][newCol].getFeatureCode(getAbutSegmentIdx(i)) != curTile.getFeatureCode(i)) {
                    mergedSet.add(grid[newRow][newCol].getFeatureCode(getAbutSegmentIdx(i)));
                    mergedSet.add(curTile.getFeatureCode(i));
                }
                Set<Set<Integer>> toRemove = new HashSet<>();

                for(Set<Integer> set : mergedFeatureList) {
                    if (!Collections.disjoint(set, mergedSet)) {
                        mergedSet.addAll(set);
                        toRemove.add(set);
                    }
                }
                mergedFeatureList.removeAll(toRemove);
                if (mergedSet.size() > 1) {
                    mergedFeatureList.add(mergedSet);
                }
            }
        }
    }

    // get abut two segments' index, a helper method
    int getAbutSegmentIdx(int curSegmentIdx) {
        int abutSegmentIdx = curSegmentIdx + 2;
        if (abutSegmentIdx > Tile.LEFT_INDEX) {
            return abutSegmentIdx % DIRECTION;
        }
        return abutSegmentIdx;
    }

    void updateFeatureList(Feature newFeature) {
        featureList.add(newFeature);
        newFeature.setFeatureCode(featureList.size() - 1);
    }

    //check whether meeple left before use it

    /**
     * choose an edge or center of the current tile to place a meeple
     * @param curTile current tile
     * @param row the row of current tile
     * @param col the column of current tile
     * @param segmentIdx four edges index or center index
     * @param playerId the player id of current player
     * @param meepleBoard meeple board of this game
     */
    public void placeMeeple(Tile curTile, int row, int col, int segmentIdx, int playerId, int[] meepleBoard) {
        if (curTile.getSegments(segmentIdx) != Segment.FIELD) {
            if (meepleBoard[playerId] > 0) {
                Set<Integer> merged = mergedSet(curTile, segmentIdx);
                //if current segment has been merged
                if (merged.size() > 1) {
                    for (Integer featureCode: merged) {
                        if (featureList.get(featureCode).hasMeepleOn()) {
                            throw new IllegalArgumentException("cannot put meeple here!");
                        }
                    }
                    featureList.get(curTile.getFeatureCode(segmentIdx)).setMeeple(playerId);
                    featureList.get(curTile.getFeatureCode(segmentIdx)).setMeepleIdx(row, col);
                    meepleBoard[playerId]--;
                } else {
                    if (!featureList.get(curTile.getFeatureCode(segmentIdx)).hasMeepleOn()) {
                        featureList.get(curTile.getFeatureCode(segmentIdx)).setMeeple(playerId);
                        featureList.get(curTile.getFeatureCode(segmentIdx)).setMeepleIdx(row, col);
                        if (featureList.get(curTile.getFeatureCode(segmentIdx)).getType().equals(Segment.MONASTERY) &&
                                Segment.MONASTERY.isCompleted(this, curTile, Tile.MONASTERY_INDEX)) {
                            featureList.get(curTile.getFeatureCode(segmentIdx)).setMonasteryComp();
                        }
                        meepleBoard[playerId]--;
                    } else {
                        throw new IllegalArgumentException("cannot put meeple here!");
                    }
                }
            } else {
                throw new IllegalArgumentException("no meeples left!");
            }
        } else {
            throw new IllegalArgumentException("cannot put meeple on FEILD!");
        }
    }

    // generate the set of features that merged together
    Set<Integer> mergedSet(Tile curTile, int segmentIdx) {
        if (!curTile.getSegments(segmentIdx).equals(Segment.FIELD)) {
            for (Set<Integer> merged : mergedFeatureList) {
                if (merged.contains(curTile.getFeatureCode(segmentIdx))) {
                    return merged;
                }
            }
        }
        return new HashSet<>();
    }

    //get all tiles in current (merged) features
    Set<List<Integer>> getAllMergedTilesIdx(Set<Integer> merged) {
        Set<List<Integer>> allTilesIdx = new HashSet<>();
        for(Integer featureCode: merged) {
            allTilesIdx.addAll(featureList.get(featureCode).getContainedTiles());    //need defensive copy? write a for loop to iterate all
        }
        return allTilesIdx;
    }
    Set<List<Integer>> getAllTilesIdx(Tile curTile, int segmentIdx) {
        Set<Integer> merged = mergedSet(curTile, segmentIdx);
        //if the current tile is merged with other features
        if (merged.size() > 1) {
            return getAllMergedTilesIdx(merged);
        } else {
            return getFeature(curTile.getFeatureCode(segmentIdx)).getContainedTiles();
        }
    }

    /**
     * check whether there are features completed and score them
     * @param curTile the current tile
     * @param scoreBoard current score board
     * @param meepleBoard current meeple board
     * @return positions of all the meeples on current (merged) features
     */
    public Set<List<Integer>> checkAndScore(Tile curTile, int[] scoreBoard, int[] meepleBoard) {
        Set<List<Integer>> meepleIdxOnAllCheckedFeatures = new HashSet<>();
        for (int segmentIdx = 0; segmentIdx < DIRECTION; segmentIdx++) {
            Segment curSegment = curTile.getSegments(segmentIdx);
            if(curSegment.isCompleted(this, curTile, segmentIdx)){
                Set<Integer> merged = mergedSet(curTile, segmentIdx);
                if (merged.size() == 0) {
                    merged = new HashSet<>();
                    merged.add(curTile.getFeatureCode(segmentIdx));
                }
                boolean hasMeeple = false;
                for (int featureCode: merged) {
                    if (featureList.get(featureCode).hasMeepleOn()) {
                        hasMeeple = true;
                        List<Integer> meepleIdxOnCurFeature = new ArrayList<>();
                        meepleIdxOnCurFeature.add(featureList.get(featureCode).getMeepleIdx().get(0));
                        meepleIdxOnCurFeature.add(featureList.get(featureCode).getMeepleIdx().get(1));
                        meepleIdxOnAllCheckedFeatures.add(meepleIdxOnCurFeature);
                    }
                }
                if (hasMeeple) {
                    Set<List<Integer>> allTilesIdx = getAllTilesIdx(curTile, segmentIdx);
                    if (!featureList.get(curTile.getFeatureCode(segmentIdx)).getIsScored()) {
                        int scoreAdded = curSegment.score(this, merged, allTilesIdx);
                        if (curSegment == Segment.CITY) {
                            scoreAdded *= 2;
                        }

                        int[] meeplesOnFeature = getMeeplesOnFeature(merged, meepleBoard);
                        Set<Integer> players = playersGetScore(meeplesOnFeature);
                        for (Integer playerId: players) {
                            scoreBoard[playerId] += scoreAdded;
                        }
                        returnMeeples(meeplesOnFeature, meepleBoard);
                    }
                }

            }
        }
        for(Feature feature: featureList) {
            if (feature.getType() == Segment.MONASTERY && feature.getIsMonasteryComp() && !feature.getIsScored()) {
                Set<Integer> monaFeature = new HashSet<>();
                Set<List<Integer>> allTilesIdx = feature.getContainedTiles();
                monaFeature.add(feature.getFeatureCode());
                int scoreAdded = Segment.MONASTERY.score(this, monaFeature, allTilesIdx);
                scoreBoard[feature.getMeeple()] += scoreAdded;
                meepleBoard[feature.getMeeple()]++;
                List<Integer> meepleIdxOnCurFeature = new ArrayList<>();
                meepleIdxOnCurFeature.add(feature.getMeepleIdx().get(0));
                meepleIdxOnCurFeature.add(feature.getMeepleIdx().get(1));
                meepleIdxOnAllCheckedFeatures.add(meepleIdxOnCurFeature);
            }
        }
        return meepleIdxOnAllCheckedFeatures;
    }

    int[] getMeeplesOnFeature(Set<Integer> merged, int[] meepleBoard) {
        int numOfPlayers = meepleBoard.length;
        int[] meeplesOnFeature = new int[numOfPlayers];
        for (Integer featureCode: merged) {
            if (featureList.get(featureCode).hasMeepleOn()) {
                meeplesOnFeature[featureList.get(featureCode).getMeeple()]++;
            }
        }
        return meeplesOnFeature;

    }

    Set<Integer> playersGetScore(int[] meeplesOnFeature) {
        Set<Integer> players = new HashSet<>();
        int max = -1;
        for (int meeplesNum : meeplesOnFeature) {
            if (meeplesNum> max) {
                max = meeplesNum;
            }
        }
        if (max > 0) {
            for (int i = 0; i < meeplesOnFeature.length; i++) {
                if (meeplesOnFeature[i] == max) {
                    players.add(i);
                }
            }
        }

        return players;
    }

    void returnMeeples(int[] meeplesOnFeature, int[] meepleBoard) {
        for (int i = 0; i < meepleBoard.length; i++) {
            meepleBoard[i] += meeplesOnFeature[i];
        }
    }

    /**
     * when there is no tile left, check and calculate all features that not completed on the board
     * @param scoreBoard current score board
     * @param meepleBoard current meeple board
     */
    public void finalScore(int[] scoreBoard, int[] meepleBoard) {
        for(Set<Integer> merged: mergedFeatureList) {
            Set<List<Integer>> allTilesIdx = getAllMergedTilesIdx(merged);
            Segment curSegment = featureList.get(merged.iterator().next()).getType();
            if (!featureList.get(merged.iterator().next()).getIsScored()) {
                int scoreAdded = curSegment.score(this, merged, allTilesIdx);
                int[] meeplesOnFeature = getMeeplesOnFeature(merged, meepleBoard);
                Set<Integer> players = playersGetScore(meeplesOnFeature);
                for (Integer playerId: players) {
                    scoreBoard[playerId] += scoreAdded;
                }
            }
        }
        //score features that not merge with others
        for(Feature feature: featureList) {
            if(!feature.getIsScored() && feature.hasMeepleOn()) {
                Segment curSegment = feature.getType();
                Set<List<Integer>> allTilesIdx = feature.getContainedTiles();
                Set<Integer> curFeatureCode = new HashSet<>();
                curFeatureCode.add(feature.getFeatureCode());
                int scoreAdded = curSegment.score(this, curFeatureCode, allTilesIdx);
                scoreBoard[feature.getMeeple()] += scoreAdded;
            }
        }
    }

}
