package org.elsys.ip.sockets.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatServer {
    private ServerSocket serverSocket;
    private final Map<String, CalculatorClientHandler> clients = new HashMap<>();

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true) {
            CalculatorClientHandler client = new CalculatorClientHandler(serverSocket.accept(), this);
            client.start();
        }
    }

    private static class CalculatorClientHandler extends Thread {
        private final Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private final ChatServer server;
        private String name;

        public CalculatorClientHandler(Socket socket, ChatServer server) {
            this.clientSocket = socket;
            this.server = server;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                name = in.readLine();
                server.addToMap(name, this);

                while (true) {
                    String line = in.readLine();
                    if (line == null) {
                        break;
                    }
                    server.printlnAll(name, line);
                }
            } catch (Throwable t) {
                System.out.println(t.getMessage());
            } finally {
                dispose();
            }
        }

        private void dispose() {
            try {
                server.removeClient(name);
                if (clientSocket != null) clientSocket.close();
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (Throwable t) {
                System.out.println(t.getMessage());
            }
        }
    }

    private void addToMap(String name, CalculatorClientHandler client) {
        clients.put(name, client);
    }

    private void removeClient(String name) {
        clients.remove(name);
    }

    private void printlnAll(String name, String line) {
        if (line.startsWith("dm ")) {
            String dmTo = line.substring(3).split(" ")[0];
            String dmMessage = line.substring(3 + dmTo.length() + 1);
            if (clients.containsKey(dmTo)) {
                clients.get(dmTo).out.println(name + " (dm)> " + dmMessage);
            } else {
                clients.get(name).out.println("Invalid receiver.");
            }
        } else {
            clients.values().forEach(c -> c.out.println(name + " > " + line));
        }
    }

    public static void main(String[] args) throws IOException {
        ChatServer server = new ChatServer();
        server.start(6666);
    }
}
