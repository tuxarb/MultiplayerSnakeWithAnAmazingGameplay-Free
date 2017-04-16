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
import java.util.Formatter;
import java.util.HashMap;

public class ServerHandler implements ActionListener {

    private static final String PLAYER_NOT_FOUND_ERROR = "Игрок #%d не найден";
    private static final String PLAYER_KAMIKAZE_HINT = "Игрок #%d становится камикадзе";
    private static final String BAN_IP_HINT = "Был забанен IP %s";
    private static final String UNBAN_IP_HINT = "Был разабанен IP %s";
    private static final String SET_PLAYER_SCORE_HINT = "Игроку #%d присвоено %d очков";
    private static final String SPEED_INCORRECT_VALUE_ERROR = "Некорректное значение уровня скорости";
    private static final String SPEED_CHANGED_HINT = "Скорость была изменена";
    private static final String UNKNOWN_COMMAND_ERROR = "Че?";

    private ServerFrame serverFrame;
    private Server server;
    private Game game;
    private ArrayList<Integer> deadIds = new ArrayList<>();
    private HashMap<Integer, Integer> clients = new HashMap<>();
    private ArrayList<String> banList = new ArrayList<>();
    private Timer timer;

    ServerHandler(ServerFrame serverFrame) {
        this.serverFrame = serverFrame;
    }

    void start() {
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
                        respond("ban Вы были забанены! Вы лох. Чмо. Нет друзей. Даже в змейке вас забанили.", connection);
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
                        game.players.set(playerID, null);
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
                        "\n");
    }

    public void handleCommand(String command) {
        /** COMMANDS LIST:
         * freeze {id}
         * unfreeze {id}
         * slowdown {id} {steps}
         * kamikaze {id}
         * ban {ip}
         * unban {ip}
         * score {id} {score}
         * speed {level} | level in [-9;9]
         * gamemode {mode} | 0 - normal, 1 - deadPlayersBecomeSolids
         * addsolid {x} {y}
         * remsolid {x} {y}
         * clearsolids
         */
        System.out.println("Command: " + command);

        String commandOperator = command.substring(0, command.indexOf(" "));
        switch (commandOperator) {
            case "freeze":
                int playerId = Integer.parseInt(
                        command.substring(command.indexOf(" ") + 1));
                Player player = getPlayerById(playerId);
                if (player == null) {
                    log(new Formatter()
                            .format(PLAYER_NOT_FOUND_ERROR, playerId)
                            .toString());
                    return;
                }
                if (player.isFreeze()) return;
                player.setFreeze(!player.isFreeze());
                break;
            case "unfreeze":
                playerId = Integer.parseInt(
                        command.substring(command.indexOf(" ") + 1));
                player = getPlayerById(playerId);
                if (player == null) {
                    log(new Formatter()
                            .format(PLAYER_NOT_FOUND_ERROR, playerId)
                            .toString());
                    return;
                }
                if (!player.isFreeze()) return;
                player.setFreeze(!player.isFreeze());
                break;
            case "kamikaze":
                playerId = Integer.parseInt(
                        command.substring(command.indexOf(" ") + 1));
                player = getPlayerById(playerId);
                if (player == null) {
                    log(new Formatter()
                            .format(PLAYER_NOT_FOUND_ERROR, playerId)
                            .toString());
                    return;
                }
                player.setKamikaze(player.getDirection());
                log(new Formatter()
                        .format(PLAYER_KAMIKAZE_HINT, player.getId())
                        .toString());
                break;
            case "ban":
                String ip = command.substring(command.indexOf(" ") + 1);
                banList.add(ip);
                for (Connection c : server.getConnections()) {
                    if (c.getRemoteAddressTCP().getAddress().toString().equals(ip)) {
                        respond("ban Вы были забанены.", c);
                        c.close();
                    }
                }
                log(new Formatter()
                        .format(BAN_IP_HINT, ip)
                        .toString());
                break;
            case "unban":
                ip = command.substring(command.indexOf(" ") + 1);
                if (banList.contains(ip)) banList.remove(ip);
                log(new Formatter()
                        .format(UNBAN_IP_HINT, ip)
                        .toString());
                break;
            case "score":
                String args[] = command.substring(command.indexOf(" ") + 1).split(" ");
                playerId = Integer.valueOf(args[0]);
                player = getPlayerById(playerId);
                if (player == null) {
                    log(new Formatter()
                            .format(PLAYER_NOT_FOUND_ERROR, playerId)
                            .toString());
                    return;
                }
                player.setScore(Integer.parseInt(args[1]));
                log(new Formatter()
                        .format(SET_PLAYER_SCORE_HINT, player.getId(), player.getScore())
                        .toString());
                break;
            case "speed":
                int speedLevel = Integer.valueOf(command.substring(command.indexOf(" ") + 1));
                if (speedLevel < -9 || speedLevel > 9) {
                    log(SPEED_INCORRECT_VALUE_ERROR);
                    return;
                }
                timer.setDelay((10 - (-speedLevel)) * 10);
                log(SPEED_CHANGED_HINT);
                break;
            default:
                log(UNKNOWN_COMMAND_ERROR);
        }
    }

    private Player getPlayerById(int playerId) {
        Player player;
        try {
            player = game.players.get(playerId);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
        return player;
    }

    private void handleRequest(String content, Connection connection) {

    }

    private void respond(String content, Connection connection) {
        Response response = new Response();
        response.setContent(content);
        connection.sendTCP(response);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
