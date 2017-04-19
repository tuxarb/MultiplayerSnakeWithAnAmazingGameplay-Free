package com.fattystump.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.fattystump.Request;
import com.fattystump.Response;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;

class ServerHandler implements ActionListener {
    private static final String PLAYER_NOT_FOUND_ERROR = "Игрок #%d не найден";
    private static final String PLAYER_KAMIKAZE_HINT = "Игрок #%d становится камикадзе";
    private static final String BAN_IP_HINT = "Был забанен IP %s";
    private static final String UNBAN_IP_HINT = "Был разабанен IP %s";
    private static final String SET_PLAYER_SCORE_HINT = "Игроку #%d присвоено %d очков";
    private static final String SPEED_INCORRECT_VALUE_ERROR = "Некорректное значение уровня скорости";
    private static final String SPEED_CHANGED_HINT = "Скорость была изменена";
    private static final String UNKNOWN_COMMAND_ERROR = "Че?";
    private static final String NEW_PLAYER_JOINED_HINT = "Игрок #%d (игровой клиент  %d) присоединился к игре";
    private static final String NEW_HIGHSCORE_HINT = "Игрок #%d установил новый рекорд - %d";
    private static final String PLAYER_DEATH_HINT = "Игрок #%d умирает. Светлая память";
    private ServerFrame serverFrame;
    private Server server;
    private Game game;
    private List<Integer> deadIds = new ArrayList<>();
    private Map<Integer, Integer> clients = new HashMap<>();
    private List<String> banList = new ArrayList<>();
    private Timer timer;

    ServerHandler(ServerFrame serverFrame) {
        this.serverFrame = serverFrame;
    }

    void start() {
        server = new Server(15000, 15000);
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
                        Request request = (Request) object;
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
                        game.getPlayers().set(playerID - 2, null);
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

    void stop() {
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

    void handleCommand(String command) {
        /** COMMANDS LIST:
         * freeze {id}
         * unfreeze {id}
         * kamikaze {id}
         * ban {ip}
         * unban {ip}
         * score {id} {score}
         * speed {level} | level in [-9;9]
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

    private void handleRequest(String content, Connection connection) {
        System.out.println("Request: " + content);
        if (content.startsWith("getNewId")) {
            Player newPlayer = new Player(game.getPlayers().size() + 2);
            game.getPlayers().add(newPlayer);
            respond("setNewId " + newPlayer.getId(), connection);
            clients.put(connection.getID(), newPlayer.getId());
            log(new Formatter()
                    .format(NEW_PLAYER_JOINED_HINT, newPlayer.getId(), connection.getID())
                    .toString());
        } else if (content.startsWith("direction")) {
            String args[] = content.substring(content.indexOf(" ") + 1).split(" ");
            int playerId = Integer.valueOf(args[0]);
            Player player = getPlayerById(playerId);
            if (player == null) {
                log(new Formatter()
                        .format(PLAYER_NOT_FOUND_ERROR, playerId)
                        .toString());
                return;
            }
            player.setDirection(Integer.valueOf(args[1]));
        } else if (content.startsWith("getHighscore")) {
            respond("getHighscore " + game.getHighScore(), connection);
        }
    }

    private Player getPlayerById(int playerId) {
        Player player;
        try {
            player = game.getPlayers().get(playerId - 2);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
        return player;
    }

    private void respond(String content, Connection connection) {
        Response response = new Response();
        response.setContent(content);
        connection.sendTCP(response);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        game.update();

        String raw = "update " +
                Game.WIDTH +
                " " +
                Game.HEIGHT +
                " ";
        for (int y = 0; y < Game.HEIGHT; y++) {
            for (int x = 0; x < Game.WIDTH; x++)
                raw += String.valueOf(game.field[x][y]) + ":";
            raw = raw.substring(0, raw.length() - 1);
            raw += ";";
        }
        raw = raw.substring(0, raw.length() - 1);

        Response response = new Response();
        response.setContent(raw);
        server.sendToAllTCP(response);

        for (int i = 0; i < game.getPlayers().size(); i++) {
            Player player = game.getPlayers().get(i);
            if (player != null) {
                if (player.isUpdateScore()) {
                    response = new Response();
                    response.setContent("score " + player.getId() + " " + player.getScore());
                    server.sendToAllTCP(response);
                    player.setUpdateScore(false);
                }
                if (player.getScore() > game.getHighScore()) {
                    game.setHighScore(player.getScore());
                    log(new Formatter()
                            .format(NEW_HIGHSCORE_HINT, player.getId(), game.getHighScore())
                            .toString());
                }
                response = new Response();
                response.setContent("highscore " + game.getHighScore());
                server.sendToAllTCP(response);
            } else if (player == null && !deadIds.contains(i + 2)) {
                deadIds.add(i + 2);
                response = new Response();
                response.setContent("dead " + (i + 2));
                server.sendToAllTCP(response);
                log(new Formatter()
                        .format(PLAYER_DEATH_HINT, (i + 2))
                        .toString());
            }
        }
    }
}
