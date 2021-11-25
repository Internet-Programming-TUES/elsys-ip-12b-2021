package org.elsys.ip.springweb;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("base")
public class OurController {
    @GetMapping("5")
    public String executeGet1() {
        return "Hello world";
    }

    @GetMapping("{id}")
    public String executeGet(@PathVariable String id, @RequestParam(defaultValue = "0") int query) {
        return String.valueOf(Integer.parseInt(id) + query);
    }

    @PostMapping
    public SlojenObect create(@RequestBody SlojenObect body) {
        return new SlojenObect(body.getId() + 1, body.getName() + " Changed", body.getList());
    }
}
