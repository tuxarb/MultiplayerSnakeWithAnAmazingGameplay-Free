package com.fattystump.client;


import com.fattystump.server.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

class Field extends JPanel {
    private String info;
    private int[][] field;
    private int highscore;
    private int clientId;
    private final Color CURRENT_SNAKE_COLOR;
    private static final int WIDTH = Game.WIDTH;
    private static final int HEIGHT = Game.HEIGHT;
    private final static int SEGMENT_SIZE = 20;
    private static BufferedImage backgroundImage = null;
    private static BufferedImage solidImage = null;
    private static BufferedImage foodImage = null;
    static String SCORE_INFO = "Ваши очки=%d\nРекорд=%d";

    static {
        try {
            backgroundImage = ImageIO.read(Field.class.getResourceAsStream("/images/background.jpg"));
            solidImage = ImageIO.read(Field.class.getResourceAsStream("/images/solid.jpg"));
            foodImage = ImageIO.read(Field.class.getResourceAsStream("/images/food.jpg"));
        } catch (IOException ignored) {
        }
    }

    Field() {
        setPreferredSize(new Dimension(WIDTH * SEGMENT_SIZE, HEIGHT * SEGMENT_SIZE));
        CURRENT_SNAKE_COLOR = Color.RED;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (field != null) {
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    if (field[x][y] == 1) {
                        if (foodImage != null) {
                            g.drawImage(foodImage, x * SEGMENT_SIZE, y * SEGMENT_SIZE, SEGMENT_SIZE, SEGMENT_SIZE, this);
                            continue;
                        } else {
                            g.setColor(Color.GREEN);
                        }
                    } else if (field[x][y] == -1) {
                        if (solidImage != null) {
                            g.drawImage(solidImage, x * SEGMENT_SIZE, y * SEGMENT_SIZE, SEGMENT_SIZE, SEGMENT_SIZE, this);
                            continue;
                        } else {
                            g.setColor(Color.GRAY);
                        }
                    } else if (field[x][y] == clientId && clientId != 0) {   // туловище
                        g.setColor(CURRENT_SNAKE_COLOR.darker());
                    } else if ((field[x][y] == -clientId || field[x][y] == -Integer.MAX_VALUE) && clientId != 0) {  // голова
                        g.setColor(CURRENT_SNAKE_COLOR.brighter());
                    } else if (field[x][y] > 1) {           // туловища других змеек
                        g.setColor(Color.BLACK.darker());
                    } else if (field[x][y] < -1) {          // головы других
                        g.setColor(new Color(10, 40, 10));
                    } else {
                        if (backgroundImage != null) {
                            g.drawImage(backgroundImage, x * SEGMENT_SIZE, y * SEGMENT_SIZE, SEGMENT_SIZE, SEGMENT_SIZE, this);
                            continue;
                        } else {
                            g.setColor(Color.WHITE);
                        }
                    }
                    g.fillRect(x * SEGMENT_SIZE, y * SEGMENT_SIZE, SEGMENT_SIZE, SEGMENT_SIZE);
                }
            }
        }
        g.setColor(new Color(255, 185, 200));
        g.setFont(new Font("Century Gothic", 4, 22));
        g.drawString(info.substring(0, info.indexOf("\n")), getWidth() / 2 - 100, getHeight() - 25);
        g.drawString(info.substring(info.indexOf("\n") + 1, info.length()), getWidth() / 2 - 100, getHeight() - 5);
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

    void setClientId(int clientId) {
        this.clientId = clientId;
    }
}
