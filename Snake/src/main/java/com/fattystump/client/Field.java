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
    private int clientId;
    private Map<Integer, Color> otherSnakesColors = new HashMap<>();
    private final Color CURRENT_SNAKE_COLOR;
    private final int WIDTH;
    private final int HEIGHT;
    private final static int SEGMENT_SIZE = 25;
    static String SCORE_INFO = "Ваши очки=%d\nРекорд=%d";

    Field(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        Random rnd = new Random();
        float hue = rnd.nextFloat();
        float sat = (rnd.nextInt(4000) + 2300) / 10000f;
        float lum = .5f;
        CURRENT_SNAKE_COLOR = Color.getHSBColor(hue, sat, lum);
        WIDTH = (int) (width / SEGMENT_SIZE + 0.09f);
        HEIGHT = (int) (height / SEGMENT_SIZE + 0.09f);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (field != null) {
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    if (field[x][y] == 1) {                 // еда
                        g.setColor(Color.GREEN);
                    } else if (field[x][y] == -1) {         // границы
                        g.setColor(Color.GRAY);
                    } else if (field[x][y] == clientId) {           // туловище
                        g.setColor(CURRENT_SNAKE_COLOR);
                    } else if (field[x][y] == -clientId) {        // голова
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
        }
        g.setColor(Color.BLACK);
        g.setFont(new Font("Verdana", Font.BOLD, 16));
        g.drawString(info.substring(0, info.indexOf("\n")), getWidth() / 2 - 50, getHeight() - 25);
        g.drawString(info.substring(info.indexOf("\n") + 1, info.length()), getWidth() / 2 - 50, getHeight() - 5);
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

    void setClientId(int clientId) {
        this.clientId = clientId;
    }
}
