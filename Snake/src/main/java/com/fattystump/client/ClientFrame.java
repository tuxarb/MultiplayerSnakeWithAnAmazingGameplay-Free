package com.fattystump.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class ClientFrame extends JFrame {
    private ClientHandler clientHandler;

    ClientFrame() {
        setTitle("Змейка");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                String buttons[] = {"Да", "Нет"};
                int result = JOptionPane.showOptionDialog(
                        ClientFrame.this,
                        "Ты уверен, что хочешь выйти?",
                        "Подтверждение",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        buttons,
                        buttons[1]
                );
                if (result == JOptionPane.YES_OPTION) {
                    if (clientHandler != null) {
                        clientHandler.stop();
                    }
                    System.exit(0);
                }
            }
        });
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
        Field field = new Field(getWidth(), getHeight());
        add(field);
        clientHandler = new ClientHandler(field);
        clientHandler.startHandling();
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
