package com.Stratleaf.StratLeaf;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Controller
public class TestController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "StratLeaf");
        model.addAttribute("pageContent", "home :: content");
        return "layout";
    }


}
