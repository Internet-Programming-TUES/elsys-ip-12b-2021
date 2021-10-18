package org.elsys.ip.sockets.chat;

import javax.sound.midi.SysexMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;
import java.util.Scanner;

public class ChatClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        new Thread(() -> {
            try {
                while (true) {
                    String line = in.readLine();
                    if (line == null) {
                        System.out.println("<<<<<< Connection Closed >>>>>>");
                        System.exit(0);
                    }
                    System.out.println(line);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }).start();
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public static void main(String[] args) throws IOException {
        ChatClient client = new ChatClient();
        client.startConnection("localhost", 6666);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine();
            client.sendMessage(line);
        }
    }
}
