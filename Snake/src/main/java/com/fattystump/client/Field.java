package com.fattystump.client;


import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

class Field extends JPanel {
    private String info;
    private int[][] field;
    private int highscore;
    private Map<Integer, Color> otherSnakesColors = new HashMap<>();
    private final Color CURRENT_SNAKE_COLOR;
    private final int WIDTH = (int) (getWidth() / SEGMENT_SIZE + 0.09f);
    private final int HEIGHT = (int) (getHeight() / SEGMENT_SIZE + 0.09f);
    private final static int SEGMENT_SIZE = 25;
    static String SCORE_INFO = "Ваши очки=%d\nВаш рекорд=%d";

    Field(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        Random rnd = new Random();
        float hue = rnd.nextFloat();
        float sat = (rnd.nextInt(4000) + 3000) / 10000f;
        float lum = .7f;
        CURRENT_SNAKE_COLOR = Color.getHSBColor(hue, sat, lum);
    }

    void repaint(int id) {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (field[x][y] == 1) {                 // еда
                    g.setColor(Color.GREEN);
                } else if (field[x][y] == -1) {         // границы
                    g.setColor(Color.GRAY);
                } else if (field[x][y] == 0) {           // туловище
                    g.setColor(CURRENT_SNAKE_COLOR);
                } else if (field[x][y] == -0) {        // голова
                    g.setColor(CURRENT_SNAKE_COLOR.brighter());
                } else if (field[x][y] > 1) {           // туловища других змеек
                    g.setColor(otherSnakesColors.get(field[x][y]));
                } else if (field[x][y] < -1) {          // головы других
                    g.setColor(otherSnakesColors.get(-field[x][y]).brighter());
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(x * SEGMENT_SIZE, y * SEGMENT_SIZE, SEGMENT_SIZE, SEGMENT_SIZE);
            }
        }
        g.setColor(Color.BLACK);
        g.setFont(new Font("Verdana", Font.BOLD, 16));
        g.drawString(info.substring(0, 2), getWidth() / 2 - 50, getHeight() - 20);
        g.drawString(info, getWidth() / 2 - 50, getHeight() - 20);
    }

    String getInfo() {
        return info;
    }

    void setInfo(String info) {
        this.info = info;
    }

    void setField(int[][] field) {
        this.field = field;
    }

    int getHighscore() {
        return highscore;
    }

    void setHighscore(int highscore) {
        this.highscore = highscore;
    }

    Map<Integer, Color> getOtherSnakesColors() {
        return otherSnakesColors;
    }
}
