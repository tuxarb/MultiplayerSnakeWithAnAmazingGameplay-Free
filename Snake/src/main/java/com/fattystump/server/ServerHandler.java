package com.fattystump.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.fattystump.Request;
import com.fattystump.Response;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ServerHandler implements ActionListener {

    private ServerFrame serverFrame;
    private Server server;
    private Game game;
    private ArrayList<Integer> deadIds = new ArrayList<>();
    private HashMap<Integer, Integer> clients = new HashMap<>();
    private ArrayList<String> banList = new ArrayList<>();
    public Timer timer;

    public ServerHandler(ServerFrame serverFrame) {
        this.serverFrame = serverFrame;
    }

    public void start() {
        server = new Server(8192, 8192);
        server.start();

        try {
            server.bind(50000, 50001);
            log("Сервер запущен!");

            Kryo kryo = server.getKryo();
            kryo.register(Request.class);
            kryo.register(Response.class);

            server.addListener(new Listener() {
                @Override
                public void received(Connection connection, Object object) {
                    if (object instanceof Request) {
                        Request request = (Request)object;
                        handleRequest(request.getContent(), connection);
                    }
                }

                @Override
                public void connected(Connection connection) {
                    if (banList.contains(connection.getRemoteAddressTCP().getAddress().toString())) {
                        log("Забаненный IP " + connection.getRemoteAddressTCP().getAddress() + " пытался присоединиться");
                        respond("ban;Вы были забанены! Вы лох. Чмо. Нет друзей. Даже в змейке вас забанили.", connection);
                        connection.close();
                    } else
                        log("Игровой клиент " +
                                connection.getID() +
                                " (" +
                                connection.getRemoteAddressTCP().getAddress() +
                                ") присоединился");
                }

                @Override
                public void disconnected(Connection connection) {
                    log("Игровой клиент " + connection.getID() + " отсоединился");
                    if (clients.containsKey(connection.getID())) {
                        int playerID = clients.get(connection.getID());
                        game.players.set(playerID - 2, null);
                        log("Игрок #" + playerID + " удален");
                        deadIds.add(playerID);
                        clients.remove(connection.getID());
                    }
                }
            });

            game = new Game();
            timer = new Timer(Game.TICK, this);
            timer.start();
            log("Игра создана. Сыграем? ;)");

        } catch (Exception e) {
            log(e.toString());
        }
    }

    public void stop() {
        server.stop();
        timer.stop();
        log("Сервер остановлен. Недолго музыка играла, недолго фраер танцевал");
    }

    private void log(String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        serverFrame.getTextLog().setText(
                serverFrame.getTextLog().getText() +
                        dateFormat.format(new Date()) +
                        " " +
                        message +
                        "\n"
        );
    }

    public void handleCommand(String command) {

    }

    private void handleRequest(String content, Connection connection) {

    }

    private void respond(String content, Connection connection) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
