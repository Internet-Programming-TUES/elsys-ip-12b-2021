package org.elsys.ip.sockets;

import org.elsys.ip.calculator.CommandExecutor;
import org.elsys.ip.calculator.CommandFactory;
import org.elsys.ip.calculator.ExitException;
import org.elsys.ip.calculator.Memory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CalculatorServer {
    private ServerSocket serverSocket;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true) {
            new CalculatorClientHandler(serverSocket.accept()).start();
        }
    }

    private static class CalculatorClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public CalculatorClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                Memory memory = new Memory();
                CommandFactory commandFactory = new CommandFactory(memory);
                CommandExecutor commandExecutor = new CommandExecutor(commandFactory);
                while (true) {
                    String line = in.readLine();
                    if (line == null) {
                        break;
                    }
                    List<String> lineSplit =
                            Arrays.stream(line.split(" ")).collect(Collectors.toList());
                    String result = commandExecutor.execute(
                            lineSplit.get(0),
                            lineSplit.stream().skip(1).collect(Collectors.toList()));
                    out.println(result);
                }
            } catch (ExitException exit) {
                // Do nothing
            } catch (Throwable t) {
                System.out.println(t.getMessage());
            } finally {
                dispose();
            }
        }

        private void dispose() {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (clientSocket != null) clientSocket.close();
            } catch (Throwable t) {
                System.out.println(t.getMessage());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        CalculatorServer server = new CalculatorServer();
        server.start(6666);
    }
}
