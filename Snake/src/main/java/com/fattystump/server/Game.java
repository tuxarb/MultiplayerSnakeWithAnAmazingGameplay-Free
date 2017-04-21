package com.fattystump.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 30;
    static final int TICK = 100;
    private static final int FRUITS = 6;
    private static final int STONES = 4;
    int field[][] = new int[WIDTH][HEIGHT];
    private Random rnd = new Random();
    private List<Player> players = new ArrayList<>();
    private List<Integer> fruitsX = new ArrayList<>();
    private List<Integer> fruitsY = new ArrayList<>();
    private List<Integer> stonesX = new ArrayList<>();
    private List<Integer> stonesY = new ArrayList<>();
    private int highScore;

    Game() {
        for (int i = 0; i < FRUITS; i++) {
            fruitsX.add(getRandom(WIDTH));
            fruitsY.add(getRandom(HEIGHT));
        }
        for (int i = 0; i < STONES; i++) {
            stonesX.add(getRandom(WIDTH));
            stonesY.add(getRandom(HEIGHT));
        }
    }

    private int getRandom(int upperBound) {
        int rand = rnd.nextInt(upperBound - 1);
        if (rand == 0) {
            rand = 1;
        }
        return rand;
    }

    void update() {
        move();
        checkCollisions();
        checkStones();
        checkFruits();
        updateField();
    }

    private void move() {
        for (Player p : players) {
            if (p == null) continue;
            p.move();
        }
    }

    private void checkCollisions() {
        outerLoop:
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            if (p == null) continue;
            //borders
            if (Collections.max(p.segmentX) >= WIDTH ||
                    Collections.max(p.segmentY) >= HEIGHT ||
                    Collections.min(p.segmentX) < 0 ||
                    Collections.min(p.segmentY) < 0) {
                // dead
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
                            players.set(i, null);
                            continue outerLoop;
                        }
                    }
                } else if (q.segmentX.indexOf(pX) == 0 && q.segmentY.indexOf(pY) == 0) {
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

    private void checkFruits() {
        for (Player p : players) {
            if (p == null) continue;
            for (int i = 0; i < FRUITS; i++) {
                int fruitX = fruitsX.get(i);
                int fruitY = fruitsY.get(i);
                if ((p.segmentX.get(0).equals(fruitX) && p.segmentY.get(0).equals(fruitY)) ||
                        (p.segmentX.get(0).equals(fruitX + 1) && p.segmentY.get(0).equals(fruitY))) {
                    fruitsX.set(i, getRandom(WIDTH));
                    fruitsY.set(i, getRandom(HEIGHT));
                    p.setScore(p.getScore() + 1);
                    p.setUpdateScore(true);
                    p.isFoodEaten = true;
                }
            }
        }
    }

    private void checkStones() {
        for (Player p : players) {
            if (p == null) continue;
            for (int i = 0; i < STONES; i++) {
                int stoneX = stonesX.get(i);
                int stoneY = stonesY.get(i);
                if ((p.segmentX.get(0).equals(stoneX) && p.segmentY.get(0).equals(stoneY)) ||
                        (p.segmentX.get(0).equals(stoneX + 1) && p.segmentY.get(0).equals(stoneY))) {
                    stonesX.set(i, getRandom(WIDTH));
                    stonesY.set(i, getRandom(HEIGHT));
                    players.set(players.indexOf(p), null);
                }
            }
        }
    }

    private void updateField() {
        field = new int[WIDTH][HEIGHT];

        // fruits
        for (int i = 0; i < FRUITS; i++) {
            field[fruitsX.get(i)][fruitsY.get(i)] = 1;
            field[fruitsX.get(i) + 1][fruitsY.get(i)] = 1;
        }

        // Stones
        for (int i = 0; i < STONES; i++) {
            field[stonesX.get(i)][stonesY.get(i)] = -1;
            field[stonesX.get(i) + 1][stonesY.get(i)] = -1;
        }
        // players
        for (Player p : players) {
            if (p == null) continue;
            // segments
            for (int i = 1; i < p.segmentX.size(); i++) {
                field[p.segmentX.get(i)][p.segmentY.get(i)] = p.getId();
            }
            // head
            field[p.segmentX.get(0)][p.segmentY.get(0)] = -p.getId();
            field[p.segmentX.get(0) + 1][p.segmentY.get(0)] = -p.getId();
        }
    }

    int getHighScore() {
        return highScore;
    }

    void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    List<Player> getPlayers() {
        return players;
    }
}
