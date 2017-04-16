package com.fattystump.client;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.fattystump.Request;
import com.fattystump.Response;
import com.sun.javafx.binding.StringFormatter;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.InetAddress;

class ClientHandler {
    private Field field;
    private Client clientKryo;
    private int id;
    private int score;
    private boolean isAlive;
    private KeywordHandler keywordHandler;

    ClientHandler(Field field) {
        this.field = field;
        this.keywordHandler = new KeywordHandler();
    }

    void startHandling() {
        clientKryo = new Client(6000, 6000);
        clientKryo.start();
        field.setInfo(String.format(Field.SCORE_INFO, score, field.getHighscore()));
        try {
            Kryo kryo = clientKryo.getKryo();
            kryo.register(Request.class);
            kryo.register(Response.class);
            String serverIP = JOptionPane.showInputDialog("Укажите IP сервера");
            InetAddress serverAddress = InetAddress.getByName(serverIP);
            clientKryo.connect(2000, serverAddress, 50000, 50001);
            clientKryo.addListener(new Listener() {
                @Override
                public void received(Connection connection, Object object) {
                    if (object instanceof Response) {
                        handleResponse(((Response) object).getContent());
                    }
                }

                void handleResponse(String responseContent) {
                    if (responseContent.startsWith("setNewId")) {
                        id = Integer.valueOf(responseContent.split(" ")[1]);
                        field.setClientId(id);
                        score = 0;
                        isAlive = true;
                        field.setInfo(StringFormatter.format(Field.SCORE_INFO, score, field.getHighscore()).getValue());
                    } else if (responseContent.startsWith("update")) {
                        String[] content = responseContent.split(" ");
                        int sizeX = Integer.valueOf(content[1]);
                        int sizeY = Integer.valueOf(content[2]);
                        String fieldPoints = content[3];
                        String[] y = fieldPoints.split(";");
                        int[][] field = new int[sizeX][sizeY];
                        for (int i = 0; i < y.length; i++) {
                            String[] x = y[i].split("-");
                            for (int j = 0; j < x.length; j++) {
                                field[j][i] = Integer.valueOf(x[j]);
                            }
                        }
                        if (!isAlive) {
                            field[0][0] = -id;
                        }
                        ClientHandler.this.field.setField(field);
                        ClientHandler.this.field.repaint();
                    } else if (responseContent.startsWith("score")) {
                        String[] response = responseContent.split(" ");
                        if (id == Integer.valueOf(response[1])) {
                            score = Integer.valueOf(response[2]);
                            field.setInfo(String.format(Field.SCORE_INFO, score, field.getHighscore()));
                            field.repaint();
                        }
                    } else if (responseContent.startsWith("highscore")) {
                        field.setHighscore(Integer.valueOf(responseContent.split(" ")[1]));
                        if (isAlive) {
                            field.setInfo(String.format(Field.SCORE_INFO, score, field.getHighscore()));
                        } else {
                            field.setInfo(String.format("Жми пробел для старта\nРекорд=%d", field.getHighscore()));
                        }
                        field.repaint();
                    } else if (responseContent.startsWith("dead")) {
                        int id = Integer.valueOf(responseContent.split(" ")[1]);
                        if (id == ClientHandler.this.id) {
                            ClientHandler.this.id = -1;
                            isAlive = false;
                            field.setInfo(String.format(
                                    "Кажется, вы труп. Нажмите пробел, чтобы родиться заново.\nРекорд=%d", field.getHighscore())
                            );
                            field.repaint();
                        }
                    } else if (responseContent.startsWith("ban")) {
                        field.setInfo(responseContent.split(" ")[1]);
                        isAlive = false;
                        field.repaint();
                    }
                }

                @Override
                public void disconnected(Connection connection) {
                    JOptionPane.showMessageDialog(
                            field,
                            "Соединение потеряно!",
                            "Потеря соединения",
                            JOptionPane.ERROR_MESSAGE
                    );
                    field.repaint();
                    isAlive = false;
                    id = -1;
                    request("getHighscore");
                }
            });
            field.addKeyListener(keywordHandler);
            request("getHighscore");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    field,
                    e.toString(),
                    "Ошибка!",
                    JOptionPane.ERROR_MESSAGE
            );
            int result = JOptionPane.showOptionDialog(null, "Сервер не найден.", "Ошибка",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null,
                    new String[]{"Повторить", "Завершить"}, "Повторить");
            if (result == 0) {
                startHandling();
            } else {
                System.exit(1);
            }
        }
    }

    private void request(String requestContent) {
        Request request = new Request();
        request.setContent(requestContent);
        clientKryo.sendTCP(request);
    }

    void stop() {
        clientKryo.close();
        clientKryo.stop();
    }

    private class KeywordHandler implements KeyListener {
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            int direction = -1;
            if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                direction = 0;
            } else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
                direction = 1;
            } else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                direction = 2;
            } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
                direction = 3;
            } else if (key == KeyEvent.VK_SPACE && !isAlive) {
                System.out.println(123);
                request("getNewId");
            }
            if (direction != -1 && isAlive) {
                request("direction " + String.valueOf(id) + " " + String.valueOf(direction));
            }
        }

        public void keyReleased(KeyEvent e) {
        }

        public void keyTyped(KeyEvent e) {
        }
    }
}
