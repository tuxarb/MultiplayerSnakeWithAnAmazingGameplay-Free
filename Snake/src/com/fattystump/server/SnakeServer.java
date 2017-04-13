package com.fattystump.server;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SnakeServer extends JFrame {

    // TODO: добавить серверный обработчик (класс ServerHandler)
    private JButton buttonStart;
    private JButton buttonStop;
    private JTextPane textLog;
    private JTextField textCmd;

    public JButton getButtonStart() {
        return buttonStart;
    }

    public void setButtonStart(JButton buttonStart) {
        this.buttonStart = buttonStart;
    }

    public JButton getButtonStop() {
        return buttonStop;
    }

    public void setButtonStop(JButton buttonStop) {
        this.buttonStop = buttonStop;
    }

    public JTextPane getTextLog() {
        return textLog;
    }

    public void setTextLog(JTextPane textLog) {
        this.textLog = textLog;
    }

    private SnakeServer() {
        setTitle("Multiplayer Snake Server (© 2017 Fatty Stump)");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 550, 400);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.NORTH);

        buttonStart = new JButton("Start");
        buttonStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: запуск сервера через экземпляр серверного обработчика
            }
        });
        panel.add(buttonStart);

        buttonStop = new JButton("Stop");
        buttonStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: остановка сервера через экземпляр серверного обработчика
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
                // TODO: выполнение команды на сервере через экземпляр серверного обработчика
                textCmd.setText("");
            }
        });
        contentPane.add(textCmd, BorderLayout.SOUTH);
        textCmd.setColumns(10);

        DefaultCaret caret = (DefaultCaret)textLog.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SnakeServer frame = new SnakeServer();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
