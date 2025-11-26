package com.Stratleaf.StratLeaf;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, RoleKey> {

    public void deleteByStratId(Integer stratId);
}
