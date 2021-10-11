package org.elsys.ip.calculator;

import java.util.List;

public class SubCommand extends AbstractCommand implements Command {
    public SubCommand(Memory memory) {
        super(memory);
    }

    @Override
    public String execute(List<String> args) {
        return String.valueOf(args.stream().
                mapToDouble(x -> parse(x)).
                reduce((x, y) -> x - y).orElseGet(() -> 0d));
    }
}
