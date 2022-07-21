package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private static final int PORT = 8000;

    public static void main(String[] args) throws IOException {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен");
            try (Socket socket = serverSocket.accept();
                 DataInputStream in = new DataInputStream(socket.getInputStream());
                 final DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
                final Scanner scanner = new Scanner(System.in);
                System.out.println("Клиент подключился");
                while (!socket.isClosed()) {
                    Thread thread1 = new Thread(() -> {
                        try {
                            String a = in.readUTF();
                            System.out.println(a);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    Thread thread2 = new Thread(() -> {
                        try {
                            out.writeUTF("Server: " + scanner.nextLine());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    thread1.start();
                    thread2.start();
                    thread1.join();
                    thread2.join();
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
