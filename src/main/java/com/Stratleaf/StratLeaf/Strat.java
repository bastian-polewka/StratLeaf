package com.Stratleaf.StratLeaf;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    public Strat(String name) {
        this.name = name;
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
