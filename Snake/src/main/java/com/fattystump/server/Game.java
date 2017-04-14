package com.fattystump.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Game {

    public static final int HEIGHT = 36;
    public static final int WIDTH = 64;
    public static final int FRUITS = 3;
    public static final int TICK = 100;

    public int field[][] = new int[WIDTH][HEIGHT];
    public ArrayList<Player> players = new ArrayList<>();
    private Random rnd = new Random();
    public  ArrayList<Integer> fruitsX = new ArrayList<>();
    public  ArrayList<Integer> fruitsY = new ArrayList<>();
    public  ArrayList<Integer> solidX = new ArrayList<>();
    public  ArrayList<Integer> solidY = new ArrayList<>();
    public  int highScore = 0;
    public boolean deadPlayersBecomeSolids = false;

    public Game(){
        for(int i = 0; i < FRUITS; i++){
            fruitsX.add(rnd.nextInt(WIDTH));
            fruitsY.add(rnd.nextInt(HEIGHT));
        }
    }

    public void update(){
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
            for (Player q : players) {
                if (q == null) continue;

                if (p == q) {

                    for (int d = 1; d < q.segmentX.size(); d++) {
                        if (q.segmentX.get(d) == pX && q.segmentY.get(d) == pY) {

                            // dead
                            if (deadPlayersBecomeSolids) playerToSolids(p);
                            players.set(i, null);
                            continue outerLoop;
                        }
                    }
                } else if (p != q && q.segmentX.contains(pX) && q.segmentY.contains(pY)) {

                    // dead
                    if (deadPlayersBecomeSolids) playerToSolids(p);
                    players.set(i, null);
                    continue outerLoop;
                }
            }
        }
    }

    private void checkSolids() {
        outerLoop:
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            if (p == null) continue;

            for (int j = 0; j < solidX.size(); j++) {
                if (p.segmentX.get(0) == solidX.get(j) && p.segmentY.get(0) == solidY.get(j)) {
                    int result = fruitsX.indexOf(solidX.get(j));
                    if (result > -1 && fruitsY.get(result) == solidY.get(j)) {

                        // don't die when fruit overlaps solid
                    } else {
                        // dead
                        if (deadPlayersBecomeSolids) playerToSolids(p);
                        players.set(i, null);
                        continue outerLoop;
                    }
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

                    p.score++;
                    p.updateScore = true;

                }

            }

        }

    }

    private void updateField() {

        field = new int[WIDTH][HEIGHT];

        // solids
        for (int i = 0; i < solidX.size(); i++) {

            // safezone
            if (solidX.get(i) < 3 && solidY.get(i) < 3) {
                solidX.remove(i);
                solidY.remove(i);
                continue;
            }

            field[solidX.get(i)][solidY.get(i)] = -1;
        }

        // fruits
        for (int i = 0; i < FRUITS; i++) {
            field[fruitsX.get(i)][fruitsY.get(i)] = 1;
        }

        // players
        for (Player p : players) {
            if (p == null) continue;
            // segments
            for (int i = 0; i < p.segmentX.size(); i++) {
                field[p.segmentX.get(i)][p.segmentY.get(i)] = p.id;
            }
            // head
            field[p.segmentX.get(0)][p.segmentY.get(0)] = -p.id;
        }
    }

    private void playerToSolids(Player p) {
        for (int i = 0; i < p.segmentX.size(); i++) {
            int x = p.segmentX.get(i);
            int y = p.segmentY.get(i);
            if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
                solidX.add(x);
                solidY.add(y);
            }
        }

    }
}
