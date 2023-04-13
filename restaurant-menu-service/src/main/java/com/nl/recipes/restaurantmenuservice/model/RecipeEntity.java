package com.nl.recipes.restaurantmenuservice.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "Recipes")
public class RecipeEntity {
    @Id
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TYPE")
    private String type;


    @Column(name = "SERVING_CAPACITY")
    private Integer servingCapacity;

    @ElementCollection
    @Embedded
    private List<IngredientsEntity> ingredients;

    @Column(name = "INSTRUCTIONS")
    private String instructions;
}
