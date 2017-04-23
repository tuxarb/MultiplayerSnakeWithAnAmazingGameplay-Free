package com.fattystump.server;


import java.awt.*;

class ServerMain {
    public static void main(String[] args) {
        String mode = System.getProperty("mode");
        if (mode != null && "gui".equals(mode.toLowerCase())) {
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
        } else {
            new ServerConsole();
        }
    }
}
