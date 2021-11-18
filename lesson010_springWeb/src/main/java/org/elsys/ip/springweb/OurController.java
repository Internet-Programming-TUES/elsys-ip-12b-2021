package org.elsys.ip.springweb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OurController {
    @GetMapping
    public String executeGet() {
        return "Hello world";
    }
}
