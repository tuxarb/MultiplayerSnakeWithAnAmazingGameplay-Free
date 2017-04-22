package com.fattystump.server;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ServerFrame extends JFrame {
    private ServerHandler serverHandler;
    private JButton buttonStart;
    private JButton buttonStop;
    private JTextPane textLog;
    private JTextField textCmd;

    private ServerFrame() {
        setTitle("Змеиная Fерма (© Жирный Пень, 2017)");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 550, 400);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(3, 3, 3, 3));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.NORTH);

        buttonStart = new JButton("Старт");
        buttonStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                serverHandler = new ServerHandler(ServerFrame.this);
                serverHandler.start();
                buttonStart.setEnabled(false);
                buttonStop.setEnabled(true);
                textCmd.setEnabled(true);
            }
        });
        panel.add(buttonStart);

        buttonStop = new JButton("Стоп");
        buttonStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                serverHandler.stop();
                buttonStart.setEnabled(true);
                buttonStop.setEnabled(false);
                textCmd.setText("");
                textCmd.setEnabled(false);
            }
        });
        buttonStop.setEnabled(false);
        panel.add(buttonStop);

        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);

        textLog = new JTextPane();
        textLog.setEnabled(false);
        textLog.setEditable(false);
        scrollPane.setViewportView(textLog);

        textCmd = new JTextField();
        textCmd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (serverHandler != null)
                    serverHandler.handleCommand(textCmd.getText());
                textCmd.setText("");
            }
        });
        contentPane.add(textCmd, BorderLayout.SOUTH);
        textCmd.setColumns(10);
        textCmd.setEnabled(false);

        JButton commands = new JButton("Команды");
        commands.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu menu = new JPopupMenu();
                addNewMenuItem(menu, "cкорость", "speed -9");
                addNewMenuItem(menu, "очки", "score 2 100");
                addNewMenuItem(menu, "камикадзе", "kamikaze 2");
                addNewMenuItem(menu, "заморозка", "freeze 2");
                addNewMenuItem(menu, "отморозка", "unfreeze 2");
                addNewMenuItem(menu, "бан", "ban 127.0.0.1");
                addNewMenuItem(menu, "разбан", "unban 127.0.0.1");
                menu.show(commands, commands.getX() / 3, commands.getY() / 3 + 15);
            }

            void addNewMenuItem(JPopupMenu menu, String name, String command) {
                JMenuItem newMenuItem = new JMenuItem(name);
                newMenuItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        textCmd.setText(command);
                    }
                });
                menu.add(newMenuItem);
            }
        });
        panel.add(commands);

        DefaultCaret caret = (DefaultCaret) textLog.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }

    JTextPane getTextLog() {
        return textLog;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ServerFrame frame = new ServerFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
