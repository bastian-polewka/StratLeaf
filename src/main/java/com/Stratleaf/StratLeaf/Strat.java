package com.Stratleaf.StratLeaf;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "STRATS")
public class Strat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String type;
    private String name;

    private String description;

    private String notes;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Side side;

    @Enumerated(EnumType.STRING)
    private CSMap map;

    @OneToMany(mappedBy = "strat", fetch = FetchType.LAZY)
    private List<Role> roles;

    // Default constructor (required by JPA)
    public Strat() {}

    public Strat(CSMap map) {
        this.status = Status.NotReady;
        this.side = Side.None;
        this.map = map;
        this.roles = new ArrayList<>();
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    public boolean isEmpty() {
        for (Role role : roles) {
            if (!role.isEmpty()) {
                return false;
            }
        }

        return isBlank(type)
                && isBlank(name)
                && isBlank(description)
                && isBlank(notes);
    }

    @Override
    public String toString() {
        return "Strat{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", notes='" + notes + '\'' +
                ", status=" + status +
                ", side=" + side +
                ", map=" + map +
                '}';
    }
}
