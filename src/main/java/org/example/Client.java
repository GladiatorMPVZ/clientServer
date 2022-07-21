package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        openConnection();
    }

    public static void openConnection() throws IOException {

        try (Socket socket = new Socket("localhost", 8000);
             final DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            final Scanner scanner = new Scanner(System.in);
            while (!socket.isClosed()) {
                Thread thread3 = new Thread(() -> {
                    try {
                            out.writeUTF("Client:" + scanner.nextLine());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                Thread thread4 = new Thread(() -> {
                    try {
                        String b = in.readUTF();
                        System.out.println(b);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                thread3.start();
                thread4.start();
                thread3.join();
                thread4.join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
