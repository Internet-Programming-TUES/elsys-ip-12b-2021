package org.elsys.ip.calculator;

public abstract class AbstractCommand implements Command {
    protected double parse(String arg) {
        try {
            return Double.parseDouble(arg);
        } catch (Throwable t) {
            return Memory.getInstance().
                    getValue(arg);
        }
    }
}
