package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.List;

/**
 * define each tile
 */
public class Tile {
    public static final int TOP_INDEX = 0;
    public static final int RIGHT_INDEX= 1;
    public static final int DOWN_INDEX = 2;
    public static final int LEFT_INDEX = 3;
    public static final int MONASTERY_INDEX = 4;
    private String code;
    private List<Segment> segments;
    private int initialDirection = 0;
    private boolean hasMonastery;
    private boolean isArmed;
    private boolean isJoined;
    private int[] featureCodeList = new int[]{-1, -1, -1, -1, -1};
    void setFeatureNum(int segmentIdx, int featureCode) {
        featureCodeList[segmentIdx] = featureCode;
    }
    int getFeatureCode(int segmentIdx) {
        return featureCodeList[segmentIdx];
    }

    /**
     * get the letter code of a tile
     * @return the letter code, a string
     */
    public String getCode() {
        return code;
    }
    boolean hasShield() {
        return isArmed;
    }
    boolean getIsJoined() {
        return isJoined;
    }

    /**
     * construtor of Tile
     * @param code the letter of each kind of tile
     * @param segments use a list to store four edge's segment (start from the top edge) and central monastery
     * @param hasMonastery whether has a monastery
     * @param isArmed whether has a shield
     * @param isJoined whether has separate feature of the same type of segment. For example: tile H, I, L, W, X
     */
    Tile(String code, String[] segments, boolean hasMonastery, boolean isArmed, boolean isJoined) {
        this.code = code;
        this.hasMonastery = hasMonastery;
        this.isArmed = isArmed;
        this.isJoined = isJoined;
        this.segments = new ArrayList<>();
        for (String segment : segments) {
            this.segments.add(Segment.valueOf(segment));
        }
    }
    boolean hasMonastery() {
        return hasMonastery;
    }
    Segment getSegments(int i) {
        if (i == MONASTERY_INDEX ) {
            if (hasMonastery) {
                return Segment.MONASTERY;
            } else {
                throw new IllegalArgumentException("This tile doesn't have Monastery segment!");
            }
        }
        return segments.get(i);
    }

    /**
     * rotate tile by clockwise, central segment not change, move the first segment to the tail in list means finishing one rotation,
     * initial direction means the original top edge's index after making a rotation
     */
    public void rotate() {
        if (initialDirection < LEFT_INDEX) {
            initialDirection++;
        } else {
            initialDirection = 0;
        }
        segments.add(0,segments.remove(segments.size() - 1));
    }
    int getInitialDirection() {
        return initialDirection;
    }

    /**
     * print the letter of tile
     * @return the letter of tile
     */
    @Override
    public String toString() {
        return code;
    }

    /**
     * if the letters are same, means tiles equal
     * @param o another tile
     * @return same or not
     */
    @Override
    public boolean equals(Object o){
        if(!(o instanceof Tile)){
            return false;
        }
        Tile another = (Tile)o;
        return ((code.equals(another.getCode())));
    }

    /**
     * hashCode of tile
     * @return
     */
    @Override
    public int hashCode(){
        return code.hashCode();
    }
}
