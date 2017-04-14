package com.fattystump.server;

import java.util.ArrayList;

public class Player {

    public int id;
    public int direction = 0;
    public int score = 0;
    public boolean updateScore = false;
    public boolean freeze = false;
    public int kamikaze = -1;
    public int steps = 1;
    public int step = 0;
    public String color;
    public ArrayList<Integer> segmentX = new ArrayList<>();
    public ArrayList<Integer> segmentY = new ArrayList<>();

    public Player(int id, String color){
        this.id = id;
        this.color = color;
        segmentX.add(0);
        segmentY.add(0);
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
        if (score >= segmentX.size()){
            segmentX.add(lastX);
            segmentY.add(lastY);
        }
    }
}
