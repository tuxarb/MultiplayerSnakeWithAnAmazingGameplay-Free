package com.fattystump.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

class ClientFrame extends JFrame {
    private ClientHandler clientHandler;

    private ClientFrame() {
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
        setResizable(false);
        URL iconURL = getClass().getResource("/images/icon.jpg");
        if (iconURL != null) {
            ImageIcon icon = new ImageIcon(iconURL);
            setIconImage(icon.getImage());
        }
        setVisible(true);
        init();
    }

    private void init() {
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout(0, 0));
        setContentPane(content);
        Field field = new Field();
        content.add(field);
        pack();
        setLocationRelativeTo(null);
        clientHandler = new ClientHandler(field, this);
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
