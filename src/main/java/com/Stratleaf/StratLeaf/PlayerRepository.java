package com.Stratleaf.StratLeaf;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
    long countByActiveTrue();

    Optional<Player> findByName(String name);
}
