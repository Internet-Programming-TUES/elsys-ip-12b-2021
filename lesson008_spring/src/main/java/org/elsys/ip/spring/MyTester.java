package org.elsys.ip.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MyTester {
    private ApplicationContext context;

    @Autowired
    public MyTester(ApplicationContext context) {
        this.context = context;
        System.out.println("MyTester()");
        MyLogic logic = context.getBean(MyLogic.class);
        System.out.println("logic = " + logic.getString());
    }

    @PostConstruct
    public void onPost() {
        System.out.println("MyTester.onPost()");
        MyLogic logic = context.getBean(MyLogic.class);
        System.out.println("logic = " + logic.getString());
        MyTester t = context.getBean(MyTester.class);
    }
}
