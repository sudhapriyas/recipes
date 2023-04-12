package com.nl.recipes.restaurantmenuservice.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class IngredientsEntity implements Serializable {
    @Column(name = "name")
    private String name;
}

