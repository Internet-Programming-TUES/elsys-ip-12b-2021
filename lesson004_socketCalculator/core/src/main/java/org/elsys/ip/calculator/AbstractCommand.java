package org.elsys.ip.calculator;

public abstract class AbstractCommand implements Command {
    protected final Memory memory;

    protected AbstractCommand(Memory memory) {
        this.memory = memory;
    }

    protected double parse(String arg) {
        try {
            return Double.parseDouble(arg);
        } catch (Throwable t) {
            return memory.
                    getValue(arg);
        }
    }
}
