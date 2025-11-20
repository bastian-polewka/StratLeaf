package com.Stratleaf.StratLeaf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class StratController {

    @Autowired
    private StratRepository stratRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "StratLeaf");
        model.addAttribute("pageContent", "home :: content");
        return "layout";
    }

    @GetMapping("/strats")
    public String showStrats(Model model) {
        model.addAttribute("STRATS", stratRepository.findAll());
        List<Player> activePlayers = playerRepository.findAll()
                .stream()
                .filter(Player::isActive)
                .toList();
        model.addAttribute("ACTIVE_PLAYERS", activePlayers);
        model.addAttribute("ROLES", roleRepository.findAll());
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


        List<Player> resolvedPlayers = new ArrayList<>();

        List<Player> players = request.getPlayers();
        List<String> roles = request.getRoleDescriptions();

        for (int i = 0; i < 5; i++) {
            String playerName = players.get(i).getName();
            Player player = playerRepository.findByName(playerName)
                    .orElseGet(() -> {
                        // create new player if not found
                        Player newPlayer = new Player();
                        newPlayer.setName(playerName);
                        newPlayer.setActive(true);
                        return playerRepository.save(newPlayer);
                    });

            resolvedPlayers.add(player);

            String roleDescription = roles.get(i);
            RoleKey key = new RoleKey();
            key.setStratID(strat.getId());
            key.setPlayerID(player.getId()); // TODO: Change it
            Role role = roleRepository.findById(key)
                    .orElseGet(() -> {
                        Role newRole = new Role(strat, player, roleDescription); // TODO: Change it
                        return roleRepository.save(newRole);
                    });

            role.setRoleDescription(roleDescription);

        }


        /*
        for (Player incoming : request.getPlayers()) {
            Player player = playerRepository.findByName(incoming.getName())
                    .orElseGet(() -> {
                        // create new player if not found
                        Player newPlayer = new Player();
                        newPlayer.setName(incoming.getName());
                        newPlayer.setActive(true);
                        return playerRepository.save(newPlayer);
                    });

            resolvedPlayers.add(player);
        }

        for (String incoming : request.getRoleDescriptions()) {
            RoleKey key = new RoleKey();
            key.setStratID(strat.getId());


            key.setPlayerID(resolvedPlayers.getFirst().getId()); // TODO: Change it
            Role role = roleRepository.findById(key)
                    .orElseGet(() -> {
                        Role newRole = new Role(strat, resolvedPlayers.getFirst(), incoming); // TODO: Change it
                        return roleRepository.save(newRole);
                    });

            role.setRoleDescription(incoming);
        }
        */


        stratRepository.save(strat);

        return ResponseEntity.ok().build();
    }
}
