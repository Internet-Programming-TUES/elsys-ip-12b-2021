package org.elsys.ip.calculator;

import java.util.List;

public class AddCommand extends AbstractCommand implements Command {
    public AddCommand(Memory memory) {
        super(memory);
    }

    @Override
    public String execute(List<String> args) {
        return String.valueOf(args.stream().
                mapToDouble(x -> parse(x)).
                reduce((x, y) -> x + y).orElseGet(() -> 0d));
    }
}
