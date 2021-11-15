package org.elsys.ip.spring.app;

import org.elsys.ip.spring.MyLogic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {
    @Bean
    MyLogic logic() {
        return new MyLogic();
    }
}
