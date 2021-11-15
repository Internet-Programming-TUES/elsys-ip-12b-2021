package org.elsys.ip.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class OurSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(OurSpringApplication.class, args);
    }

    @Autowired
    private ApplicationContext context;
}
