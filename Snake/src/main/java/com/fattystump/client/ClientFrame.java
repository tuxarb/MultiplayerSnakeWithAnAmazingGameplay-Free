package com.fattystump.client;

import javax.swing.*;
import java.awt.*;

public class ClientFrame extends JFrame {

    ClientFrame() {
        setTitle("Snake");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(
                Toolkit.getDefaultToolkit().getScreenSize().width - 200,
                Toolkit.getDefaultToolkit().getScreenSize().height - 200
        );
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        init();
    }

    private void init() {
        add(new Field());
        new ClientHandler().startHandling();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientFrame();
            }
        });
    }
}
