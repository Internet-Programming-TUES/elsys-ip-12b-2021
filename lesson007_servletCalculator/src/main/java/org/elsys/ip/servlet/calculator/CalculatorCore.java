package org.elsys.ip.servlet.calculator;

import org.elsys.ip.calculator.CommandExecutor;
import org.elsys.ip.calculator.CommandFactory;
import org.elsys.ip.calculator.Memory;

import java.util.List;

public class CalculatorCore {
    private final Memory memory = new Memory();
    private final CommandFactory commandFactory = new CommandFactory(memory);
    private final CommandExecutor commandExecutor = new CommandExecutor(commandFactory);

    public String execute(
            String name,
            List<String> args) {
        return commandExecutor.execute(name, args);
    }
}
