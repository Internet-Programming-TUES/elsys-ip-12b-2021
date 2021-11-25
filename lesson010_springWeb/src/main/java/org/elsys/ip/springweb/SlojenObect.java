package org.elsys.ip.springweb;

import java.util.List;

public class SlojenObect {
    private final int id;
    private final String name;
    private final List<Integer> list;

    public SlojenObect(int id, String name, List<Integer> list) {
        this.id = id;
        this.name = name;
        this.list = list;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getList() {
        return list;
    }
}
