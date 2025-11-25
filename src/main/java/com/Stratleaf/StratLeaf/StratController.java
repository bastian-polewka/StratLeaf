package com.Stratleaf.StratLeaf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    public String showStrats(Model model, @RequestParam(name = "mapSelector", required = false) CSMap selectedMap) {
        List<Player> activePlayers = playerRepository.findAll()
                .stream()
                .filter(Player::isActive)
                .toList();
        model.addAttribute("ACTIVE_PLAYERS", activePlayers);

        List<Role> roles = roleRepository.findAll();
        model.addAttribute("ROLES", roles);


        model.addAttribute("selectedMap", selectedMap);

        List<Strat> strats = stratRepository.findByMap(selectedMap);

        System.out.println("HERE MAP: " + selectedMap);
        System.out.println("HERE STRAT: " + strats);

        if (strats.isEmpty()) {
            Strat strat = new Strat(selectedMap);
            stratRepository.save(strat);
            strats = stratRepository.findByMap(selectedMap);

        }
        else {
            checkNewEmptyStrat(selectedMap);
        }

        strats = stratRepository.findByMap(selectedMap);

        model.addAttribute("STRATS", strats);


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


        System.out.println("ROLES" + roles);

        addPlayerAndRole(resolvedPlayers, players, roles, strat);



        stratRepository.save(strat);

        boolean check = checkNewEmptyStrat(request.getMap());


        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete-strat")
    @ResponseBody
    public ResponseEntity<Void> removeStrat(@RequestBody StratUpdateRequest request) {

        Strat strat = stratRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Strat not found"));

        stratRepository.delete(strat);

        boolean check = checkNewEmptyStrat(request.getMap());

        return ResponseEntity.ok().build();
    }


    @ModelAttribute("allMaps")
    public CSMap[] populateMaps() {
        return CSMap.values();
    }

    private void addPlayerAndRole(List<Player> resolvedPlayers, List<Player> players, List<String> roles, Strat strat) {
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
    }

    /**
     * Checks if there is the last strat is empty so it can be edited.
     * If there is no new empty strat it will create it and add it.
     *
     * @param  selectedMap  Map which is currently displayed on the site.
     * @return boolean depending on if there is currently a new empty strat in the table.
     */
    private boolean checkNewEmptyStrat(CSMap selectedMap) {
        List<Strat> strats = stratRepository.findByMap(selectedMap);


        if (strats.isEmpty()) {
            Strat strat = new Strat(selectedMap);
            stratRepository.save(strat);
            return false;
        }
        else {
            if (!strats.getLast().isEmpty()) {

                Strat strat = new Strat(selectedMap);
                stratRepository.save(strat);
                System.out.println("NEW LINE ADDED");

                return false;

            }
            else {
                System.out.println("NO NEW LINE IS ADDED");
                return true;
            }
        }
    }
}
