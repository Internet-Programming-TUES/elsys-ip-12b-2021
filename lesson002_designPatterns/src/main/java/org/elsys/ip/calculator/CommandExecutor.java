package org.elsys.ip.calculator;

import java.util.List;

public class CommandExecutor {
    private final CommandFactory commandFactory;

    public CommandExecutor(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    public String execute(
            String name,
            List<String> args) {
        Command command = commandFactory.createCommand(name);
        if (command == null) {
            return "No handler for " + name;
        }
        return command.execute(args);
    }
}
