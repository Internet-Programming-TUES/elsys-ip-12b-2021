package org.elsys.ip.springwebcalculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class CalculatorController {
    @Autowired
    private CalculatorCore calculatorCore;

    @GetMapping("/calculator")
    public String form(Model model) {
        model.addAttribute("input", new InputModel());
        model.addAttribute("commands", getCommands());
        return "form";
    }

    @PostMapping("/calculator")
    public String result(@ModelAttribute InputModel input, Model model) {
        List<String> lineSplit =
                Arrays.stream(input.getArgs().split(" ")).collect(Collectors.toList());
        String result = calculatorCore.execute(
                input.getCommand(),
                new ArrayList<>(lineSplit));
        input.getResult().add(result);
        model.addAttribute("input", input);
        model.addAttribute("commands", getCommands());
        return "form";
    }

    private Map<String, String> getCommands() {
        var map = new HashMap<String, String>();
        map.put("add", "Addition");
        map.put("sub", "Substitution");
        map.put("mem", "Memory");

        return map;
    }

}