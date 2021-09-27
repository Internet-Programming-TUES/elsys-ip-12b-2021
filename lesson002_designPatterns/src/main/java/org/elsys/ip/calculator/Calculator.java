package org.elsys.ip.calculator;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CommandFactory commandFactory = new CommandFactory();
        CommandExecutor commandExecutor = new CommandExecutor(commandFactory);
        while (true) {
            String line = scanner.nextLine();
            List<String> lineSplit =
                    Arrays.stream(line.split(" ")).toList();
            String result = commandExecutor.execute(
                    lineSplit.get(0),
                    lineSplit.stream().skip(1).toList());
            System.out.println(result);
        }
    }
}
