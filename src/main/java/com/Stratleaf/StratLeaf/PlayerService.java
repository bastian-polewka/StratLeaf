package com.Stratleaf.StratLeaf;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Transactional
    public Player savePlayer(Player player) {
        if (player.isActive()) {
            long activeCount = playerRepository.countByActiveTrue();
            if (activeCount >= 5) {
                throw new IllegalStateException("Cannot have more than 5 active players");
            }
        }
        return playerRepository.save(player);
    }
}
