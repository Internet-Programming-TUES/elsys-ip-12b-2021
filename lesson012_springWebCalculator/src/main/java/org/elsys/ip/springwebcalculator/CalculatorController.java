package org.elsys.ip.springwebcalculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CalculatorController {
    @Autowired
    private CalculatorCore calculatorCore;

    @GetMapping("/calculator")
    public String form(Model model) {
        model.addAttribute("input", new InputModel());
        model.addAttribute("result", null);
        return "form";
    }

    @PostMapping("/calculator")
    public String result(@ModelAttribute InputModel input, Model model) {
        List<String> lineSplit =
                Arrays.stream(input.getCommand().split(" ")).collect(Collectors.toList());
        String result = calculatorCore.execute(
                lineSplit.get(0),
                lineSplit.stream().skip(1).collect(Collectors.toList()));
        model.addAttribute("result", result);
        model.addAttribute("input", input);
        return "form";
    }

}