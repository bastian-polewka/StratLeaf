package com.Stratleaf.StratLeaf;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class RoleKey implements Serializable {

    @Column(name = "strat_id")
    private Integer stratID;

    @Column(name = "player_id")
    private Integer playerID;

}
