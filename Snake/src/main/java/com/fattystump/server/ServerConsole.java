package com.fattystump.server;


import com.esotericsoftware.minlog.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

class ServerConsole {
    private ServerHandler serverHandler;
    private boolean isServerRun;
    private static final Scanner SCAN = new Scanner(System.in);
    private static final Logger LOG = LoggerFactory.getLogger(ServerConsole.class);

    ServerConsole() {
        Log.NONE();
        start();
    }

    private void start() {
        println("Змеиная Fерма (© Жирный Пень, 2017)");
        displayMainMenu();
        while (true) {
            switch (readLine()) {
                case "1":
                    if (isServerRun) {
                        println("Сервер уже запущен!");
                        LOG.warn("Сервер уже запущен!");
                        break;
                    }
                    serverHandler = new ServerHandler();
                    serverHandler.start();
                    isServerRun = true;
                    println("Сервер был успешно запущен!");
                    break;
                case "2":
                    if (!isServerRun) {
                        println("Сервер еще не запущен, нечего останавливать.");
                        LOG.warn("Сервер еще не запущен, нечего останавливать.");
                        break;
                    }
                    serverHandler.stop();
                    isServerRun = false;
                    println("Сервер был остановлен!");
                    break;
                case "3":
                    if (!isServerRun) {
                        println("Сначала запустите сервер!");
                        LOG.warn("Сначала запустите сервер!");
                        break;
                    }
                    println("1. speed {level} | level in [-9;9]");
                    println("2. score {id} {score}");
                    println("3. kamikaze {id}");
                    println("4. freeze {id}");
                    println("5. unfreeze {id}");
                    println("6. ban {ip}");
                    println("7. unban {ip}");

                    while (true) {
                        println("Введите команду или '0' для возвращения в основное меню.");
                        print("> ");
                        String input = readLine();
                        if ("0".equals(input)) {
                            displayMainMenu();
                            break;
                        }
                        serverHandler.handleCommand(input.toLowerCase());
                    }
                    break;
                case "exit":
                    println("Пока!!");
                    break;
                default:
                    println("Неверный ввод.");
            }
        }
    }

    private void displayMainMenu() {
        print("\n");
        print("\n");
        println("Команды:");
        println("1. Запуск сервера.");
        println("2. Остановка сервера.");
        println("3. Список серверных команд.");
        print("\n");
        print("> ");
    }

    private void println(String text) {
        System.out.println(text);
    }

    private void print(String text) {
        System.out.print(text);
    }

    private String readLine() {
        return SCAN.nextLine();
    }
}

