package com.Stratleaf.StratLeaf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class StratController {

    @Autowired
    private StratRepository stratRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "StratLeaf");
        model.addAttribute("pageContent", "home :: content");
        return "layout";
    }

    @GetMapping("/strats")
    public String showStrats(Model model) {
        List<Strat> strats = stratRepository.findAll();
        model.addAttribute("STRATS", strats);
        return "strats";
    }
}
