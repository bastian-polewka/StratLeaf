package com.Stratleaf.StratLeaf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class StratController {

    @Autowired
    private StratRepository stratRepository;
    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "StratLeaf");
        model.addAttribute("pageContent", "home :: content");
        return "layout";
    }

    @GetMapping("/strats")
    public String showStrats(Model model) {
        model.addAttribute("STRATS", stratRepository.findAll());
        model.addAttribute("PLAYERS", playerRepository.findAll());
        return "strats";
    }

    @PostMapping("/update-strat")
    @ResponseBody
    public ResponseEntity<Void> updateStrat(@RequestBody StratUpdateRequest request) {

        Strat strat = stratRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Strat not found"));

        strat.setType(request.getType());
        strat.setName(request.getName());
        strat.setDescription(request.getDescription());
        strat.setNotes(request.getNotes());
        strat.setStatus(request.getStatus());
        strat.setSide(request.getSide());
        strat.setMap(request.getMap());

        System.out.println("hey: " + strat.toString());
        System.out.println("hey2: " + request.toString());

        // PLAYER RELATION
        if (request.getPlayerId() == null) {
            strat.setPlayer(null);
        } else {
            Player player = playerRepository.findById(request.getPlayerId())
                    .orElse(null);
            strat.setPlayer(player);
        }

        stratRepository.save(strat);

        return ResponseEntity.ok().build();
    }
}
