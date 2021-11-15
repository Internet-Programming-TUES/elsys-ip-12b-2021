package org.elsys.ip.spring;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

public class MyLogic {
    public MyLogic() {
        System.out.println("MyLogic()");
    }

    public String getString() {
        return "LOGIC";
    }

    @PostConstruct
    public void onPost() {
        System.out.println("MyLogic.onPost()");
    }
}
