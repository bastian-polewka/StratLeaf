package com.Stratleaf.StratLeaf;

import com.Stratleaf.StratLeaf.RoleRepository;
import com.Stratleaf.StratLeaf.StratRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StratService {

    private final RoleRepository roleRepository;
    private final StratRepository stratRepository;

    @Autowired
    public StratService(RoleRepository roleRepository, StratRepository stratRepository) {
        this.roleRepository = roleRepository;
        this.stratRepository = stratRepository;
    }

    @Transactional
    public void deleteStratAndAssociatedRoles(Integer stratId) {
        roleRepository.deleteByStratId(stratId);
        stratRepository.deleteById(stratId);
    }
}