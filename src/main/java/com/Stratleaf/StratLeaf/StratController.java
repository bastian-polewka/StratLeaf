package com.Stratleaf.StratLeaf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

public class StratController {

    @Autowired
    private StratRepository stratRepository;

    @GetMapping("/strats")
    public String showStrats(Model model) {
        model.addAttribute("users", stratRepository.findAll());
        return "strats";
    }
}
