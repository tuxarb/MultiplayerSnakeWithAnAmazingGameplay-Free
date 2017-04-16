package com.fattystump.server;

import java.util.ArrayList;

public class Player {

    private int id;
    public int direction = 0;
    public int score = 0;
    public boolean updateScore = false;
    private boolean freeze = false;
    public  boolean isFoodEaten = false;
    private int kamikaze = -1;
    private int steps = 1;
    private int step = 0;
    public String color;
    public ArrayList<Integer> segmentX = new ArrayList<>();
    public ArrayList<Integer> segmentY = new ArrayList<>();

    Player(int id, String color){
        this(id);
        this.color = color;
    }

    Player(int id) {
        this.id = id;
        segmentX.add(0);
        segmentY.add(0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getKamikaze() {
        return kamikaze;
    }

    public void setKamikaze(int kamikaze) {
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public void setSegmentY(ArrayList<Integer> segmentY) {
        this.segmentY = segmentY;
    }

    public void move(){
        //server commands
        if (freeze) return;
        step++;
        if(step >= steps){
            step = 0;
        }
        else{
            return;
        }
        if (kamikaze >= -1) direction = kamikaze;

        //save last segment (to add a new one)
        int lastX = segmentX.get(segmentX.size()-1);
        int lastY = segmentY.get(segmentY.size()-1);

        //move segments
        for(int i = segmentX.size() - 1; i > 0; i--){
            segmentX.set(i,segmentX.get(i-1));
            segmentY.set(i,segmentY.get(i-1));
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
        if (isFoodEaten){
            segmentX.add(lastX);
            segmentY.add(lastY);
            isFoodEaten = false;
        }
    }
}
