package com.fattystump.client;


import com.fattystump.model.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class Field extends JPanel {
    private String info;
    private int[][] field;
    private int highscore;
    private int clientId;
    private int direction;
    private final Color CURRENT_SNAKE_COLOR;
    private static final int WIDTH = Game.WIDTH;
    private static final int HEIGHT = Game.HEIGHT;
    private final static int SEGMENT_SIZE = 20;
    private int rivalHeadPrevPositionX;
    private int rivalHeadPrevPositionY;
    private Image rivalPrevHeadImage = rivalSnakeHeadRightImage;
    private static BufferedImage backgroundImage = null;
    private static BufferedImage stoneImage = null;
    private static BufferedImage foodImage = null;
    private static BufferedImage snakeHeadLeftImage = null;
    private static BufferedImage snakeHeadRightImage = null;
    private static BufferedImage snakeHeadDownImage = null;
    private static BufferedImage snakeHeadUpImage = null;
    private static BufferedImage snakeBodyImage = null;
    private static BufferedImage rivalSnakeHeadLeftImage = null;
    private static BufferedImage rivalSnakeHeadRightImage = null;
    private static BufferedImage rivalSnakeHeadDownImage = null;
    private static BufferedImage rivalSnakeHeadUpImage = null;
    private static BufferedImage rivalSnakeBodyImage = null;
    static String SCORE_INFO = "Ваши очки=%d\nРекорд=%d";

    static {
        try {
            backgroundImage = ImageIO.read(Field.class.getResourceAsStream("/images/background.jpg"));
            stoneImage = ImageIO.read(Field.class.getResourceAsStream("/images/stone.jpg"));
            foodImage = ImageIO.read(Field.class.getResourceAsStream("/images/food.png"));
            snakeHeadLeftImage = ImageIO.read(Field.class.getResourceAsStream("/images/snake_head_left.png"));
            snakeHeadRightImage = ImageIO.read(Field.class.getResourceAsStream("/images/snake_head_right.png"));
            snakeHeadUpImage = ImageIO.read(Field.class.getResourceAsStream("/images/snake_head_up.png"));
            snakeHeadDownImage = ImageIO.read(Field.class.getResourceAsStream("/images/snake_head_down.png"));
            rivalSnakeHeadLeftImage = ImageIO.read(Field.class.getResourceAsStream("/images/rival_snake_head_left.png"));
            rivalSnakeHeadRightImage = ImageIO.read(Field.class.getResourceAsStream("/images/rival_snake_head_right.png"));
            rivalSnakeHeadUpImage = ImageIO.read(Field.class.getResourceAsStream("/images/rival_snake_head_up.png"));
            rivalSnakeHeadDownImage = ImageIO.read(Field.class.getResourceAsStream("/images/rival_snake_head_down.png"));
            snakeBodyImage = ImageIO.read(Field.class.getResourceAsStream("/images/snake_body.png"));
            rivalSnakeBodyImage = ImageIO.read(Field.class.getResourceAsStream("/images/rival_snake_body.png"));
        } catch (Exception ignored) {
        }
    }

    Field() {
        setPreferredSize(new Dimension(WIDTH * SEGMENT_SIZE, HEIGHT * SEGMENT_SIZE));
        CURRENT_SNAKE_COLOR = Color.RED;
    }

    @Override
    protected void paintComponent(Graphics g) {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(backgroundImage, 0, 0, WIDTH * SEGMENT_SIZE, HEIGHT * SEGMENT_SIZE, this);
        if (field != null) {
            for (int y = 0; y < HEIGHT; y++) {
                boolean isTheNextObjectDrawn = true;
                for (int x = 0; x < WIDTH; x++) {
                    if (field[x][y] == 1) {
                        if (foodImage == null) {
                            g.setColor(Color.GREEN);
                        } else {
                            if (!isTheNextObjectDrawn) {
                                isTheNextObjectDrawn = true;
                                continue;
                            }
                            g.drawImage(foodImage, x * SEGMENT_SIZE, y * SEGMENT_SIZE, 2 * SEGMENT_SIZE, SEGMENT_SIZE, this);
                            isTheNextObjectDrawn = false;
                            continue;
                        }
                    } else if (field[x][y] == -1) {
                        if (stoneImage == null) {
                            g.setColor(Color.GRAY);
                        } else {
                            if (!isTheNextObjectDrawn) {
                                isTheNextObjectDrawn = true;
                                continue;
                            }
                            g.drawImage(stoneImage, x * SEGMENT_SIZE, y * SEGMENT_SIZE, 2 * SEGMENT_SIZE, SEGMENT_SIZE, this);
                            isTheNextObjectDrawn = false;
                            continue;
                        }
                    } else if (field[x][y] == clientId && clientId != 0) {   // туловище
                        if (snakeBodyImage == null) {
                            g.setColor(CURRENT_SNAKE_COLOR.darker());
                        } else {
                            g.drawImage(snakeBodyImage, x * SEGMENT_SIZE, y * SEGMENT_SIZE, SEGMENT_SIZE, SEGMENT_SIZE, this);
                            continue;
                        }
                    } else if (field[x][y] == -clientId && clientId != 0 || clientId == 0 && field[x][y] == -Integer.MAX_VALUE) { // голова
                        if (snakeHeadRightImage == null) {
                            g.setColor(CURRENT_SNAKE_COLOR.brighter());
                        } else {
                            if (!isTheNextObjectDrawn) {
                                isTheNextObjectDrawn = true;
                                continue;
                            }
                            Image image = getImageDependingOnDirection();
                            int headX = x * SEGMENT_SIZE;
                            if (direction == 1 || direction == 3) {
                                headX = x * SEGMENT_SIZE - SEGMENT_SIZE / 2;
                            }
                            g.drawImage(image, headX, y * SEGMENT_SIZE, 2 * SEGMENT_SIZE, SEGMENT_SIZE, this);
                            isTheNextObjectDrawn = false;
                            continue;
                        }
                    } else if (field[x][y] > 1) {           // туловища других змеек
                        if (rivalSnakeHeadLeftImage == null) {
                            g.setColor(Color.BLACK.darker());
                        } else {
                            g.drawImage(rivalSnakeBodyImage, x * SEGMENT_SIZE, y * SEGMENT_SIZE, SEGMENT_SIZE, SEGMENT_SIZE, this);
                            continue;
                        }
                    } else if (field[x][y] < -1 && field[x][y] != -Integer.MAX_VALUE) {          // головы других змеек
                        if (rivalSnakeHeadRightImage == null) {
                            g.setColor(new Color(10, 40, 10));
                        } else {
                            if (!isTheNextObjectDrawn) {
                                isTheNextObjectDrawn = true;
                                continue;
                            }
                            int headX = x * SEGMENT_SIZE;
                            Image image;
                            if (x + 1 == rivalHeadPrevPositionX && y == rivalHeadPrevPositionY) {
                                image = rivalSnakeHeadLeftImage;
                            } else if (x == rivalHeadPrevPositionX && y + 1 == rivalHeadPrevPositionY) {
                                image = rivalSnakeHeadUpImage;
                                headX = x * SEGMENT_SIZE - SEGMENT_SIZE / 2;
                            } else if (x == rivalHeadPrevPositionX && y - 1 == rivalHeadPrevPositionY) {
                                image = rivalSnakeHeadDownImage;
                                headX = x * SEGMENT_SIZE - SEGMENT_SIZE / 2;
                            } else if (x - 1 == rivalHeadPrevPositionX && y == rivalHeadPrevPositionY) {
                                image = rivalSnakeHeadRightImage;
                            } else {
                                image = rivalPrevHeadImage;
                            }
                            g.drawImage(image, headX, y * SEGMENT_SIZE, 2 * SEGMENT_SIZE, SEGMENT_SIZE, this);
                            isTheNextObjectDrawn = false;
                            rivalHeadPrevPositionX = x;
                            rivalHeadPrevPositionY = y;
                            rivalPrevHeadImage = image;
                            continue;
                        }
                    } else {
                        continue;
                    }
                    g.fillRect(x * SEGMENT_SIZE, y * SEGMENT_SIZE, SEGMENT_SIZE, SEGMENT_SIZE);
                }
            }
        }
        g.setColor(new Color(0, 0, 0));
        g.setFont(new Font("Century Gothic", 4, 22));
        g.drawString(info.substring(0, info.indexOf("\n")), getWidth() / 2 - 100, getHeight() - 25);
        g.drawString(info.substring(info.indexOf("\n") + 1, info.length()), getWidth() / 2 - 100, getHeight() - 5);
    }

    private Image getImageDependingOnDirection() {
        Image image;
        switch (direction) {
            case 1:
                image = snakeHeadUpImage;
                break;
            case 2:
                image = snakeHeadLeftImage;
                break;
            case 3:
                image = snakeHeadDownImage;
                break;
            default:
                image = snakeHeadRightImage;
                break;
        }
        return image;
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

    void setDirection(int direction) {
        this.direction = direction;
    }
}
