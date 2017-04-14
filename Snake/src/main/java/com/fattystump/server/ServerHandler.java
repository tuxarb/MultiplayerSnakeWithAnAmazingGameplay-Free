package com.fattystump.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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

    }

    public void stop() {

    }

    private void log(String message) {

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
