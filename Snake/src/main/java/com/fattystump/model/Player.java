package com.fattystump.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    List<Integer> segmentX = new ArrayList<>();
    List<Integer> segmentY = new ArrayList<>();
    boolean isFoodEaten = false;
    private int kamikaze = -1;
    private int id;
    private int direction;
    private int score;
    private boolean updateScore = false;
    private boolean freeze = false;

    public Player(int id) {
        this.id = id;
        segmentX.add(0);
        segmentY.add(0);
    }

    void move() {
        //server commands
        if (freeze) {
            return;
        }
        if (kamikaze > -1) {
            direction = kamikaze;
        }

        //save last segment (to add a new one)
        int lastX = segmentX.get(segmentX.size() - 1);
        int lastY = segmentY.get(segmentY.size() - 1);

        //move segments
        for (int i = segmentX.size() - 1; i > 0; i--) {
            segmentX.set(i, segmentX.get(i - 1));
            segmentY.set(i, segmentY.get(i - 1));
        }
        // move head in direction
        switch (direction) {
            //Right
            case 0:
                segmentX.set(0, segmentX.get(0) + 1);
                break;
            //Up
            case 1:
                segmentY.set(0, segmentY.get(0) - 1);
                break;
            //Left
            case 2:
                segmentX.set(0, segmentX.get(0) - 1);
                break;
            //Down
            case 3:
                segmentY.set(0, segmentY.get(0) + 1);
                break;
        }
        //add segment
        if (isFoodEaten) {
            segmentX.add(lastX);
            segmentY.add(lastY);
            isFoodEaten = false;
        }
    }

    public int getId() {
        return id;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isUpdateScore() {
        return updateScore;
    }

    public void setUpdateScore(boolean updateScore) {
        this.updateScore = updateScore;
    }

    public boolean isFreeze() {
        return freeze;
    }

    public void setFreeze(boolean freeze) {
        this.freeze = freeze;
    }

    public void setKamikaze(int kamikaze) {
        this.kamikaze = kamikaze;
    }
}
