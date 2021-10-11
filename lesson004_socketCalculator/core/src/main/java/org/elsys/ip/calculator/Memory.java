package org.elsys.ip.calculator;

import java.util.HashMap;
import java.util.Map;

public class Memory {
    private Map<String, Double> memory = new HashMap<>();

    public double getValue(String name) {
        return memory.getOrDefault(name, 0d);
    }

    public void setValue(String name, double value) {
        memory.put(name, value);
    }
}
