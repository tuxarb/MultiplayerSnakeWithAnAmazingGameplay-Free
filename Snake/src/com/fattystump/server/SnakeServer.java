package com.fattystump.server;

import javax.swing.*;
import java.awt.*;

/**
 * Created by vkhly on 13.04.2017.
 */
public class SnakeServer extends JFrame {

    // TODO: добавить серверный обработчик (класс ServerHandler)
    private JPanel contentPane;
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

    SnakeServer() {
        setTitle("Multiplayer Snake Server (© 2017 Fatty Stump)");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 550, 400);


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
