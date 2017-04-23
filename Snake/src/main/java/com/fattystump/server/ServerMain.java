package com.fattystump.server;


import java.awt.*;

class ServerMain {
    public static void main(String[] args) {
        String mode = System.getProperty("mode");
        if (mode != null && "console".equals(mode.toLowerCase())) {
            new ServerConsole();
        } else {
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
}
