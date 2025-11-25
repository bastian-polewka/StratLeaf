package com.Stratleaf.StratLeaf;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StratRepository extends JpaRepository<Strat, Integer> {
    List<Strat> findByMap(CSMap map);
}
