package com.fattystump.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Game {
    
    static final int HEIGHT = 36;
    static final int WIDTH = 64;
    static final int FRUITS = 3;
    static final int TICK = 100;
    static final int STONES = 3;

    public int field[][] = new int[WIDTH][HEIGHT];
    private ArrayList<Player> players = new ArrayList<>();
    private Random rnd = new Random();

    public  ArrayList<Integer> fruitsX = new ArrayList<>();
    public  ArrayList<Integer> fruitsY = new ArrayList<>();
    public  ArrayList<Integer> solidsX = new ArrayList<>();
    public  ArrayList<Integer> solidsY = new ArrayList<>();
    public ArrayList<Integer> stoneX = new ArrayList<>();
    public ArrayList<Integer> stoneY = new ArrayList<>();
    public  int highScore = 0;

    private boolean deadPlayersBecomeSolids = false;

    public Game(){
        for(int i = 0; i < FRUITS; i++){
            fruitsX.add(rnd.nextInt(WIDTH));
            fruitsY.add(rnd.nextInt(HEIGHT));
        }
        for (int i = 0; i < STONES; i++){
            stoneX.add(rnd.nextInt(WIDTH));
            stoneY.add(rnd.nextInt(HEIGHT));
        }
    }

    int getHighScore() {
        return highScore;
    }

    void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    ArrayList<Player> getPlayers() {
        return players;
    }

    void update(){
        move();
        checkCollisions();
        checkSolids();
        checkFruits();
        updateField();
    }

    private void move(){
        for(Player p : players){
            if (p == null) continue;
            p.move();
        }
    }

    private void checkCollisions(){
        outerLoop:
        for(int i = 0; i < players.size(); i++){
            Player p = players.get(i);
            if (p == null) continue;

            //borders
            if (Collections.max(p.segmentX) >= WIDTH ||
                Collections.max(p.segmentY) >= HEIGHT ||
                    Collections.min(p.segmentX) < 0 ||
                    Collections.min(p.segmentY) < 0) {

                // dead
                if (deadPlayersBecomeSolids) playerToSolids(p);
                players.set(i, null);
                continue;

            }

            // other players
            int pX = p.segmentX.get(0);
            int pY = p.segmentY.get(0);
            for (int j = 0; j < players.size(); j++) {
                Player q = players.get(j);
                if (q == null) continue;

                if (p.equals(q)) {
                    for (int d = 1; d < q.segmentX.size(); d++) {
                        if (q.segmentX.get(d) == pX && q.segmentY.get(d) == pY) {
                            // dead
                            if (deadPlayersBecomeSolids) playerToSolids(p);
                            players.set(i, null);
                            continue outerLoop;

                        }

                    }

                } else if (q.segmentX.indexOf(pX) == 0 && q.segmentY.indexOf(pY) == 0){
                    players.set(i, null);
                    players.set(j, null);
                    continue outerLoop;
                } else if (q.segmentX.contains(pX) && q.segmentY.contains(pY)) {
                    for (int t = q.segmentY.size() - 1; t >= q.segmentX.indexOf(pX); t--)
                        q.segmentY.remove(t);
                    q.segmentX.removeAll(q.segmentX.subList(q.segmentX.indexOf(pX), q.segmentX.size()));
                    players.set(i, null);
                    continue outerLoop;
                }
            }

        }
    }

    private void checkSolids() {

        for (Player p : players) {
            if (p == null) continue;

            for (int i = 0; i < STONES; i++) {

                if (p.segmentX.get(0) == stoneX.get(i) && p.segmentY.get(0) == stoneY.get(i)) {

                    stoneX.set(i, rnd.nextInt(WIDTH));
                    stoneY.set(i, rnd.nextInt(HEIGHT));
                    players.set(players.indexOf(p), null);

                }

            }

        }

    }
    private void checkFruits() {

        for (Player p : players) {
            if (p == null) continue;

            for (int i = 0; i < FRUITS; i++) {

                if (p.segmentX.get(0) == fruitsX.get(i) && p.segmentY.get(0) == fruitsY.get(i)) {

                    fruitsX.set(i, rnd.nextInt(WIDTH));
                    fruitsY.set(i, rnd.nextInt(HEIGHT));

                    p.setScore(p.getScore() + 1);
                    p.setUpdateScore(true);

                }

            }

        }

    }

    private void updateField() {

        field = new int[WIDTH][HEIGHT];

        // solids
        for (int i = 0; i < solidsX.size(); i++) {

            // safezone
            if (solidsX.get(i) < 3 && solidsY.get(i) < 3) {
                solidsX.remove(i);
                solidsY.remove(i);
                continue;
            }

            field[solidsX.get(i)][solidsY.get(i)] = -1;
        }

        // fruits
        for (int i = 0; i < FRUITS; i++) {
            field[fruitsX.get(i)][fruitsY.get(i)] = 1;
        }

        // Stones
        for (int i = 0; i < STONES; i++) {
            field[stoneX.get(i)][stoneY.get(i)] = Integer.MAX_VALUE;
        }
        // players
        for (Player p : players) {
            if (p == null) continue;
            // segments
            for (int i = 0; i < p.segmentX.size(); i++) {
                field[p.segmentX.get(i)][p.segmentY.get(i)] = p.getId();
            }
            // head
            field[p.segmentX.get(0)][p.segmentY.get(0)] = -p.getId();
        }
    }

    private void playerToSolids(Player p) {
        for (int i = 0; i < p.segmentX.size(); i++) {
            int x = p.segmentX.get(i);
            int y = p.segmentY.get(i);
            if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
                solidsX.add(x);
                solidsY.add(y);
            }
        }

    }
}
