package com.Stratleaf.StratLeaf;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @EmbeddedId
    private RoleKey id;

    @ManyToOne
    @MapsId("stratID")
    @JoinColumn(name = "strat_id")
    private Strat strat;

    @ManyToOne
    @MapsId("playerID")
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(name = "role_description")
    private String roleDescription;

    public Role() {}

    public Role(Strat strat, Player player, String roleDescription) {
        this.id = new RoleKey();
        this.id.setStratID(strat.getId());
        this.id.setPlayerID(player.getId());
        this.strat = strat;
        this.player = player;
        this.roleDescription = roleDescription;
    }

}
