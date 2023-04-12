package com.nl.recipes.restaurantmenuservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeVariant {
    private Integer id;
    private String name;
    private String type;
    private Integer servingCapacity;
    private List<IngredientVariant> ingredientsList = new ArrayList<>();
    private String instructions;

}
