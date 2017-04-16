package com.fattystump.server;

import java.util.ArrayList;

class Player {
    ArrayList<Integer> segmentX = new ArrayList<>();
    ArrayList<Integer> segmentY = new ArrayList<>();
    boolean isFoodEaten = false;
    private int kamikaze = -1;
    private int steps = 1;
    private int step = 0;
    private int id;
    private int direction = -1;
    private int score = 0;
    private boolean updateScore = false;
    private boolean freeze = false;

    Player(int id) {
        this.id = id;
        segmentX.add(0);
        segmentY.add(0);
    }

    void move() {
        //server commands
        if (freeze) {
            return;
        }
        step++;
        if (step >= steps) {
            step = 0;
        } else {
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
        System.out.println(direction);
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

    int getId() {
        return id;
    }

    int getDirection() {
        return direction;
    }

    void setDirection(int direction) {
        this.direction = direction;
    }

    int getScore() {
        return score;
    }

    void setScore(int score) {
        this.score = score;
    }

    boolean isUpdateScore() {
        return updateScore;
    }

    void setUpdateScore(boolean updateScore) {
        this.updateScore = updateScore;
    }

    boolean isFreeze() {
        return freeze;
    }

    void setFreeze(boolean freeze) {
        this.freeze = freeze;
    }

    int getKamikaze() {
        return kamikaze;
    }

    void setKamikaze(int kamikaze) {
        this.kamikaze = kamikaze;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public ArrayList<Integer> getSegmentX() {
        return segmentX;
    }

    public void setSegmentX(ArrayList<Integer> segmentX) {
        this.segmentX = segmentX;
    }

    public ArrayList<Integer> getSegmentY() {
        return segmentY;
    }
}
