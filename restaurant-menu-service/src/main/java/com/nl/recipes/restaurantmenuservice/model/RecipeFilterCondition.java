package com.nl.recipes.restaurantmenuservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeFilterCondition {
    private String type;
    private Integer servingCapacity;
    private IngredientSearchVariant ingredientSearchVO;
    private String instructions;
}
