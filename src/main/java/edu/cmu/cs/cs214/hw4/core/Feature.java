package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * define a feature
 */
public class Feature {
    private Segment type;
    private int meeple = -1;
    private boolean isScored = false;
    private boolean isMonasteryComp = false;
    private List<Integer> meepleIdx;
    private int featureCode;

    Feature(Segment type) {
        this.type = type;
    }
    void setMeepleIdx(int row, int col) {
        meepleIdx = new ArrayList<>();
        meepleIdx.add(row);
        meepleIdx.add(col);
    }
    List<Integer> getMeepleIdx() {
        List<Integer> meepleIdxCopy = new ArrayList<>();
        meepleIdxCopy.add(meepleIdx.get(0));
        meepleIdxCopy.add(meepleIdx.get(1));
        return meepleIdxCopy;
    }

    /**
     * if the monastery is completed once a tile placed, mark it
     */
    void setMonasteryComp() {
        isMonasteryComp = true;
    }
    boolean getIsMonasteryComp() {
        return isMonasteryComp;
    }

    /**
     * if it is scored, mark it
     * @return true if it is scored
     */
    boolean getIsScored() {
        return isScored;
    }
    void setIsScored() {
        this.isScored = true;
    }

    /**
     * get meeple's value on current feature, which also denotes the player id
     * @return return the value of meeple
     */
    int getMeeple() {
        return meeple;
    }
    boolean hasMeepleOn() {
        return meeple != -1;
    }
    private Set<List<Integer>> containedTiles = new HashSet<>();
    Set<List<Integer>> getContainedTiles() {
        Set<List<Integer>> tiles = new HashSet<>();
        for (List<Integer> idx: containedTiles) {
            List<Integer> idxCopy = new ArrayList<Integer>();
            idxCopy.add(idx.get(0));
            idxCopy.add(idx.get(1));
            tiles.add(idxCopy);
        }
        return tiles;
    }

    void addTile(int row, int col){
        List<Integer> idx = new ArrayList<Integer>();
        idx.add(row);
        idx.add(col);
        containedTiles.add(idx);
    }

    /**
     * each time create a new feature, give it a feature code to distinguish it
     * @param featureCode a number to define and call a feature
     */
    void setFeatureCode(int featureCode) {
        this.featureCode = featureCode;
    }
    int getFeatureCode() {
        return featureCode;
    }
    Segment getType() {
        return type;
    }

    /**
     * put meeple on a feature, the value is the player id
     * @param playerId the value of meeple
     */
    void setMeeple(int playerId) {
        meeple = playerId;
    }
}
