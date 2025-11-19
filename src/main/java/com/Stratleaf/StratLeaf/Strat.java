package com.Stratleaf.StratLeaf;

import jakarta.persistence.*;


@Entity
@Table(name = "STRATS")
public class Strat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String type;
    private String name;
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
    private String description;
    private String notes;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Side side;
    @Enumerated(EnumType.STRING)
    private CSMap map;

    // Default constructor (required by JPA)
    public Strat() {}

    public Strat(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public CSMap getMap() {
        return map;
    }

    public void setMap(CSMap map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "Strat{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", player=" + (player != null ? player.getId() : null) +
                ", description='" + description + '\'' +
                ", notes='" + notes + '\'' +
                ", status=" + status +
                ", side=" + side +
                ", map=" + map +
                '}';
    }
}
