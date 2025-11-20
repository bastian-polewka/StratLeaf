package com.Stratleaf.StratLeaf;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
public class StratUpdateRequest {
    private Integer id;
    private String type;
    private String name;
    private String description;
    private String notes;
    private Status status;
    private Side side;
    private CSMap map;
    private List<Player> players;
    private List<String> roleDescriptions;
}